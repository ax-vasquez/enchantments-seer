package com.nuggylib.enchantmentsseer.common.block.transmitter;

import com.nuggylib.enchantmentsseer.common.lib.transmitter.ConnectionType;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;
import com.nuggylib.enchantmentsseer.common.util.VoxelShapeUtils;
import net.minecraft.util.Direction;
import net.minecraft.util.math.shapes.VoxelShape;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/transmitter/BlockLargeTransmitter.java"
 */
public abstract class BlockLargeTransmitter extends BlockTransmitter {

    private static final VoxelShape[] SIDES = new VoxelShape[EnumUtils.DIRECTIONS.length];
    private static final VoxelShape[] SIDES_PULL = new VoxelShape[EnumUtils.DIRECTIONS.length];
    private static final VoxelShape[] SIDES_PUSH = new VoxelShape[EnumUtils.DIRECTIONS.length];
    public static final VoxelShape center;

    static {
        VoxelShapeUtils.setShape(box(4, 0, 4, 12, 4, 12), SIDES, true);
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(4, 3, 4, 12, 4, 12),
                box(5, 2, 5, 11, 3, 11),
                box(3, 0, 3, 13, 2, 13)
        ), SIDES_PULL, true);
        VoxelShapeUtils.setShape(VoxelShapeUtils.combine(
                box(4, 3, 4, 12, 4, 12),
                box(5, 1, 5, 11, 3, 11),
                box(6, 0, 6, 10, 1, 10)
        ), SIDES_PUSH, true);
        center = box(4, 4, 4, 12, 12, 12);
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
