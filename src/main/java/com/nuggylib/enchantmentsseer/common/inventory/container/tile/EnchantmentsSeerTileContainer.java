package com.nuggylib.enchantmentsseer.common.inventory.container.tile;

import com.nuggylib.enchantmentsseer.common.inventory.IInventorySlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.network.PacketBuffer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Inspired by Mekanism
 *
 * This class extends the custom {@link Container} class and provides operations that require the use of both the
 * container and its underlying {@link TileEntity}.
 *
 * This class is necessary for any block that does something when activated, such as open a GUI, stores items, or is
 * animated. Not every use case will need all the methods in this class, but this class provides all the methods that
 * any such block could need to do its job.
 */
public class EnchantmentsSeerTileContainer<TILE extends TileEntity> extends EnchantmentsSeerContainer {

    @Nonnull
    protected final TILE tile;

    protected EnchantmentsSeerTileContainer(ContainerType<?> type, int id, PlayerInventory inv, @Nonnull TILE tile) {
        super(type, id, inv);
        this.tile = tile;
    }

    public TILE getTileEntity() {
        return tile;
    }

    @Override
    protected void openInventory(@Nonnull PlayerInventory inv) {
        super.openInventory(inv);
        // TODO
        tile.open(inv.player);
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        //prevent Containers from remaining valid after the chunk has unloaded;
        // TODO
        return tile.hasGui() && !tile.isRemoved() && WorldUtils.isBlockLoaded(tile.getLevel(), tile.getBlockPos());
    }

    @Override
    protected void addSlots() {
        super.addSlots();
        // TODO
        if (tile.hasInventory()) {
            //Get all the inventory slots the tile has
            List<IInventorySlot> inventorySlots = tile.getInventorySlots(null);
            for (IInventorySlot inventorySlot : inventorySlots) {
                Slot containerSlot = inventorySlot.createContainerSlot();
                if (containerSlot != null) {
                    addSlot(containerSlot);
                }
            }
        }
    }

    @Nonnull
    public static <TILE extends TileEntity> TILE getTileFromBuf(PacketBuffer buf, Class<TILE> type) {
        if (buf == null) {
            throw new IllegalArgumentException("Null packet buffer");
        }
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            BlockPos pos = buf.readBlockPos();
            TILE tile = WorldUtils.getTileEntity(type, Minecraft.getInstance().level, pos);
            if (tile == null) {
                throw new IllegalStateException("Client could not locate tile at " + pos + " for tile container. "
                        + "This is likely caused by a mod breaking client side tile lookup");
            }
            return tile;
        });
    }
}
