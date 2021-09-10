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
package com.nuggylib.enchantmentsseer.common;

import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerBlocks;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerContainerTypes;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerItems;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerTileEntityTypes;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
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
    public static final String MOD_NAME = "Enchantments Seer";
    public static final String LOG_TAG = '[' + MOD_NAME + ']';
    public static final Logger logger = LogManager.getLogger();
    /**
     * EnchantmentsSeer mod instance
     */
    public static EnchantmentsSeer instance;

    public static CreativeTabEnchantmentsSeer tabEnchantmentsSeer = new CreativeTabEnchantmentsSeer();

    public EnchantmentsSeer() {
        instance = this;
        EnchantmentsSeerItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentsSeerBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentsSeerTileEntityTypes.TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentsSeerContainerTypes.CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
    }

}
