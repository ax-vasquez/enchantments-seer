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
import java.util.Random;

/**
 * Functions similarly to the {@link EnchantmentHelper}, except that it provides "replacement" methods to perform
 * similar logic without anything related to use of random value.
 */
public class EnchantmentSeerEnchantmentHelper {

    /**
     *
     * @param powerBonus
     * @param itemToEnchant
     * @return
     */
    public static int getEnchantmentCost(int powerBonus, ItemStack itemToEnchant) {
        Item item = itemToEnchant.getItem();
        int i = itemToEnchant.getItemEnchantability();
        if (i <= 0) {
            return 0;
        } else {
            // TODO: In the future, let's make it so that you also need the appropriate "power level" to unlock all levels (power level is the number of bookshelves around the table, basically)
            if (powerBonus > 15) {
                powerBonus = 15;
            }

            // TODO: FIX THIS!!!! This return value is just an arbitrary value to clear errors while developing
            return powerBonus * 2;
        }
    }

    public static List<EnchantmentData> getAvailableEnchantmentResults(int cost, ItemStack itemToEnchant) {
        List<EnchantmentData> list = Lists.newArrayList();
        Item item = itemToEnchant.getItem();
        boolean flag = itemToEnchant.getItem() == Items.BOOK;

        for(Enchantment enchantment : Registry.ENCHANTMENT) {
            if (enchantment.isDiscoverable() && (enchantment.canApplyAtEnchantingTable(itemToEnchant) || (flag && enchantment.isAllowedOnBooks()))) {
                for(int i = enchantment.getMaxLevel(); i > enchantment.getMinLevel() - 1; --i) {
                    if (cost >= enchantment.getMinCost(i) && cost <= enchantment.getMaxCost(i)) {
                        list.add(new EnchantmentData(enchantment, i));
                        break;
                    }
                }
            }
        }

        return list;
    }

}
