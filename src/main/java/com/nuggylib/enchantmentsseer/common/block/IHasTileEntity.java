package com.nuggylib.enchantmentsseer.common.block;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * An interface to use for blocks that should also have a corresponding Tile entity.
 *
 * @param <TILE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/interfaces/IHasTileEntity.java"
 */
public interface IHasTileEntity<TILE extends TileEntity> {

    TileEntityType<? extends TILE> getTileType();
}
