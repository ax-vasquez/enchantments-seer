package com.nuggylib.enchantmentsseer.common.network.to_server;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.network.IEnchantmentsSeerPacket;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import org.apache.logging.log4j.util.BiConsumer;
import org.apache.logging.log4j.util.TriConsumer;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_server/PacketGuiInteract.java"
 */
public class PacketGuiInteract implements IEnchantmentsSeerPacket {

    private final Type interactionType;

    private GuiInteraction interaction;
    private GuiInteractionItem itemInteraction;
    private GuiInteractionEntity entityInteraction;
    private BlockPos tilePosition;
    private ItemStack extraItem;
    private int entityID;
    private int extra;

    public PacketGuiInteract(GuiInteractionEntity interaction, int entityID) {
        this.interactionType = Type.ENTITY;
        this.entityInteraction = interaction;
        this.entityID = entityID;
    }

    public PacketGuiInteract(GuiInteraction interaction, TileEntity tile) {
        this(interaction, tile.getBlockPos());
    }

    public PacketGuiInteract(GuiInteraction interaction, TileEntity tile, int extra) {
        this(interaction, tile.getBlockPos(), extra);
    }

    public PacketGuiInteract(GuiInteraction interaction, BlockPos tilePosition) {
        this(interaction, tilePosition, 0);
    }

    public PacketGuiInteract(GuiInteraction interaction, BlockPos tilePosition, int extra) {
        this.interactionType = Type.INT;
        this.interaction = interaction;
        this.tilePosition = tilePosition;
        this.extra = extra;
    }

    public PacketGuiInteract(GuiInteractionItem interaction, TileEntity tile, ItemStack stack) {
        this(interaction, tile.getBlockPos(), stack);
    }

    public PacketGuiInteract(GuiInteractionItem interaction, BlockPos tilePosition, ItemStack stack) {
        this.interactionType = Type.ITEM;
        this.itemInteraction = interaction;
        this.tilePosition = tilePosition;
        this.extraItem = stack;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        PlayerEntity player = context.getSender();
        if (player != null) {
            if (interactionType == Type.ENTITY) {
                Entity entity = player.level.getEntity(entityID);
                if (entity != null) {
                    entityInteraction.consume(entity, player);
                }
            } else {
                TileEntityEnchantmentsSeer tile = WorldUtils.getTileEntity(TileEntityEnchantmentsSeer.class, player.level, tilePosition);
                if (tile != null) {
                    if (interactionType == Type.INT) {
                        interaction.consume(tile, player, extra);
                    } else if (interactionType == Type.ITEM) {
                        itemInteraction.consume(tile, player, extraItem);
                    }
                }
            }
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(interactionType);
        if (interactionType == Type.ENTITY) {
            buffer.writeEnum(entityInteraction);
            buffer.writeVarInt(entityID);
        } else if (interactionType == Type.INT) {
            buffer.writeEnum(interaction);
            buffer.writeBlockPos(tilePosition);
            buffer.writeVarInt(extra);
        } else if (interactionType == Type.ITEM) {
            buffer.writeEnum(itemInteraction);
            buffer.writeBlockPos(tilePosition);
            buffer.writeItem(extraItem);
        }
    }

    public static PacketGuiInteract decode(PacketBuffer buffer) {
        Type type = buffer.readEnum(Type.class);
        if (type == Type.ENTITY) {
            return new PacketGuiInteract(buffer.readEnum(GuiInteractionEntity.class), buffer.readVarInt());
        } else if (type == Type.INT) {
            return new PacketGuiInteract(buffer.readEnum(GuiInteraction.class), buffer.readBlockPos(), buffer.readVarInt());
        } else if (type == Type.ITEM) {
            return new PacketGuiInteract(buffer.readEnum(GuiInteractionItem.class), buffer.readBlockPos(), buffer.readItem());
        }
        EnchantmentsSeer.LOGGER.error("Received malformed GUI interaction packet.");
        return null;
    }

    public enum GuiInteractionItem {
        ;

        private final TriConsumer<TileEntityEnchantmentsSeer, PlayerEntity, ItemStack> consumerForTile;

        GuiInteractionItem(TriConsumer<TileEntityEnchantmentsSeer, PlayerEntity, ItemStack> consumerForTile) {
            this.consumerForTile = consumerForTile;
        }

        public void consume(TileEntityEnchantmentsSeer tile, PlayerEntity player, ItemStack stack) {
            consumerForTile.accept(tile, player, stack);
        }
    }

    public enum GuiInteraction {//TODO: Cleanup this enum/the elements in it as it is rather disorganized order wise currently
        CRAFT_SINGLE((tile, player, extra) -> {

        }),

        ;

        private final TriConsumer<TileEntityEnchantmentsSeer, PlayerEntity, Integer> consumerForTile;

        GuiInteraction(TriConsumer<TileEntityEnchantmentsSeer, PlayerEntity, Integer> consumerForTile) {
            this.consumerForTile = consumerForTile;
        }

        public void consume(TileEntityEnchantmentsSeer tile, PlayerEntity player, int extra) {
            consumerForTile.accept(tile, player, extra);
        }
    }

    public enum GuiInteractionEntity {
        ;

        private final BiConsumer<Entity, PlayerEntity> consumerForEntity;

        GuiInteractionEntity(BiConsumer<Entity, PlayerEntity> consumerForTile) {
            this.consumerForEntity = consumerForTile;
        }

        public void consume(Entity entity, PlayerEntity player) {
            consumerForEntity.accept(entity, player);
        }
    }

    private enum Type {
        ENTITY,
        ITEM,
        INT;
    }

}
