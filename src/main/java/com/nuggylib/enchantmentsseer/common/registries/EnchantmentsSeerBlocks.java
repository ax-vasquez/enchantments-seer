package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import net.minecraft.block.Block;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

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

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EnchantmentsSeer.MOD_ID);

    // Blocks
//    public static final BlockRegistryObject<BlockTile<TileEntitySeersEnchantmentTable, BlockTypeTile<TileEntitySeersEnchantmentTable>>, ItemBlockEnchantmentsSeer> SEERS_ENCHANTING_TABLE = BLOCKS.register("seers_enchanting_table", () -> new BlockTile<>(EnchantmentsSeerBlockTypes.SEERS_ENCHANTING_TABLE), ItemBlockSeersEnchantingTable::new);

}
