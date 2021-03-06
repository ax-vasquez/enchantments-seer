package com.nuggylib.enchantmentsseer.common.inventory;

import com.nuggylib.enchantmentsseer.common.Action;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.items.ItemHandlerHelper;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IInventorySlot extends INBTSerializable<CompoundNBT> {

    ItemStack getStack();

    void setStack(ItemStack stack);

    default ItemStack insertItem(ItemStack stack, Action action ) {
        if (stack.isEmpty() || !isItemValid(stack)) {
            //"Fail quick" if the given stack is empty or we can never insert the item or currently are unable to insert it
            return stack;
        }
        int needed = getLimit(stack) - getCount();
        if (needed <= 0) {
            //Fail if we are a full slot
            return stack;
        }
        boolean sameType = false;
        if (isEmpty() || (sameType = ItemHandlerHelper.canItemStacksStack(getStack(), stack))) {
            int toAdd = Math.min(stack.getCount(), needed);
            if (action.execute()) {
                //If we want to actually insert the item, then update the current item
                if (sameType) {
                    // Note: this also will mark that the contents changed
                    //We can just grow our stack by the amount we want to increase it
                    growStack(toAdd, action);
                } else {
                    //If we are not the same type then we have to copy the stack and set it
                    // Note: this also will mark that the contents changed
                    ItemStack toSet = stack.copy();
                    toSet.setCount(toAdd);
                    setStack(toSet);
                }
            }
            ItemStack remainder = stack.copy();
            remainder.setCount(stack.getCount() - toAdd);
            return remainder;
        }
        //If we didn't accept this item, then just return the given stack
        return stack;
    }

    default ItemStack extractItem(int amount, Action action) {
        if (isEmpty() || amount < 1) {
            //"Fail quick" if we don't can never extract from this slot, have an item stored, or the amount being requested is less than one
            return ItemStack.EMPTY;
        }
        ItemStack current = getStack();
        //Ensure that if this slot allows going past the max stack size of an item, that when extracting we don't act as if we have more than
        // the max stack size, as the JavaDoc for IItemHandler requires that the returned stack is not larger than its stack size
        int currentAmount = Math.min(getCount(), current.getMaxStackSize());
        if (currentAmount < amount) {
            //If we are trying to extract more than we have, just change it so that we are extracting it all
            amount = currentAmount;
        }
        //Note: While we technically could just return the stack itself if we are removing all that we have, it would require a lot more checks
        // especially for supporting the fact of limiting by the max stack size.
        ItemStack toReturn = current.copy();
        toReturn.setCount(amount);
        if (action.execute()) {
            //If shrink gets the size to zero it will update the empty state so that isEmpty() returns true.
            // Note: this also will mark that the contents changed
            shrinkStack(amount, action);
        }
        return toReturn;
    }

    int getLimit(ItemStack stack);

    boolean isItemValid(ItemStack stack);

    @Nullable
    Slot createContainerSlot();

    default int setStackSize(int amount, Action action) {
        if (isEmpty()) {
            return 0;
        } else if (amount <= 0) {
            if (action.execute()) {
                setEmpty();
            }
            return 0;
        }
        ItemStack stack = getStack();
        int maxStackSize = getLimit(stack);
        if (amount > maxStackSize) {
            amount = maxStackSize;
        }
        if (stack.getCount() == amount || action.simulate()) {
            //If our size is not changing or we are only simulating the change, don't do anything
            return amount;
        }
        ItemStack newStack = stack.copy();
        newStack.setCount(amount);
        setStack(newStack);
        return amount;
    }

    default int growStack(int amount, Action action) {
        int current = getCount();
        if (amount > 0) {
            //Cap adding amount at how much we need, so that we don't risk integer overflow
            amount = Math.min(amount, getLimit(getStack()));
        }
        int newSize = setStackSize(current + amount, action);
        return newSize - current;
    }

    default int shrinkStack(int amount, Action action) {
        return -growStack(-amount, action);
    }

    default boolean isEmpty() {
        return getStack().isEmpty();
    }

    default void setEmpty() {
        setStack(ItemStack.EMPTY);
    }

    default int getCount() {
        return getStack().getCount();
    }
}
