package com.nuggylib.enchantmentsseer.common.util;

import com.google.common.collect.Lists;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.registry.Registry;

import javax.annotation.Nullable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A simple class to copy (and clarify) the logic from the vanilla {@link EnchantmentHelper}
 */
public class SeersEnchantmentHelper {

    /**
     * Get all available enchantments for the given enchant item and reagent item.
     *
     * Reagent logic:
     * <ol>
     *     <li><b>Seer's Manuscript:</b> All possible enchantment results are returned</li>
     *     <li><b>Arbitrary enchanted book:</b> Only the enchantment(s) on the given book, and only if it is available to the given enchant item</li>
     *     <li><b>Seer's Stone:</b> No enchantments returned - only used when recharging an enchanted item</li>
     * </ol>
     *
     * @param enchantItemStack          The {@link ItemStack} for the item to potentially enchant
     * @param reagentItemStack          The {@link ItemStack} for the item to use as the reagent when enchanting    // TODO: Limit results based on the reagent and disallow nullable once done
     * @param simulate                  TODO: Figure out what this is for
     * @return
     */
    public static List<Enchantment> getAvailableEnchantmentResults(ItemStack enchantItemStack, @Nullable ItemStack reagentItemStack, boolean simulate) {
        List<Enchantment> availableEnchantments = Lists.newArrayList();
        boolean isBookItem = enchantItemStack.getItem() == Items.BOOK;

        for(Enchantment enchantment : Registry.ENCHANTMENT) {
            if ((!enchantment.isTreasureOnly() || simulate) && enchantment.isDiscoverable() && (enchantment.canApplyAtEnchantingTable(enchantItemStack) || (isBookItem && enchantment.isAllowedOnBooks()))) {
                availableEnchantments.add(enchantment);
            }
        }

        return availableEnchantments;
    }

}
