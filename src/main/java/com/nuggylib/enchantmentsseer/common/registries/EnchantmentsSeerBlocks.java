package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.block.basic.BlockResource;
import com.nuggylib.enchantmentsseer.common.block.prefab.BlockTile;
import com.nuggylib.enchantmentsseer.common.content.BlockType;
import com.nuggylib.enchantmentsseer.common.content.BlockTypeTile;
import com.nuggylib.enchantmentsseer.common.item.block.ItemBlockEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.item.block.ItemBlockResource;
import com.nuggylib.enchantmentsseer.common.item.block.ItemBlockSeersEnchantingTable;
import com.nuggylib.enchantmentsseer.common.registration.impl.BlockDeferredRegister;
import com.nuggylib.enchantmentsseer.common.registration.impl.BlockRegistryObject;
import com.nuggylib.enchantmentsseer.common.resource.BlockResourceInfo;
import com.nuggylib.enchantmentsseer.common.resource.PrimaryResource;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;

import java.util.LinkedHashMap;
import java.util.Map;

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

    public static final Map<PrimaryResource, BlockRegistryObject<?, ?>> PROCESSED_RESOURCE_BLOCKS = new LinkedHashMap<>();

    static {
        // resource blocks
        for (PrimaryResource resource : EnumUtils.PRIMARY_RESOURCES) {
            if (resource.getResourceBlockInfo() != null) {
                PROCESSED_RESOURCE_BLOCKS.put(resource, registerResourceBlock(resource.getResourceBlockInfo()));
            }
        }
    }

    // Blocks
    public static final BlockRegistryObject<BlockTile<TileEntitySeersEnchantmentTable, BlockTypeTile<TileEntitySeersEnchantmentTable>>, ItemBlockEnchantmentsSeer> SEERS_ENCHANTING_TABLE = BLOCKS.register("fuelwood_heater", () -> new BlockTile<>(EnchantmentsSeerBlockTypes.SEERS_ENCHANTING_TABLE), ItemBlockSeersEnchantingTable::new);

    private static BlockRegistryObject<BlockResource, ItemBlockResource> registerResourceBlock(BlockResourceInfo resource) {
        return BLOCKS.registerDefaultProperties("block_" + resource.getRegistrySuffix(), () -> new BlockResource(resource), (block, properties) -> {
            return new ItemBlockResource(block, properties);
        });
    }
}
