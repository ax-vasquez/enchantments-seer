package com.nuggylib.enchantmentsseer.api.providers;

import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/providers/IBlockProvider.java"
 */
public interface IBlockProvider extends IItemProvider {

    @Nonnull
    Block getBlock();

    @Deprecated//TODO - 1.17: Remove this as we don't actually use this
    default boolean blockMatches(ItemStack otherStack) {
        Item item = otherStack.getItem();
        return item instanceof BlockItem && blockMatches(((BlockItem) item).getBlock());
    }

    @Deprecated//TODO - 1.17: Remove this as we don't actually use this
    default boolean blockMatches(Block other) {
        return getBlock() == other;
    }

    @Override
    default ResourceLocation getRegistryName() {
        //Make sure to use the block's registry name in case it somehow doesn't match
        return getBlock().getRegistryName();
    }

    @Override
    default String getTranslationKey() {
        return getBlock().getDescriptionId();
    }
}
