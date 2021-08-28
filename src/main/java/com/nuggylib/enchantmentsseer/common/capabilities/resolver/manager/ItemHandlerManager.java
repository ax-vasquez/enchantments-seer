package com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.IInventorySlotHolder;
import com.nuggylib.enchantmentsseer.common.capabilities.proxy.ProxyItemHandler;
import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/resolver/manager/ItemHandlerManager.java"
 */
public class ItemHandlerManager extends CapabilityHandlerManager<IInventorySlotHolder, IInventorySlot, IItemHandler, IItemHandlerModifiable> {

    public ItemHandlerManager(@Nullable IInventorySlotHolder holder, @Nonnull IItemHandlerModifiable baseHandler) {
        super(holder, baseHandler, CapabilityItemHandler.ITEM_HANDLER_CAPABILITY, ProxyItemHandler::new, IInventorySlotHolder::getInventorySlots);
    }
}
