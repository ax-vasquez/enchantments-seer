package com.nuggylib.enchantmentsseer.common.block.transmitter;

import com.nuggylib.enchantmentsseer.common.block.BlockEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.block.states.TransmitterType.Size;
import com.nuggylib.enchantmentsseer.common.content.network.transmitter.Transmitter;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.ConnectionType;
import com.nuggylib.enchantmentsseer.common.tile.transmitter.TileEntityTransmitter;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;
import com.nuggylib.enchantmentsseer.common.util.VoxelShapeUtils;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.IWorldReader;
import net.minecraft.world.World;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/transmitter/BlockTransmitter.java"
 */
public abstract class BlockTransmitter extends BlockEnchantmentsSeer {

    private static final Map<ConnectionInfo, VoxelShape> cachedShapes = new HashMap<>();

    protected BlockTransmitter() {
        super(AbstractBlock.Properties.of(Material.PISTON).strength(1, 6));
    }

    @Nonnull
    @Override
    @Deprecated
    public ActionResultType use(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, PlayerEntity player, @Nonnull Hand hand,
                                @Nonnull BlockRayTraceResult hit) {
        ItemStack stack = player.getItemInHand(hand);
        if (!stack.isEmpty() && player.isShiftKeyDown()) {
            if (!world.isClientSide) {
                WorldUtils.dismantleBlock(state, world, pos);
            }
            return ActionResultType.SUCCESS;
        }
        return ActionResultType.PASS;
    }

    @Override
    public void setPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        TileEntityTransmitter tile = WorldUtils.getTileEntity(TileEntityTransmitter.class, world, pos);
        if (tile != null) {
            tile.onAdded();
        }
    }

    @Override
    @Deprecated
    public void neighborChanged(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Block neighborBlock, @Nonnull BlockPos neighborPos,
                                boolean isMoving) {
        // TODO: See about removing this altogether
    }

    @Override
    public void onNeighborChange(BlockState state, IWorldReader world, BlockPos pos, BlockPos neighbor) {
        // TODO: See about removing this altogether
    }

    @Override
    @Deprecated
    public boolean isPathfindable(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull PathType type) {
        return false;
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, ISelectionContext context) {
        //Get the partial selection box if we are holding a configurator
        if (context.getEntity() == null) {
            //If we don't have an entity get the full VoxelShape
            return getRealShape(world, pos);
        }
        TileEntityTransmitter tile = WorldUtils.getTileEntity(TileEntityTransmitter.class, world, pos);
        if (tile == null) {
            //If we failed to get the tile, just give the center shape
            return getCenter();
        }
        //If we failed to figure it out somehow, just fall back to the center. This should never happen
        return getCenter();
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getOcclusionShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos) {
        //Override this so that we ALWAYS have the full collision box, even if a configurator is being held
        return getRealShape(world, pos);
    }

    @Nonnull
    @Override
    @Deprecated
    public VoxelShape getCollisionShape(@Nonnull BlockState state, @Nonnull IBlockReader world, @Nonnull BlockPos pos, @Nonnull ISelectionContext context) {
        //Override this so that we ALWAYS have the full collision box, even if a configurator is being held
        return getRealShape(world, pos);
    }

    protected abstract VoxelShape getCenter();

    protected abstract VoxelShape getSide(ConnectionType type, Direction side);

    private VoxelShape getRealShape(IBlockReader world, BlockPos pos) {
        TileEntityTransmitter tile = WorldUtils.getTileEntity(TileEntityTransmitter.class, world, pos);
        if (tile == null) {
            //If we failed to get the tile, just give the center shape
            return getCenter();
        }
        Transmitter<?, ?, ?> transmitter = tile.getTransmitter();
        ConnectionType[] connectionTypes = new ConnectionType[transmitter.getConnectionTypesRaw().length];
        for (int i = 0; i < EnumUtils.DIRECTIONS.length; i++) {
            //Get the actual connection types
            connectionTypes[i] = transmitter.getConnectionType(EnumUtils.DIRECTIONS[i]);
        }
        ConnectionInfo info = new ConnectionInfo(tile.getTransmitterType().getSize(), connectionTypes);
        if (cachedShapes.containsKey(info)) {
            return cachedShapes.get(info);
        }
        //If we don't have a cached version of our shape, then we need to calculate it
        List<VoxelShape> shapes = new ArrayList<>();
        for (Direction side : EnumUtils.DIRECTIONS) {
            ConnectionType connectionType = connectionTypes[side.ordinal()];
            if (connectionType != ConnectionType.NONE) {
                shapes.add(getSide(connectionType, side));
            }
        }
        VoxelShape center = getCenter();
        if (shapes.isEmpty()) {
            cachedShapes.put(info, center);
            return center;
        }
        shapes.add(center);
        VoxelShape shape = VoxelShapeUtils.combine(shapes);
        cachedShapes.put(info, shape);
        return shape;
    }

    private static class ConnectionInfo {

        private final Size size;
        private final ConnectionType[] connectionTypes;

        private ConnectionInfo(Size size, ConnectionType[] connectionTypes) {
            this.size = size;
            this.connectionTypes = connectionTypes;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o instanceof ConnectionInfo) {
                ConnectionInfo other = (ConnectionInfo) o;
                return size == other.size && Arrays.equals(connectionTypes, other.connectionTypes);
            }
            return false;
        }

        @Override
        public int hashCode() {
            int result = Objects.hash(size);
            result = 31 * result + Arrays.hashCode(connectionTypes);
            return result;
        }
    }
}
