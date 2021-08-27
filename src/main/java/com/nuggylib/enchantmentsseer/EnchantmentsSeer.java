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

import com.nuggylib.enchantmentsseer.client.gui.screen.SeersEnchantmentScreen;
import com.nuggylib.enchantmentsseer.item.SeersEnchantedPageItem;
import com.nuggylib.enchantmentsseer.client.render.tileentity.SeersEnchantmentTableTileEntityRenderer;
import com.nuggylib.enchantmentsseer.item.SeersManuscriptItem;
import com.nuggylib.enchantmentsseer.block.SeersEnchantingTableBlock;
import com.nuggylib.enchantmentsseer.inventory.container.SeersEnchantingTableContainer;
import com.nuggylib.enchantmentsseer.tileentity.SeersEnchantingTableTileEntity;
import com.nuggylib.enchantmentsseer.util.ResourceType;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
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
    public static final Logger LOGGER = LogManager.getLogger();

    private static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MOD_ID);
    private static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MOD_ID);
    private static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, MOD_ID);
    private static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, MOD_ID);

    // Blocks
    public static final RegistryObject<Block> SEERS_ENCHANTING_TABLE_BLOCK = BLOCKS.register("seers_enchanting_table", () -> new SeersEnchantingTableBlock(Block.Properties.of(Material.STONE)));

    // Containers
    public static final RegistryObject<ContainerType<SeersEnchantingTableContainer>> SEERS_ENCHANTING_TABLE_CONTAINER_TYPE = CONTAINER_TYPES.register("seers_enchanting_table", () -> new ContainerType<>(SeersEnchantingTableContainer::new));

    // Items
    public static final RegistryObject<Item> SEERS_STONE = ITEMS.register("seers_stone", () -> new Item(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> SEERS_MANUSCRIPT = ITEMS.register("seers_manuscript", () -> new SeersManuscriptItem(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<Item> SEERS_ENCHANTED_PAGE = ITEMS.register("seers_enchanted_page", () -> new SeersEnchantedPageItem(new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));
    public static final RegistryObject<BlockItem> SEERS_ENCHANTING_TABLE_BLOCK_ITEM = ITEMS.register("seers_enchanting_table", () -> new BlockItem(SEERS_ENCHANTING_TABLE_BLOCK.get(), new Item.Properties().tab(ItemGroup.TAB_MATERIALS)));

    // TileEntityTypes
    public static final RegistryObject<TileEntityType<SeersEnchantingTableTileEntity>> SEERS_ENCHANTING_TABLE_TE_TYPE = TILE_ENTITY_TYPES.register("seers_enchanting_table", () -> TileEntityType.Builder.of(SeersEnchantingTableTileEntity::new, SEERS_ENCHANTING_TABLE_BLOCK.get()).build(null));

    public EnchantmentsSeer() {

        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);

        try {
            LOGGER.info("Registering items");
            ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
            LOGGER.info("Done registering items");
        } catch (Error e) {
            LOGGER.error(String.format("Error occurred while registering items: '%s'", e.getMessage()));
        }

        try {
            LOGGER.info("Registering blocks");
            BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
            LOGGER.info("Done registering blocks");
        } catch (Error e) {
            LOGGER.error(String.format("Error occurred while registering blocks: '%s'", e.getMessage()));
        }

        try {
            LOGGER.info("Registering tile entity types");
            TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
            LOGGER.info("Done tile entity types");
        } catch (Error e) {
            LOGGER.error(String.format("Error occurred while registering tile entity types: '%s'", e.getMessage()));
        }

        try {
            LOGGER.info("Registering container types");
            CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
            LOGGER.info("Done container types");
        } catch (Error e) {
            LOGGER.error(String.format("Error occurred while registering container types: '%s'", e.getMessage()));
        }


    }

    private void clientSetup(final FMLClientSetupEvent event)
    {
        // Registers the GUI for the seers enchanting table
        ScreenManager.register(SEERS_ENCHANTING_TABLE_CONTAINER_TYPE.get(), SeersEnchantmentScreen::new);
        // Registers the book animation for the seers enchanting table
        ClientRegistry.bindTileEntityRenderer(SEERS_ENCHANTING_TABLE_TE_TYPE.get(), SeersEnchantmentTableTileEntityRenderer::new);
    }

    /**
     * Gets a ResourceLocation with a defined resource type and name.
     *
     * @param type - type of resource to retrieve
     * @param name - simple name of file to retrieve as a ResourceLocation
     *
     * @return the corresponding ResourceLocation
     */
    public static ResourceLocation getResource(ResourceType type, String name) {
        return rl(type.getPrefix() + name);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}
