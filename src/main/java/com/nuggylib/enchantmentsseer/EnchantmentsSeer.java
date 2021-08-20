/*
 *     A Minecraft mod that adds an alternative way to enchant items.
 *     Copyright (C) 2021 Armando Vasquez
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.nuggylib.enchantmentsseer;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The main class for the Enchantments Seer mod
 *
 * @see "https://mcforge.readthedocs.io/en/1.16.x/concepts/registries/#deferredregister"
 */
@Mod("enchantments-seer")
public class EnchantmentsSeer
{
    public static final String MOD_ID = "enchantments-seer";
    private static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);

    // Items
    public static final RegistryObject<Item> SEERS_STONE = ITEMS.register("seers_stone", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

    public EnchantmentsSeer() {
        try {
            LOGGER.info("Registering items");
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
            LOGGER.info("Done registering items");
        } catch (Error e) {
            LOGGER.error(String.format("Error occurred while registering items: '%s'", e.getMessage()));
        }

    }
}
