package com.nuggylib.enchantmentsseer.common.registration.impl;

import com.nuggylib.enchantmentsseer.api.providers.IBlockProvider;
import com.nuggylib.enchantmentsseer.common.registration.DoubleWrappedRegistryObject;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/impl/BlockRegistryObject.java"
 */
public class BlockRegistryObject<BLOCK extends Block, ITEM extends Item> extends DoubleWrappedRegistryObject<BLOCK, ITEM> implements IBlockProvider {

    public BlockRegistryObject(RegistryObject<BLOCK> blockRegistryObject, RegistryObject<ITEM> itemRegistryObject) {
        super(blockRegistryObject, itemRegistryObject);
    }

    @Nonnull
    @Override
    public BLOCK getBlock() {
        return getPrimary();
    }

    @Nonnull
    @Override
    public ITEM getItem() {
        return getSecondary();
    }
}
