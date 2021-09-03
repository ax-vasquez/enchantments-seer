package com.nuggylib.enchantmentsseer.common.network.to_server;

import com.nuggylib.enchantmentsseer.common.block.attribute.Attribute;
import com.nuggylib.enchantmentsseer.common.block.attribute.AttributeGui;
import com.nuggylib.enchantmentsseer.common.inventory.container.ContainerProvider;
import com.nuggylib.enchantmentsseer.common.network.IEnchantmentsSeerPacket;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Shamelessly-copied from Mekanism; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_server/PacketGuiButtonPress.java"
 */
public class PacketGuiButtonPress implements IEnchantmentsSeerPacket {

    private final Type type;
    private ClickedItemButton itemButton;
    private ClickedTileButton tileButton;
    private ClickedEntityButton entityButton;
    private Hand hand;
    private int entityID;
    private int extra;
    private BlockPos tilePosition;

    public PacketGuiButtonPress(ClickedTileButton buttonClicked, TileEntity tile) {
        this(buttonClicked, tile.getBlockPos());
    }

    public PacketGuiButtonPress(ClickedTileButton buttonClicked, TileEntity tile, int extra) {
        this(buttonClicked, tile.getBlockPos(), extra);
    }

    public PacketGuiButtonPress(ClickedTileButton buttonClicked, BlockPos tilePosition) {
        this(buttonClicked, tilePosition, 0);
    }

    public PacketGuiButtonPress(ClickedItemButton buttonClicked, Hand hand) {
        type = Type.ITEM;
        this.itemButton = buttonClicked;
        this.hand = hand;
    }

    public PacketGuiButtonPress(ClickedTileButton buttonClicked, BlockPos tilePosition, int extra) {
        type = Type.TILE;
        this.tileButton = buttonClicked;
        this.tilePosition = tilePosition;
        this.extra = extra;
    }

    public PacketGuiButtonPress(ClickedEntityButton buttonClicked, int entityID) {
        type = Type.ENTITY;
        this.entityButton = buttonClicked;
        this.entityID = entityID;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        if (player == null) {
            return;
        }
        if (type == Type.ENTITY) {
            Entity entity = player.level.getEntity(entityID);
            if (entity != null) {
                INamedContainerProvider provider = entityButton.getProvider(entity);
                if (provider != null) {
                    //Ensure valid data
                    NetworkHooks.openGui(player, provider, buf -> buf.writeVarInt(entityID));
                }
            }
        } else if (type == Type.TILE) {
            TileEntityEnchantmentsSeer tile = WorldUtils.getTileEntity(TileEntityEnchantmentsSeer.class, player.level, tilePosition);
            if (tile != null) {
                INamedContainerProvider provider = tileButton.getProvider(tile, extra);
                if (provider != null) {
                    //Ensure valid data
                    NetworkHooks.openGui(player, provider, buf -> {
                        buf.writeBlockPos(tilePosition);
                        buf.writeVarInt(extra);
                    });
                }
            }
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(type);
        if (type == Type.ENTITY) {
            buffer.writeEnum(entityButton);
            buffer.writeVarInt(entityID);
        } else if (type == Type.TILE) {
            buffer.writeEnum(tileButton);
            buffer.writeBlockPos(tilePosition);
            buffer.writeVarInt(extra);
        } else if (type == Type.ITEM) {
            buffer.writeEnum(itemButton);
            buffer.writeEnum(hand);
        }
    }

    public static PacketGuiButtonPress decode(PacketBuffer buffer) {
        Type type = buffer.readEnum(Type.class);
        switch (type) {
            case ENTITY:
                return new PacketGuiButtonPress(buffer.readEnum(ClickedEntityButton.class), buffer.readVarInt());
            case TILE:
                return new PacketGuiButtonPress(buffer.readEnum(ClickedTileButton.class), buffer.readBlockPos(), buffer.readVarInt());
            case ITEM:
                return new PacketGuiButtonPress(buffer.readEnum(ClickedItemButton.class), buffer.readEnum(Hand.class));
            default:
                return null;
        }
    }

    public enum ClickedItemButton {
        ;

        private final BiFunction<ItemStack, Hand, INamedContainerProvider> providerFromItem;

        ClickedItemButton(BiFunction<ItemStack, Hand, INamedContainerProvider> providerFromItem) {
            this.providerFromItem = providerFromItem;
        }

        public INamedContainerProvider getProvider(ItemStack stack, Hand hand) {
            return providerFromItem.apply(stack, hand);
        }
    }

    public enum ClickedTileButton {
        ;

        private final BiFunction<TileEntityEnchantmentsSeer, Integer, INamedContainerProvider> providerFromTile;

        ClickedTileButton(BiFunction<TileEntityEnchantmentsSeer, Integer, INamedContainerProvider> providerFromTile) {
            this.providerFromTile = providerFromTile;
        }

        public INamedContainerProvider getProvider(TileEntityEnchantmentsSeer tile, int extra) {
            return providerFromTile.apply(tile, extra);
        }
    }

    public enum ClickedEntityButton {
        ;

        private final Function<Entity, INamedContainerProvider> providerFromEntity;

        ClickedEntityButton(Function<Entity, INamedContainerProvider> providerFromEntity) {
            this.providerFromEntity = providerFromEntity;
        }

        public INamedContainerProvider getProvider(Entity entity) {
            return providerFromEntity.apply(entity);
        }
    }

    public enum Type {
        TILE,
        ITEM,
        ENTITY;
    }
}