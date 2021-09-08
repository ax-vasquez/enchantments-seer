package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.EnchantmentsSeerTileContainer;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.SeersEnchantingTableContainer;
import com.nuggylib.enchantmentsseer.common.registration.impl.ContainerTypeDeferredRegister;
import com.nuggylib.enchantmentsseer.common.registration.impl.ContainerTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;

/**
 * Inspired by Mekanism code
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registries/MekanismContainerTypes.java"
 */
public class EnchantmentsSeerContainerTypes {

    private EnchantmentsSeerContainerTypes() {

    }

    public static final ContainerTypeDeferredRegister CONTAINER_TYPES = new ContainerTypeDeferredRegister(EnchantmentsSeer.MOD_ID);

    public static final ContainerTypeRegistryObject<SeersEnchantingTableContainer> SEERS_ENCHANTING_TABLE = CONTAINER_TYPES.register(EnchantmentsSeerBlocks.SEERS_ENCHANTING_TABLE, SeersEnchantingTableContainer::new);

}
