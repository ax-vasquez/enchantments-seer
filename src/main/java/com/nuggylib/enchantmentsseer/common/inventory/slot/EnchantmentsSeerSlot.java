package com.nuggylib.enchantmentsseer.common.inventory.slot;

import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;

/**
 * Simple class overriding {@link Slot} to help clarify the logic elsewhere in our mod code
 */
public class EnchantmentsSeerSlot extends Slot {

    public EnchantmentsSeerSlot(IInventory container, int slotIndex, int x, int y) {
        super(container, slotIndex, x, y);
    }

}
