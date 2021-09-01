package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.item.ItemSeersEnchantedPage;
import com.nuggylib.enchantmentsseer.common.item.ItemSeersManuscript;
import com.nuggylib.enchantmentsseer.common.item.ItemSeersStone;
import com.nuggylib.enchantmentsseer.common.registration.impl.ItemDeferredRegister;
import com.nuggylib.enchantmentsseer.common.registration.impl.ItemRegistryObject;
import net.minecraft.item.Item;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registries/MekanismItems.java"
 */
public class EnchantmentsSeerItems {

    private EnchantmentsSeerItems() {}

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(EnchantmentsSeer.MOD_ID);

    public static final ItemRegistryObject<Item> SEERS_STONE = ITEMS.register("seers_stone", ItemSeersStone::new);
    public static final ItemRegistryObject<Item> SEERS_MANUSCRIPT = ITEMS.register("seers_manuscript", ItemSeersManuscript::new);
    public static final ItemRegistryObject<Item> SEERS_ENCHANTED_PAGE = ITEMS.register("seers_enchanted_page", ItemSeersEnchantedPage::new);

}
