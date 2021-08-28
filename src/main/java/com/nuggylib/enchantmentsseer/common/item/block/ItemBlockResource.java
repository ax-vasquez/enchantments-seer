package com.nuggylib.enchantmentsseer.common.item.block;

import com.nuggylib.enchantmentsseer.common.block.basic.BlockResource;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/item/block/ItemBlockResource.java"
 */
public class ItemBlockResource extends ItemBlockEnchantmentsSeer<BlockResource> {

    public ItemBlockResource(BlockResource block, Item.Properties properties) {
        super(block, properties);
    }

    @Override
    public int getBurnTime(ItemStack itemStack) {
        return getBlock().getResourceInfo().getBurnTime();
    }
}
