package com.nuggylib.enchantmentsseer.common.tile.transmitter;

import com.nuggylib.enchantmentsseer.api.providers.IBlockProvider;
import com.nuggylib.enchantmentsseer.client.model.data.TransmitterModelData;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import com.nuggylib.enchantmentsseer.common.block.IHasTileEntity;
import com.nuggylib.enchantmentsseer.common.block.states.TransmitterType;
import com.nuggylib.enchantmentsseer.common.block.states.TransmitterType.Size;
import com.nuggylib.enchantmentsseer.common.block.transmitter.BlockLargeTransmitter;
import com.nuggylib.enchantmentsseer.common.block.transmitter.BlockSmallTransmitter;
import com.nuggylib.enchantmentsseer.common.capabilities.DynamicHandler.InteractPredicate;
import com.nuggylib.enchantmentsseer.common.content.network.transmitter.Transmitter;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.ConnectionType;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.TransmitterNetworkRegistry;
import com.nuggylib.enchantmentsseer.common.tile.base.CapabilityTileEntity;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelDataMap;
import net.minecraftforge.client.model.data.ModelProperty;
import net.minecraftforge.forgespi.language.IConfigurable;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/transmitter/TileEntityTransmitter.java"
 */
public abstract class TileEntityTransmitter extends CapabilityTileEntity implements IConfigurable, ITickableTileEntity {

    public static final ModelProperty<TransmitterModelData> TRANSMITTER_PROPERTY = new ModelProperty<>();

    private final Transmitter<?, ?, ?> transmitter;
    private boolean forceUpdate = true;
    private boolean loaded = false;

    public TileEntityTransmitter(IBlockProvider blockProvider) {
        super(((IHasTileEntity<? extends TileEntityTransmitter>) blockProvider.getBlock()).getTileType());
        this.transmitter = createTransmitter(blockProvider);
        cacheCoord();
    }

    protected abstract Transmitter<?, ?, ?> createTransmitter(IBlockProvider blockProvider);

    public Transmitter<?, ?, ?> getTransmitter() {
        return transmitter;
    }

    public void setForceUpdate() {
        forceUpdate = true;
    }

    public abstract TransmitterType getTransmitterType();

    @Override
    public void tick() {
        if (!isRemote() && forceUpdate) {
            forceUpdate = false;
        }
    }

    @Nonnull
    @Override
    public CompoundNBT getReducedUpdateTag() {
        return getTransmitter().getReducedUpdateTag(super.getReducedUpdateTag());
    }

    @Override
    public void handleUpdateTag(BlockState state, @Nonnull CompoundNBT tag) {
        super.handleUpdateTag(state, tag);
        getTransmitter().handleUpdateTag(tag);
    }

    @Override
    public void handleUpdatePacket(@Nonnull CompoundNBT tag) {
        super.handleUpdatePacket(tag);
        //Delay requesting the model data update and actually updating the packet until we have finished parsing the update tag
        requestModelDataUpdate();
        WorldUtils.updateBlock(getLevel(), getBlockPos(), getBlockState());
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbtTags) {
        super.load(state, nbtTags);
        getTransmitter().read(nbtTags);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbtTags) {
        return getTransmitter().write(super.save(nbtTags));
    }

    @Override
    public void clearRemoved() {
        super.clearRemoved();
        onWorldJoin();
    }

    @Override
    public void onChunkUnloaded() {
        if (!isRemote()) {
            getTransmitter().takeShare();
        }
        onWorldSeparate();
        getTransmitter().onChunkUnload();
        super.onChunkUnloaded();
    }

    @Override
    public void setRemoved() {
        super.setRemoved();
        onWorldSeparate();
        getTransmitter().remove();
    }

    public void onAdded() {
        onWorldJoin();
    }

    private void onWorldJoin() {
        loaded = true;
        if (!isRemote()) {
            TransmitterNetworkRegistry.registerOrphanTransmitter(getTransmitter());
        }
    }

    private void onWorldSeparate() {
        loaded = false;
        if (isRemote()) {
            getTransmitter().setTransmitterNetwork(null);
        } else {
            TransmitterNetworkRegistry.invalidateTransmitter(getTransmitter());
        }
    }

    public boolean isLoaded() {
        return loaded;
    }

    protected ActionResultType onConfigure(PlayerEntity player, Direction side) {
        //TODO: Move some of this stuff back into the tiles?
        return getTransmitter().onConfigure(player, side);
    }

    public List<VoxelShape> getCollisionBoxes() {
        List<VoxelShape> list = new ArrayList<>();
        boolean isSmall = getTransmitterType().getSize() == Size.SMALL;
        for (Direction side : EnumUtils.DIRECTIONS) {
            ConnectionType connectionType = getTransmitter().getConnectionType(side);
            if (connectionType != ConnectionType.NONE) {
                if (isSmall) {
                    list.add(BlockSmallTransmitter.getSideForType(connectionType, side));
                } else {
                    list.add(BlockLargeTransmitter.getSideForType(connectionType, side));
                }
            }
        }
        //Center position
        list.add(isSmall ? BlockSmallTransmitter.center : BlockLargeTransmitter.center);
        return list;
    }

    @Nonnull
    @Override
    public AxisAlignedBB getRenderBoundingBox() {
        //If any of the block is in view, then allow rendering the contents
        return new AxisAlignedBB(worldPosition, worldPosition.offset(1, 1, 1));
    }

    @Nonnull
    @Override
    public IModelData getModelData() {
        TransmitterModelData data = initModelData();
        updateModelData(data);
        return new ModelDataMap.Builder().withInitial(TRANSMITTER_PROPERTY, data).build();
    }

    protected void updateModelData(TransmitterModelData modelData) {
        //Update the data, using information about if there is actually a connection on a given side
        for (Direction side : EnumUtils.DIRECTIONS) {
            modelData.setConnectionData(side, getTransmitter().getConnectionType(side));
        }
    }

    @Nonnull
    protected TransmitterModelData initModelData() {
        return new TransmitterModelData();
    }

    public void sideChanged(@Nonnull Direction side, @Nonnull ConnectionType old, @Nonnull ConnectionType type) {
    }

    protected InteractPredicate getExtractPredicate() {
        return (tank, side) -> {
            if (side == null) {
                //Note: We return true here, but extraction isn't actually allowed and gets blocked by the read only handler
                return true;
            }
            //If we have a side only allow extracting if our connection allows it
            ConnectionType connectionType = getTransmitter().getConnectionType(side);
            return connectionType == ConnectionType.NORMAL || connectionType == ConnectionType.PUSH;
        };
    }

    protected InteractPredicate getInsertPredicate() {
        return (tank, side) -> {
            if (side == null) {
                //Note: We return true here, but insertion isn't actually allowed and gets blocked by the read only handler
                return true;
            }
            //If we have a side only allow inserting if our connection allows it
            ConnectionType connectionType = getTransmitter().getConnectionType(side);
            return connectionType == ConnectionType.NORMAL || connectionType == ConnectionType.PULL;
        };
    }
}
