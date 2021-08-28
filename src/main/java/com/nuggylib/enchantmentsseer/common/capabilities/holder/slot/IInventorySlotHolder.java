package com.nuggylib.enchantmentsseer.common.capabilities.holder.slot;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.IHolder;
import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/holder/slot/IInventorySlotHolder.java"
 */
public interface IInventorySlotHolder extends IHolder {

    @Nonnull
    List<IInventorySlot> getInventorySlots(@Nullable Direction side);
}
