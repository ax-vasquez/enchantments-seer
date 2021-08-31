package com.nuggylib.enchantmentsseer.api.providers;

import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/providers/IItemProvider.java"
 */
public interface IItemProvider extends IBaseProvider, net.minecraft.util.IItemProvider {

    /**
     * Gets the item this provider represents.
     */
    @Nonnull
    Item getItem();//TODO - 1.17: Replace this with just using vanilla's asItem?

    @Nonnull
    @Override
    default Item asItem() {
        return getItem();
    }

    /**
     * Creates an item stack of size one using the item this provider represents.
     */
    @Nonnull
    default ItemStack getItemStack() {
        return getItemStack(1);
    }

    /**
     * Creates an item stack of the given size using the item this provider represents.
     *
     * @param size Size of the stack.
     */
    @Nonnull
    default ItemStack getItemStack(int size) {
        return new ItemStack(getItem(), size);
    }

    @Deprecated//TODO - 1.17: Remove this as we don't actually use this
    default boolean itemMatches(ItemStack otherStack) {
        return itemMatches(otherStack.getItem());
    }

    @Deprecated//TODO - 1.17: Remove this as we don't actually use this
    default boolean itemMatches(Item other) {
        return getItem() == other;
    }

    @Override
    default ResourceLocation getRegistryName() {
        return getItem().getRegistryName();
    }

    @Override
    default String getTranslationKey() {
        return getItem().getDescriptionId();
    }
}
