package com.nuggylib.enchantmentsseer.common.tile.base;

import com.nuggylib.enchantmentsseer.common.tile.interfaces.ITileWrapper;
import com.nuggylib.enchantmentsseer.api.Coord4D;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.play.server.SUpdateTileEntityPacket;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Objects;

/**
 * Shamelessly-copied from the Mekanism code base; all credit goes to them.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/base/TileEntityUpdateable.java"
 */
public abstract class TileEntityUpdateable extends TileEntity implements ITileWrapper {

    @Nullable
    private Coord4D cachedCoord;
    private boolean cacheCoord;

    public TileEntityUpdateable(TileEntityType<?> type) {
        super(type);
    }

    /**
     * Call this for tiles that we may call {@link #getTileCoord()} a fair amount on to cache the coord when position/world information changes.
     */
    protected void cacheCoord() {
        //Mark that we want to cache the coord and then update the coord if needed
        cacheCoord = true;
        updateCoord();
    }

    /**
     * Like getWorld(), but for when you _know_ world won't be null
     *
     * @return The world!
     */
    @Nonnull
    protected World getWorldNN() {
        return Objects.requireNonNull(getLevel(), "getWorldNN called before world set");
    }

    public boolean isRemote() {
        return getWorldNN().isClientSide();
    }

    @Nullable
    @Override
    public SUpdateTileEntityPacket getUpdatePacket() {
        return new SUpdateTileEntityPacket(getBlockPos(), 0, getUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, @Nonnull CompoundNBT tag) {
        //We don't want to do a full read from NBT so simply call the super's read method to let Forge do whatever
        // it wants, but don't treat this as if it was the full saved NBT data as not everything has to be synced to the client
        super.load(state, tag);
    }

    @Nonnull
    @Override
    public CompoundNBT getUpdateTag() {
        return getReducedUpdateTag();
    }

    /**
     * Similar to {@link #getUpdateTag()} but with reduced information for when we are doing our own syncing.
     */
    @Nonnull
    public CompoundNBT getReducedUpdateTag() {
        //Add the base update tag information
        return super.getUpdateTag();
    }

    @Override
    public World getTileWorld() {
        return level;
    }

    @Override
    public BlockPos getTilePos() {
        return worldPosition;
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbt) {
        super.load(state, nbt);
        updateCoord();
    }

    @Override
    public void setLevelAndPosition(@Nonnull World world, @Nonnull BlockPos pos) {
        super.setLevelAndPosition(world, pos);
        updateCoord();
    }

    @Override
    public void setPosition(@Nonnull BlockPos pos) {
        super.setPosition(pos);
        updateCoord();
    }

    private void updateCoord() {
        if (cacheCoord && level != null) {
            cachedCoord = new Coord4D(worldPosition, level);
        }
    }

    @Override
    public Coord4D getTileCoord() {
        return cacheCoord && cachedCoord != null ? cachedCoord : ITileWrapper.super.getTileCoord();
    }
}
