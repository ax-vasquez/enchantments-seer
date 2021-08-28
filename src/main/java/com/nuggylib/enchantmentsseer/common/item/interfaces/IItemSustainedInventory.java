package com.nuggylib.enchantmentsseer.common.item.interfaces;

import com.nuggylib.enchantmentsseer.api.NBTConstants;
import com.nuggylib.enchantmentsseer.common.tile.interfaces.ISustainedInventory;
import com.nuggylib.enchantmentsseer.common.util.ItemDataUtils;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.ListNBT;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/item/interfaces/IItemSustainedInventory.java"
 */
public interface IItemSustainedInventory extends ISustainedInventory {

    @Override
    default void setInventory(ListNBT nbtTags, Object... data) {
        if (data[0] instanceof ItemStack) {
            ItemDataUtils.setList((ItemStack) data[0], NBTConstants.ITEMS, nbtTags);
        }
    }

    @Override
    default ListNBT getInventory(Object... data) {
        if (data[0] instanceof ItemStack) {
            return ItemDataUtils.getList((ItemStack) data[0], NBTConstants.ITEMS);
        }
        return null;
    }
}
