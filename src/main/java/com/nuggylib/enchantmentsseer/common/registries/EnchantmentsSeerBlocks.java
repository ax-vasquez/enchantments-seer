package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.block.prefab.BlockTile;
import com.nuggylib.enchantmentsseer.common.content.BlockTypeTile;
import com.nuggylib.enchantmentsseer.common.item.block.ItemBlockEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.item.block.ItemBlockSeersEnchantingTable;
import com.nuggylib.enchantmentsseer.common.registration.impl.BlockDeferredRegister;
import com.nuggylib.enchantmentsseer.common.registration.impl.BlockRegistryObject;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;

/**
 * Block registy class
 *
 * Based on the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registries/MekanismBlocks.java"
 */
public class EnchantmentsSeerBlocks {

    private EnchantmentsSeerBlocks() {
    }

    public static final BlockDeferredRegister BLOCKS = new BlockDeferredRegister(EnchantmentsSeer.MOD_ID);

    // Blocks
    public static final BlockRegistryObject<BlockTile<TileEntitySeersEnchantmentTable, BlockTypeTile<TileEntitySeersEnchantmentTable>>, ItemBlockEnchantmentsSeer> SEERS_ENCHANTING_TABLE = BLOCKS.register("seers_enchanting_table", () -> new BlockTile<>(EnchantmentsSeerBlockTypes.SEERS_ENCHANTING_TABLE), ItemBlockSeersEnchantingTable::new);

}
