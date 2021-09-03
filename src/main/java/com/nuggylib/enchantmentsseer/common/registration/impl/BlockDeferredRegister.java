package com.nuggylib.enchantmentsseer.common.registration.impl;

import com.nuggylib.enchantmentsseer.api.providers.IBlockProvider;
import com.nuggylib.enchantmentsseer.common.registration.DoubleDeferredRegister;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase
 *
 * Block deferred register
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/impl/BlockDeferredRegister.java"
 */
public class BlockDeferredRegister extends DoubleDeferredRegister<Block, Item> {

    private final List<IBlockProvider> allBlocks = new ArrayList<>();

    public BlockDeferredRegister(String modid) {
        super(modid, ForgeRegistries.BLOCKS, ForgeRegistries.ITEMS);
    }

    public <BLOCK extends Block> BlockRegistryObject<BLOCK, BlockItem> register(String name, Supplier<? extends BLOCK> blockSupplier) {
        return registerDefaultProperties(name, blockSupplier, BlockItem::new);
    }

    public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> registerDefaultProperties(String name, Supplier<? extends BLOCK> blockSupplier,
                                                                                                                    BiFunction<BLOCK, Item.Properties, ITEM> itemCreator) {
        return register(name, blockSupplier, block -> itemCreator.apply(block, ItemDeferredRegister.getBaseProperties()));
    }

    public <BLOCK extends Block, ITEM extends BlockItem> BlockRegistryObject<BLOCK, ITEM> register(String name, Supplier<? extends BLOCK> blockSupplier,
                                                                                                   Function<BLOCK, ITEM> itemCreator) {
        BlockRegistryObject<BLOCK, ITEM> registeredBlock = register(name, blockSupplier, itemCreator, BlockRegistryObject::new);
        allBlocks.add(registeredBlock);
        return registeredBlock;
    }

    public List<IBlockProvider> getAllBlocks() {
        return allBlocks;
    }
}
