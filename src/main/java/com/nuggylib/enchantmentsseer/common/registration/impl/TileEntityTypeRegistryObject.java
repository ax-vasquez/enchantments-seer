package com.nuggylib.enchantmentsseer.common.registration.impl;

import com.nuggylib.enchantmentsseer.common.registration.WrappedRegistryObject;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <TILE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/impl/TileEntityTypeRegistryObject.java"
 */
public class TileEntityTypeRegistryObject<TILE extends TileEntity> extends WrappedRegistryObject<TileEntityType<TILE>> {

    public TileEntityTypeRegistryObject(RegistryObject<TileEntityType<TILE>> registryObject) {
        super(registryObject);
    }

    @Nonnull
    public TileEntityType<TILE> getTileEntityType() {
        return get();
    }
}
