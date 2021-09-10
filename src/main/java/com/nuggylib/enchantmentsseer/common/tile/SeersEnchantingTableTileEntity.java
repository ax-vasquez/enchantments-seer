package com.nuggylib.enchantmentsseer.common.tile;

import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerTileEntityTypes;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import net.minecraft.tileentity.EnchantingTableTileEntity;

/**
 * Our version of the {@link EnchantingTableTileEntity}
 *
 * @see "https://mcforge.readthedocs.io/en/1.16.x/datastorage/capabilities/#forge-provided-capabilities"
 */
public class SeersEnchantingTableTileEntity extends TileEntity implements IItemHandler {

    public SeersEnchantingTableTileEntity() {
        super(EnchantmentsSeerTileEntityTypes.SEERS_ENCHANTING_TABLE.get());
    }

    @Override
    public int getSlots() {
        return 0;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }
}
