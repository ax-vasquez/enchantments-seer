package com.nuggylib.enchantmentsseer.common.block.transmitter;

import com.nuggylib.enchantmentsseer.common.lib.transmitter.ConnectionType;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;
import com.nuggylib.enchantmentsseer.common.util.VoxelShapeUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/transmitter/BlockSmallTransmitter.java"
 */
public abstract class BlockSmallTransmitter extends BlockTransmitter {

    private static final VoxelShape[] SIDES = new VoxelShape[EnumUtils.DIRECTIONS.length];
    private static final VoxelShape[] SIDES_PULL = new VoxelShape[EnumUtils.DIRECTIONS.length];
    private static final VoxelShape[] SIDES_PUSH = new VoxelShape[EnumUtils.DIRECTIONS.length];
    public static final VoxelShape center;

    static {
        VoxelShapeUtils.setShape(box(5, 0, 5, 11, 5, 11), SIDES, true);
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(5, 4, 5, 11, 5, 11),
                box(6, 2, 6, 10, 4, 10),
                box(4, 0, 4, 12, 2, 12)
        ), SIDES_PULL, true);
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(5, 3, 5, 11, 5, 11),
                box(6, 1, 6, 10, 3, 10),
                box(7, 0, 7, 9, 1, 9)
        ), SIDES_PUSH, true);
        center = box(5, 5, 5, 11, 11, 11);
    }

    public static VoxelShape getSideForType(ConnectionType type, Direction side) {
        if (type == ConnectionType.PUSH) {
            return SIDES_PUSH[side.ordinal()];
        } else if (type == ConnectionType.PULL) {
            return SIDES_PULL[side.ordinal()];
        } //else normal
        return SIDES[side.ordinal()];
    }

    @Override
    protected VoxelShape getCenter() {
        return center;
    }

    @Override
    protected VoxelShape getSide(ConnectionType type, Direction side) {
        return getSideForType(type, side);
    }
}
