package com.nuggylib.enchantmentsseer.common.util;

import com.nuggylib.enchantmentsseer.api.RelativeSide;
import net.minecraft.util.Direction;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/util/EnumUtils.java"
 */
public class EnumUtils {

    private EnumUtils() {
    }

    /**
     * Cached value of {@link Direction#values()}. DO NOT MODIFY THIS LIST.
     */
    public static final Direction[] DIRECTIONS = Direction.values();

    /**
     * Cached value of the horizontal directions. DO NOT MODIFY THIS LIST.
     *
     * @implNote Index is ordinal() - 2, as the first two elements of {@link Direction} are {@link Direction#DOWN} and {@link Direction#UP}
     */
    public static final Direction[] HORIZONTAL_DIRECTIONS = new Direction[]{Direction.NORTH, Direction.SOUTH, Direction.WEST, Direction.EAST};

    /**
     * Cached value of {@link RelativeSide#values()}. DO NOT MODIFY THIS LIST.
     */
    public static final RelativeSide[] SIDES = RelativeSide.values();
}
