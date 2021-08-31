package com.nuggylib.enchantmentsseer.common.tile.interfaces;

import net.minecraft.util.Direction;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/interfaces/ITileDirectional.java"
 */
public interface ITileDirectional {

    default boolean isDirectional() {
        return true;
    }

    void setFacing(@Nonnull Direction direction);

    @Nonnull
    Direction getDirection();

    @Nonnull
    default Direction getOppositeDirection() {
        return getDirection().getOpposite();
    }
}
