package com.nuggylib.enchantmentsseer.api;

import com.nuggylib.enchantmentsseer.client.render.text.APILang;
import com.nuggylib.enchantmentsseer.client.render.text.IHasTranslationKey;
import com.nuggylib.enchantmentsseer.client.render.text.ILangEntry;
import com.nuggylib.enchantmentsseer.api.math.MathUtils;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/RelativeSide.java"
 */
public enum RelativeSide implements IHasTranslationKey {
    FRONT(APILang.FRONT),
    LEFT(APILang.LEFT),
    RIGHT(APILang.RIGHT),
    BACK(APILang.BACK),
    TOP(APILang.TOP),
    BOTTOM(APILang.BOTTOM);

    private static final RelativeSide[] SIDES = values();

    /**
     * Gets a side by index.
     *
     * @param index Index of the side.
     */
    public static RelativeSide byIndex(int index) {
        return MathUtils.getByIndexMod(SIDES, index);
    }

    private final ILangEntry langEntry;

    RelativeSide(ILangEntry langEntry) {
        this.langEntry = langEntry;
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }

    /**
     * Gets the {@link Direction} from the block based on what side it is facing.
     *
     * @param facing The direction the block is facing.
     *
     * @return The direction representing which side of the block this RelativeSide is actually representing based on the direction it is facing.
     */
    public Direction getDirection(@Nonnull Direction facing) {
        if (this == FRONT) {
            return facing;
        } else if (this == BACK) {
            return facing.getOpposite();
        } else if (this == LEFT) {
            if (facing == Direction.DOWN || facing == Direction.UP) {
                return Direction.EAST;
            }
            return facing.getClockWise();
        } else if (this == RIGHT) {
            if (facing == Direction.DOWN || facing == Direction.UP) {
                return Direction.WEST;
            }
            return facing.getCounterClockWise();
        } else if (this == TOP) {
            if (facing == Direction.DOWN) {
                return Direction.NORTH;
            } else if (facing == Direction.UP) {
                return Direction.SOUTH;
            }
            return Direction.UP;
        } else if (this == BOTTOM) {
            if (facing == Direction.DOWN) {
                return Direction.SOUTH;
            } else if (facing == Direction.UP) {
                return Direction.NORTH;
            }
            return Direction.DOWN;
        }
        //Fallback to north though we should never get here
        return Direction.NORTH;
    }

    /**
     * Gets the {@link RelativeSide} based on a side, and the facing direction of a block.
     *
     * @param facing The direction the block is facing.
     * @param side   The side of the block we want to know what {@link RelativeSide} it is.
     *
     * @return the {@link RelativeSide} based on a side, and the facing direction of a block.
     *
     * @apiNote The calculations for what side is what when facing upwards or downwards, is done as if it was facing NORTH and rotated around the X-axis
     */
    public static RelativeSide fromDirections(@Nonnull Direction facing, @Nonnull Direction side) {
        if (side == facing) {
            return FRONT;
        } else if (side == facing.getOpposite()) {
            return BACK;
        } else if (facing == Direction.DOWN || facing == Direction.UP) {
            if (side == Direction.NORTH) {
                return facing == Direction.DOWN ? TOP : BOTTOM;
            } else if (side == Direction.SOUTH) {
                return facing == Direction.DOWN ? BOTTOM : TOP;
            } else if (side == Direction.WEST) {
                return RIGHT;
            } else if (side == Direction.EAST) {
                return LEFT;
            }
        } else if (side == Direction.DOWN) {
            return BOTTOM;
        } else if (side == Direction.UP) {
            return TOP;
        } else if (side == facing.getCounterClockWise()) {
            return RIGHT;
        } else if (side == facing.getClockWise()) {
            return LEFT;
        }
        //Fall back to front, should never get here
        return FRONT;
    }
}
