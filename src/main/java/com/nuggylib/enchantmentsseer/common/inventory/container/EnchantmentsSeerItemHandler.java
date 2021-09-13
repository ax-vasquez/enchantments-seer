package com.nuggylib.enchantmentsseer.common.inventory.container;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.jetbrains.annotations.NotNull;

public class EnchantmentsSeerItemHandler implements IItemHandlerModifiable {

    public EnchantmentsSeerItemHandler() {}

    @Override
    public void setStackInSlot(int slot, @NotNull ItemStack stack) {

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
        EnchantmentsSeer.logger.info("Insert item!");
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
