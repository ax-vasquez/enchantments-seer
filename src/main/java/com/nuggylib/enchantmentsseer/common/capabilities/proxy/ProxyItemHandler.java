package com.nuggylib.enchantmentsseer.common.capabilities.proxy;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.common.capabilities.holder.IHolder;
import com.nuggylib.enchantmentsseer.api.annotations.FieldsAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/proxy/ProxyItemHandler.java"
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class ProxyItemHandler extends ProxyHandler implements IItemHandlerModifiable {

    private final IItemHandlerModifiable inventory;

    public ProxyItemHandler(IItemHandlerModifiable inventory, @Nullable Direction side, @Nullable IHolder holder) {
        super(side, holder);
        this.inventory = inventory;
    }

    @Override
    public int getSlots() {
        return inventory.getSlots();
    }

    @Override
    public ItemStack getStackInSlot(int slot) {
        return inventory.getStackInSlot(slot);
    }

    @Override
    public ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return readOnly || readOnlyInsert.getAsBoolean() ? stack : inventory.insertItem(slot, stack, Action.get(!simulate).simulate());
    }

    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return readOnly || readOnlyExtract.getAsBoolean() ? ItemStack.EMPTY : inventory.extractItem(slot, amount, Action.get(!simulate).simulate());
    }

    @Override
    public int getSlotLimit(int slot) {
        return inventory.getSlotLimit(slot);
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {
        return !readOnly || inventory.isItemValid(slot, stack);
    }

    @Override
    public void setStackInSlot(int slot, ItemStack stack) {
        if (!readOnly) {
            inventory.setStackInSlot(slot, stack);
        }
    }
}

