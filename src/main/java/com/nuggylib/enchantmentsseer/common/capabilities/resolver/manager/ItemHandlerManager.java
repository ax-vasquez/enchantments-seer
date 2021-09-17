package com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.IInventorySlotHolder;
import com.nuggylib.enchantmentsseer.common.inventory.IInventorySlot;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Inspired by Mekanism
 *
 * Our version of a custom {@link IItemHandler} class that emulates how Mekanism does it, but removes any notion of
 * "side" logic (e.g., wherever Mekanism uses {@link Direction})
 */
public class ItemHandlerManager extends CapabilityHandlerManager<IInventorySlotHolder, IInventorySlot, IItemHandler> {

    public ItemHandlerManager(@Nullable IInventorySlotHolder holder, @Nonnull IItemHandler baseHandler) {
        super(holder, baseHandler, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, IInventorySlotHolder::getInventorySlots);
    }
}
