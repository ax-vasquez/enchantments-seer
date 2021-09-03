package com.nuggylib.enchantmentsseer.common.tile.component.config.slot;

import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;

import java.util.Arrays;
import java.util.List;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @implNote DataTypes that are not strictly input or output we set as being able to both input and output and allow the slots to determine if something can be
 * inserted/outputted from them.
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/component/config/slot/InventorySlotInfo.java"
 */
public class InventorySlotInfo extends BaseSlotInfo {

    private final List<IInventorySlot> inventorySlots;

    public InventorySlotInfo(boolean canInput, boolean canOutput, IInventorySlot... slots) {
        this(canInput, canOutput, Arrays.asList(slots));
    }

    public InventorySlotInfo(boolean canInput, boolean canOutput, List<IInventorySlot> slots) {
        super(canInput, canOutput);
        inventorySlots = slots;
    }

    public List<IInventorySlot> getSlots() {
        return inventorySlots;
    }

    public boolean hasSlot(IInventorySlot slot) {
        return getSlots().contains(slot);
    }
}
