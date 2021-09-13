package com.nuggylib.enchantmentsseer.common.util;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

// TODO: Cleanup the methods in this class so they are more specific to our needs
/**
 * Inspired by Mekanism
 *
 * This class contains various helpers related to general world operations.
 */
public class WorldUtils {

    /**
     * Checks if a position is in bounds of the world, and is loaded
     *
     * @param world world
     * @param pos   position
     *
     * @return True if the position is loaded or the given world is of a superclass of IWorldReader that does not have a concept of being loaded.
     */
    @Contract("null, _ -> false")
    public static boolean isBlockLoaded(@Nullable IBlockReader world, @Nonnull BlockPos pos) {
        if (world == null || !World.isInWorldBounds(pos)) {
            return false;
        } else if (world instanceof IWorldReader) {
            //Note: We don't bother checking if it is a world and then isBlockPresent because
            // all that does is also validate the y value is in bounds, and we already check to make
            // sure the position is valid both in the y and xz directions
            return ((IWorldReader) world).hasChunkAt(pos);
        }
        return true;
    }

    /**
     * Gets a tile entity if the location is loaded
     *
     * @param world world
     * @param pos   position
     *
     * @return tile entity if found, null if either not found or not loaded
     */
    @Nullable
    @Contract("null, _ -> null")
    public static TileEntity getTileEntity(@Nullable IBlockReader world, @Nonnull BlockPos pos) {
        if (!isBlockLoaded(world, pos)) {
            //If the world is null or its a world reader and the block is not loaded, return null
            return null;
        }
        return world.getBlockEntity(pos);
    }

    /**
     * Gets a tile entity if the location is loaded
     *
     * @param clazz Class type of the TileEntity we expect to be in the position
     * @param world world
     * @param pos   position
     *
     * @return tile entity if found, null if either not found, not loaded, or of the wrong type
     */
    @Nullable
    @Contract("_, null, _ -> null")
    public static <T extends TileEntity> T getTileEntity(@Nonnull Class<T> clazz, @Nullable IBlockReader world, @Nonnull BlockPos pos) {
        return getTileEntity(clazz, world, pos, false);
    }

    /**
     * Gets a tile entity if the location is loaded
     *
     * @param clazz        Class type of the TileEntity we expect to be in the position
     * @param world        world
     * @param pos          position
     * @param logWrongType Whether or not an error should be logged if a tile of a different type is found at the position
     *
     * @return tile entity if found, null if either not found or not loaded, or of the wrong type
     */
    @Nullable
    @Contract("_, null, _, _ -> null")
    public static <T extends TileEntity> T getTileEntity(@Nonnull Class<T> clazz, @Nullable IBlockReader world, @Nonnull BlockPos pos, boolean logWrongType) {
        TileEntity tile = getTileEntity(world, pos);
        if (tile == null) {
            return null;
        }
        if (clazz.isInstance(tile)) {
            return clazz.cast(tile);
        } else if (logWrongType) {
            EnchantmentsSeer.logger.warn("Unexpected TileEntity class at {}, expected {}, but found: {}", pos, clazz, tile.getClass());
        }
        return null;
    }

}
