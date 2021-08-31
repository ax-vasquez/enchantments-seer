package com.nuggylib.enchantmentsseer.common.registration.impl;

import com.nuggylib.enchantmentsseer.common.registration.WrappedDeferredRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/impl/TileEntityTypeDeferredRegister.java"
 */
public class TileEntityTypeDeferredRegister extends WrappedDeferredRegister<TileEntityType<?>> {

    public TileEntityTypeDeferredRegister(String modid) {
        super(modid, ForgeRegistries.TILE_ENTITIES);
    }

    @SuppressWarnings("ConstantConditions")
    public <TILE extends TileEntity> TileEntityTypeRegistryObject<TILE> register(BlockRegistryObject<?, ?> block, Supplier<? extends TILE> factory) {
        //Note: There is no data fixer type as forge does not currently have a way exposing data fixers to mods yet
        return register(block.getInternalRegistryName(), () -> TileEntityType.Builder.<TILE>of(factory, block.getBlock()).build(null),
                TileEntityTypeRegistryObject::new);
    }
}
