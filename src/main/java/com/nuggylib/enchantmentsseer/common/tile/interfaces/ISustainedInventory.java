package com.nuggylib.enchantmentsseer.common.tile.interfaces;

import net.minecraft.nbt.ListNBT;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/interfaces/ISustainedInventory.java"
 */
public interface ISustainedInventory {

    /**
     * Sets the inventory tag list to a new value.
     *
     * @param nbtTags - NBTTagList value to set
     * @param data    - ItemStack parameter if using on item
     */
    void setInventory(ListNBT nbtTags, Object... data);

    /**
     * Gets the inventory tag list from an item or block.
     *
     * @param data - ItemStack parameter if using on item
     *
     * @return inventory tag list
     */
    ListNBT getInventory(Object... data);

    /**
     * Gets if there is an inventory from an item or block.
     *
     * @param data - ItemStack parameter if using on item
     *
     * @return true if there is a non empty inventory stored, false otherwise
     */
    default boolean hasInventory(Object... data) {
        ListNBT inventory = getInventory(data);
        return inventory != null && !inventory.isEmpty();
    }
}
