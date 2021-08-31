package com.nuggylib.enchantmentsseer.common.item.block;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <BLOCK>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/item/block/ItemBlockMekanism.java"
 */
public class ItemBlockEnchantmentsSeer<BLOCK extends Block> extends BlockItem {

    @Nonnull
    private final BLOCK block;

    public ItemBlockEnchantmentsSeer(@Nonnull BLOCK block, Item.Properties properties) {
        super(block, properties);
        this.block = block;
    }

    @Nonnull
    @Override
    public BLOCK getBlock() {
        return block;
    }

    @Nonnull
    @Override
    public ITextComponent getName(@Nonnull ItemStack stack) {
        return super.getName(stack);
    }
}
