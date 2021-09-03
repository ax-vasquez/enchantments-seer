package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.registration.impl.TileEntityTypeDeferredRegister;
import com.nuggylib.enchantmentsseer.common.registration.impl.TileEntityTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registries/MekanismTileEntityTypes.java"
 */
public class EnchantmentsSeerTileEntityTypes {

    private EnchantmentsSeerTileEntityTypes() {

    }

    public static final TileEntityTypeDeferredRegister TILE_ENTITY_TYPES = new TileEntityTypeDeferredRegister(EnchantmentsSeer.MOD_ID);

    public static final TileEntityTypeRegistryObject<TileEntitySeersEnchantmentTable> SEERS_ENCHANTING_TABLE = TILE_ENTITY_TYPES.register(EnchantmentsSeerBlocks.SEERS_ENCHANTING_TABLE, TileEntitySeersEnchantmentTable::new);

}
