package com.nuggylib.enchantmentsseer.common.capabilities.holder.slot;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.IHolder;
import com.nuggylib.enchantmentsseer.common.inventory.IInventorySlot;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

public interface IInventorySlotHolder extends IHolder {

    @Nonnull
    List<IInventorySlot> getInventorySlots(@Nullable Direction side);
}
