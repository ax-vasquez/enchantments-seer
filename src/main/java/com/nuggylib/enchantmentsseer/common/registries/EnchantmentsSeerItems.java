package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;

import com.nuggylib.enchantmentsseer.common.item.ItemSeersEnchantedPage;
import com.nuggylib.enchantmentsseer.common.item.ItemSeersManuscript;
import com.nuggylib.enchantmentsseer.common.item.ItemSeersStone;
import com.nuggylib.enchantmentsseer.common.item.ItemSeersStoneU;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registries/MekanismItems.java"
 */
public class EnchantmentsSeerItems {

    private EnchantmentsSeerItems() {}

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EnchantmentsSeer.MOD_ID);

    // Item
    public static final RegistryObject<Item> SEERS_STONE = ITEMS.register("seers_stone", () -> new ItemSeersStone(new Item.Properties().tab(EnchantmentsSeer.tabEnchantmentsSeer)));
    public static final RegistryObject<Item> SEERS_STONE_UNCHARGED = ITEMS.register("seers_stone_uncharged", () -> new ItemSeersStoneU(new Item.Properties().tab(EnchantmentsSeer.tabEnchantmentsSeer)));
    public static final RegistryObject<Item> SEERS_MANUSCRIPT = ITEMS.register("seers_manuscript", () -> new ItemSeersManuscript(new Item.Properties().tab(EnchantmentsSeer.tabEnchantmentsSeer)));
    public static final RegistryObject<Item> SEERS_ENCHANTED_PAGE = ITEMS.register("seers_enchanted_page", () -> new ItemSeersEnchantedPage(new Item.Properties().tab(EnchantmentsSeer.tabEnchantmentsSeer)));

    // BlockItem
    public static final RegistryObject<Item> SEERS_ENCHANTING_TABLE = ITEMS.register("seers_enchanting_table", () -> new BlockItem(EnchantmentsSeerBlocks.SEERS_ENCHANTING_TABLE.get(), new Item.Properties().tab(EnchantmentsSeer.tabEnchantmentsSeer)));
}
