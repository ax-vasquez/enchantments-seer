package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import com.nuggylib.enchantmentsseer.common.Action;
import com.nuggylib.enchantmentsseer.common.util.StackUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 * Inspired by Mekanism
 *
 */
public class InsertableSlot extends Slot {

    public InsertableSlot(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Math.min(getMaxStackSize(), stack.getMaxStackSize());
    }

}
