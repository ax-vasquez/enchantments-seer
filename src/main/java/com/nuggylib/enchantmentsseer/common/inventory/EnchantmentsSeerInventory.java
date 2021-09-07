package com.nuggylib.enchantmentsseer.common.inventory;

import net.minecraft.inventory.Inventory;

/**
 * Simple class to extend/override the base {@link Inventory} functionality.
 *
 * This class was originally intended to clarify the usage of methods in the base class, but we can use this to further
 * modify the logic later.
 */
public class EnchantmentsSeerInventory extends Inventory {

    public EnchantmentsSeerInventory(int size) {
        super(size);
    }

}
