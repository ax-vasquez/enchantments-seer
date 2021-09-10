package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.block.SeersEnchantingTableBlock;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentsSeerBlocks {

    private EnchantmentsSeerBlocks() {
    }

    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, EnchantmentsSeer.MOD_ID);

    // Blocks
    public static final RegistryObject<Block> SEERS_ENCHANTING_TABLE = BLOCKS.register("seers_enchanting_table", () -> new SeersEnchantingTableBlock(AbstractBlock.Properties.of(Material.STONE, MaterialColor.COLOR_BLACK)));

}
