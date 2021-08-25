package com.nuggylib.enchantmentsseer.inventory.container.slot;

import com.nuggylib.enchantmentsseer.api.annotations.FieldsAreNonnullByDefault;
import com.nuggylib.enchantmentsseer.common.content.qio.QIOCraftingWindow;
import com.nuggylib.enchantmentsseer.inventory.slot.CraftingWindowInventorySlot;
import mcp.MethodsReturnNonnullByDefault;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CraftingWindowOutputInventorySlot extends CraftingWindowInventorySlot {

    public static CraftingWindowOutputInventorySlot create(QIOCraftingWindow window) {
        return new CraftingWindowOutputInventorySlot(window);
    }

    private CraftingWindowOutputInventorySlot(QIOCraftingWindow window) {
        super(manualOnly, internalOnly, window, null);
    }

    @Nonnull
    @Override
    public VirtualInventoryContainerSlot createContainerSlot() {
        return new VirtualCraftingOutputSlot(this, getSlotOverlay(), this::setStackUnchecked, craftingWindow);
    }
}
