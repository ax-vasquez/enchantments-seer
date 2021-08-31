package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.common.util.StackUtils;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism code; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/InsertableSlot.java"
 */
public class InsertableSlot extends Slot implements IInsertableSlot {

    public InsertableSlot(IInventory inventory, int index, int x, int y) {
        super(inventory, index, x, y);
    }

    @Override
    public int getMaxStackSize(ItemStack stack) {
        return Math.min(getMaxStackSize(), stack.getMaxStackSize());
    }

    @Nonnull
    @Override
    public ItemStack insertItem(@Nonnull ItemStack stack, Action action) {
        if (stack.isEmpty() || !mayPlace(stack)) {
            //TODO: Should we even be checking isItemValid
            //"Fail quick" if the given stack is empty or we are not valid for the slot
            return stack;
        }
        ItemStack current = getItem();
        int needed = getMaxStackSize(stack) - current.getCount();
        if (needed <= 0) {
            //Fail if we are a full slot
            return stack;
        }
        if (current.isEmpty() || ItemHandlerHelper.canItemStacksStack(current, stack)) {
            int toAdd = Math.min(stack.getCount(), needed);
            if (action.execute()) {
                //If we want to actually insert the item, then update the current item
                //Set the stack to our new stack (we have no simple way to increment the stack size) so we have to set it instead of being able to just grow it
                set(StackUtils.size(stack, current.getCount() + toAdd));
            }
            return StackUtils.size(stack, stack.getCount() - toAdd);
        }
        //If we didn't accept this item, then just return the given stack
        return stack;
    }
}
