package com.nuggylib.enchantmentsseer.common.inventory.container.tile;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.EnchantmentsSeerInventory;
import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.VirtualInventoryContainerSlot;
import com.nuggylib.enchantmentsseer.common.inventory.slot.EnchantmentsSeerSlot;
import com.nuggylib.enchantmentsseer.common.registration.impl.ContainerTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Inspired by Mekanism code
 *
 * This is the base container class
 *
 * @param <TILE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/tile/MekanismTileContainer.java"
 */
public class EnchantmentsSeerTileContainer<TILE extends TileEntityEnchantmentsSeer> extends EnchantmentsSeerContainer {

    @Nonnull
    protected final TILE tile;

    public EnchantmentsSeerTileContainer(ContainerTypeRegistryObject<?> type, int id, PlayerInventory inv, @Nonnull TILE tile) {
        super(type, id, inv);
        this.tile = tile;
        addSlotsAndOpen();
    }

    public TILE getTileEntity() {
        return tile;
    }

    @Override
    protected void openInventory(@Nonnull PlayerInventory inv) {
        super.openInventory(inv);
        tile.open(inv.player);
    }

    @Override
    protected void closeInventory(@Nonnull PlayerEntity player) {
        super.closeInventory(player);
        tile.close(player);
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        //prevent Containers from remaining valid after the chunk has unloaded;
        return tile.hasGui() && !tile.isRemoved() && WorldUtils.isBlockLoaded(tile.getLevel(), tile.getBlockPos());
    }

    @Override
    protected void addSlots() {
        super.addSlots();
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
