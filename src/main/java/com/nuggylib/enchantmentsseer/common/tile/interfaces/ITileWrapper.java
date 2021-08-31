package com.nuggylib.enchantmentsseer.common.tile.interfaces;

import com.nuggylib.enchantmentsseer.api.Coord4D;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/interfaces/ITileWrapper.java"
 */
public interface ITileWrapper {

    BlockPos getTilePos();

    World getTileWorld();

    default Coord4D getTileCoord() {
        return new Coord4D(getTilePos(), getTileWorld());
    }
}
