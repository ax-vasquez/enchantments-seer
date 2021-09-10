package com.nuggylib.enchantmentsseer.common;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import java.util.List;

/**
 * Helper class to perform enchantment-related tasks.
 *
 * This class is loosely-based off of {@link EnchantmentHelper} and provides similar functionality with slightly-altered
 * behavior.
 */
public class EnchantmentsSeerEnchantHelper {

    /**
     * Get all enchantments for the given item stack
     *
     * This method is similar to {@link EnchantmentHelper#getAvailableEnchantmentResults(int, ItemStack, boolean)}, except
     * that we don't care about level cost. We simply want to get <b>all</b> possible enchantments for the given item.
     *
     * @param itemStack             The item to get enchantments for
     * @param simulate              TODO: we need to figure out what this is used for (maybe testing?)
     * @return                      The list of compatible enchantments for the given {@link ItemStack}
     */
    public static List<Enchantment> getAvailableEnchantmentResults(ItemStack itemStack, boolean simulate) {
        List<Enchantment> list = Lists.newArrayList();
        boolean isBookItem = itemStack.getItem() == Items.BOOK;

        // TODO: Add a way to "hook in" to other enchantment registries as well - as of now, this will only look for vanilla enchantments
        for(Enchantment enchantment : Registry.ENCHANTMENT) {
            if ((!enchantment.isTreasureOnly() || simulate) && enchantment.isDiscoverable() && (enchantment.canApplyAtEnchantingTable(itemStack) || (isBookItem && enchantment.isAllowedOnBooks()))) {
                for(int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                    list.add(enchantment);
                }
            }
        }

        return list;
    }

}
