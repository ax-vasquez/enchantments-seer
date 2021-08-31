package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import com.nuggylib.enchantmentsseer.common.block.attribute.Attributes;
import com.nuggylib.enchantmentsseer.common.content.BlockTypeTile;
import com.nuggylib.enchantmentsseer.common.content.BlockTypeTile.BlockTileBuilder;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registries/MekanismBlockTypes.java"
 */
public class EnchantmentsSeerBlockTypes {

    private EnchantmentsSeerBlockTypes() {}

    public static final BlockTypeTile<TileEntitySeersEnchantmentTable> SEERS_ENCHANTING_TABLE = BlockTileBuilder
            .createBlock(() -> EnchantmentsSeerTileEntityTypes.SEERS_ENCHANTING_TABLE, EnchantmentsSeerLang.DESCRIPTION_SEERS_ENCHANTING_TABLE)
            .withGui(() -> EnchantmentsSeerContainerTypes.SEERS_ENCHANTING_TABLE)
            .with(Attributes.INVENTORY)
            .build();

}
