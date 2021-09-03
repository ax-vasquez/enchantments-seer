package com.nuggylib.enchantmentsseer.common.content.network.transmitter;

import com.nuggylib.enchantmentsseer.api.Coord4D;
import com.nuggylib.enchantmentsseer.api.NBTConstants;
import com.nuggylib.enchantmentsseer.client.render.text.EnumColor;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.ConnectionType;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.TransmissionType;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.TransmitterNetworkRegistry;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.acceptor.AbstractAcceptorCache;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.acceptor.AcceptorCache;
import com.nuggylib.enchantmentsseer.common.tile.interfaces.ITileWrapper;
import com.nuggylib.enchantmentsseer.common.tile.transmitter.TileEntityTransmitter;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;
import com.nuggylib.enchantmentsseer.common.util.NBTUtils;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Util;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <ACCEPTOR>
 * @param <NETWORK>
 * @param <TRANSMITTER>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/content/network/transmitter/Transmitter.java"
 */
public abstract class Transmitter<ACCEPTOR, NETWORK extends DynamicNetwork<ACCEPTOR, NETWORK, TRANSMITTER>,
        TRANSMITTER extends Transmitter<ACCEPTOR, NETWORK, TRANSMITTER>> implements ITileWrapper {

    public static boolean connectionMapContainsSide(byte connections, Direction side) {
        return connectionMapContainsSide(connections, side.ordinal());
    }

    private static boolean connectionMapContainsSide(byte connections, int sideOrdinal) {
        byte tester = (byte) (1 << sideOrdinal);
        return (connections & tester) > 0;
    }

    private static byte setConnectionBit(byte connections, boolean toSet, Direction side) {
        return (byte) ((connections & ~(byte) (1 << side.ordinal())) | (byte) ((toSet ? 1 : 0) << side.ordinal()));
    }

    private static ConnectionType getConnectionType(Direction side, byte allConnections, byte transmitterConnections, ConnectionType[] types) {
        int sideOrdinal = side.ordinal();
        if (!connectionMapContainsSide(allConnections, sideOrdinal)) {
            return ConnectionType.NONE;
        } else if (connectionMapContainsSide(transmitterConnections, sideOrdinal)) {
            return ConnectionType.NORMAL;
        }
        return types[sideOrdinal];
    }

    private ConnectionType[] connectionTypes = {ConnectionType.NORMAL, ConnectionType.NORMAL, ConnectionType.NORMAL, ConnectionType.NORMAL, ConnectionType.NORMAL,
            ConnectionType.NORMAL};
    private final AbstractAcceptorCache<ACCEPTOR, ?> acceptorCache;
    public byte currentTransmitterConnections = 0x00;

    private final TileEntityTransmitter transmitterTile;
    private final Set<TransmissionType> supportedTransmissionTypes;
    protected boolean redstoneReactive;
    private boolean redstonePowered;
    private boolean redstoneSet;
    private NETWORK theNetwork = null;
    private boolean orphaned = true;
    protected boolean isUpgrading;

    public Transmitter(TileEntityTransmitter transmitterTile, TransmissionType... transmissionTypes) {
        this.transmitterTile = transmitterTile;
        acceptorCache = createAcceptorCache();
        supportedTransmissionTypes = EnumSet.noneOf(TransmissionType.class);
        supportedTransmissionTypes.addAll(Arrays.asList(transmissionTypes));
    }

    protected AbstractAcceptorCache<ACCEPTOR, ?> createAcceptorCache() {
        return new AcceptorCache<>(this, getTransmitterTile());
    }

    public AbstractAcceptorCache<ACCEPTOR, ?> getAcceptorCache() {
        return acceptorCache;
    }

    public TileEntityTransmitter getTransmitterTile() {
        return transmitterTile;
    }

    /**
     * @apiNote Don't use this to directly modify the backing array, use the helper set methods.
     */
    public ConnectionType[] getConnectionTypesRaw() {
        return connectionTypes;
    }

    public void setConnectionTypesRaw(@Nonnull ConnectionType[] connectionTypes) {
        if (this.connectionTypes.length != connectionTypes.length) {
            throw new IllegalArgumentException("Mismatched connection types length");
        }
        this.connectionTypes = connectionTypes;
    }

    public ConnectionType getConnectionTypeRaw(@Nonnull Direction side) {
        return connectionTypes[side.ordinal()];
    }

    public void setConnectionTypeRaw(@Nonnull Direction side, @Nonnull ConnectionType type) {
        int index = side.ordinal();
        ConnectionType old = connectionTypes[index];
        if (old != type) {
            connectionTypes[index] = type;
            getTransmitterTile().sideChanged(side, old, type);
        }
    }

    @Override
    public BlockPos getTilePos() {
        return transmitterTile.getTilePos();
    }

    @Override
    public World getTileWorld() {
        return transmitterTile.getTileWorld();
    }

    @Override
    public Coord4D getTileCoord() {
        return transmitterTile.getTileCoord();
    }

    public boolean isRemote() {
        return transmitterTile.isRemote();
    }

    protected TRANSMITTER getTransmitter() {
        return (TRANSMITTER) this;
    }

    /**
     * Gets the network currently in use by this transmitter segment.
     *
     * @return network this transmitter is using
     */
    public NETWORK getTransmitterNetwork() {
        return theNetwork;
    }

    /**
     * Sets this transmitter segment's network to a new value.
     *
     * @param network - network to set to
     */
    public void setTransmitterNetwork(NETWORK network) {
        setTransmitterNetwork(network, true);
    }

    /**
     * Sets this transmitter segment's network to a new value.
     *
     * @param network    - network to set to
     * @param requestNow - Force a request now if not the return value will be if a request is needed
     */
    public boolean setTransmitterNetwork(NETWORK network, boolean requestNow) {
        if (theNetwork == network) {
            return false;
        }
        if (isRemote() && theNetwork != null) {
            theNetwork.removeTransmitter(getTransmitter());
        }
        theNetwork = network;
        orphaned = theNetwork == null;
        if (isRemote()) {
            if (theNetwork != null) {
                theNetwork.addTransmitter(getTransmitter());
            }
        } else if (requestNow) {
            //If we are requesting now request the update
            requestsUpdate();
        } else {
            //Otherwise return that we need to update it
            return true;
        }
        return false;
    }

    public boolean hasTransmitterNetwork() {
        return !isOrphan() && getTransmitterNetwork() != null;
    }

    public abstract NETWORK createEmptyNetwork();

    public abstract NETWORK createEmptyNetworkWithID(UUID networkID);

    public abstract NETWORK createNetworkByMerging(Collection<NETWORK> toMerge);

    public NETWORK getExternalNetwork(BlockPos from) {
        TileEntityTransmitter transmitter = WorldUtils.getTileEntity(TileEntityTransmitter.class, getTileWorld(), from);
        if (transmitter != null && supportsTransmissionType(transmitter)) {
            return (NETWORK) transmitter.getTransmitter().getTransmitterNetwork();
        }
        return null;
    }

    public boolean isValid() {
        return !getTransmitterTile().isRemoved() && getTransmitterTile().isLoaded();
    }


    public boolean isOrphan() {
        return orphaned;
    }

    public void setOrphan(boolean nowOrphaned) {
        orphaned = nowOrphaned;
    }

    /**
     * Get the transmitter's transmission types
     *
     * @return TransmissionType this transmitter uses
     */
    public Set<TransmissionType> getSupportedTransmissionTypes() {
        return supportedTransmissionTypes;
    }

    public boolean supportsTransmissionType(Transmitter<?, ?, ?> transmitter) {
        return transmitter.getSupportedTransmissionTypes().stream().anyMatch(supportedTransmissionTypes::contains);
    }

    public boolean supportsTransmissionType(TileEntityTransmitter transmitter) {
        return supportsTransmissionType(transmitter.getTransmitter());
    }

    @Nonnull
    public LazyOptional<ACCEPTOR> getAcceptor(Direction side) {
        return acceptorCache.getCachedAcceptor(side);
    }

    public boolean handlesRedstone() {
        return true;
    }

    public byte getAllCurrentConnections() {
        return (byte) (currentTransmitterConnections | acceptorCache.currentAcceptorConnections);
    }

    public boolean isValidTransmitter(Transmitter<?, ?, ?> transmitter) {
        return true;
    }

    public boolean canConnectToAcceptor(Direction side) {
        ConnectionType type = getConnectionTypeRaw(side);
        return type == ConnectionType.NORMAL || type == ConnectionType.PUSH;
    }

    /**
     * @apiNote Only call this from the server side
     */
    public boolean isValidAcceptor(TileEntity tile, Direction side) {
        //TODO: Rename this method better to make it more apparent that it caches and also listens to the acceptor
        //If it isn't a transmitter or the transmission type is different than the one the transmitter has
        return !(tile instanceof TileEntityTransmitter) || !supportsTransmissionType((TileEntityTransmitter) tile);
    }

    /**
     * Only call on the server
     */
    public void requestsUpdate() {
        getTransmitterTile().sendUpdatePacket();
    }

    @Nonnull
    public CompoundNBT getReducedUpdateTag(CompoundNBT updateTag) {
        updateTag.putByte(NBTConstants.CURRENT_CONNECTIONS, currentTransmitterConnections);
        updateTag.putByte(NBTConstants.CURRENT_ACCEPTORS, acceptorCache.currentAcceptorConnections);
        for (Direction direction : EnumUtils.DIRECTIONS) {
            updateTag.putInt(NBTConstants.SIDE + direction.ordinal(), getConnectionTypeRaw(direction).ordinal());
        }
        //Transmitter
        if (hasTransmitterNetwork()) {
            updateTag.putUUID(NBTConstants.NETWORK, getTransmitterNetwork().getUUID());
        }
        return updateTag;
    }

    public void handleUpdateTag(@Nonnull CompoundNBT tag) {
        NBTUtils.setByteIfPresent(tag, NBTConstants.CURRENT_CONNECTIONS, connections -> currentTransmitterConnections = connections);
        NBTUtils.setByteIfPresent(tag, NBTConstants.CURRENT_ACCEPTORS, acceptors -> acceptorCache.currentAcceptorConnections = acceptors);
        for (Direction direction : EnumUtils.DIRECTIONS) {
            NBTUtils.setEnumIfPresent(tag, NBTConstants.SIDE + direction.ordinal(), ConnectionType::byIndexStatic, type -> setConnectionTypeRaw(direction, type));
        }
        //Transmitter
        NBTUtils.setUUIDIfPresentElse(tag, NBTConstants.NETWORK, networkID -> {
            if (hasTransmitterNetwork() && getTransmitterNetwork().getUUID().equals(networkID)) {
                //Nothing needs to be done
                return;
            }
            DynamicNetwork<?, ?, ?> clientNetwork = TransmitterNetworkRegistry.getInstance().getClientNetwork(networkID);
            if (clientNetwork == null) {
                NETWORK network = createEmptyNetworkWithID(networkID);
                network.register();
                setTransmitterNetwork(network);
                handleContentsUpdateTag(network, tag);
            } else {
                //TODO: Validate network type?
                updateClientNetwork((NETWORK) clientNetwork);
            }
        }, () -> setTransmitterNetwork(null));
    }

    protected void updateClientNetwork(@Nonnull NETWORK network) {
        network.register();
        setTransmitterNetwork(network);
    }

    protected void handleContentsUpdateTag(@Nonnull NETWORK network, @Nonnull CompoundNBT tag) {
    }

    public void read(@Nonnull CompoundNBT nbtTags) {
        redstoneReactive = nbtTags.getBoolean(NBTConstants.REDSTONE);
        for (Direction direction : EnumUtils.DIRECTIONS) {
            NBTUtils.setEnumIfPresent(nbtTags, NBTConstants.CONNECTION + direction.ordinal(), ConnectionType::byIndexStatic, type -> setConnectionTypeRaw(direction, type));
        }
    }

    @Nonnull
    public CompoundNBT write(@Nonnull CompoundNBT nbtTags) {
        nbtTags.putBoolean(NBTConstants.REDSTONE, redstoneReactive);
        for (Direction direction : EnumUtils.DIRECTIONS) {
            nbtTags.putInt(NBTConstants.CONNECTION + direction.ordinal(), getConnectionTypeRaw(direction).ordinal());
        }
        return nbtTags;
    }

    public void markDirtyAcceptor(Direction side) {
        if (hasTransmitterNetwork()) {
            getTransmitterNetwork().acceptorChanged(getTransmitter(), side);
        }
    }

    public void remove() {
        //Clear our cached listeners
        acceptorCache.clear();
    }

    public void onChunkUnload() {
        //Clear our cached listeners
        acceptorCache.clear();
    }

    public ConnectionType getConnectionType(Direction side) {
        return getConnectionType(side, getAllCurrentConnections(), currentTransmitterConnections, connectionTypes);
    }

    public Set<Direction> getConnections(ConnectionType type) {
        Set<Direction> sides = null;
        for (Direction side : EnumUtils.DIRECTIONS) {
            if (getConnectionType(side) == type) {
                if (sides == null) {
                    //Lazy init the set so that if there are none we can just use an empty set
                    // instead of having to initialize an enum set
                    sides =  EnumSet.noneOf(Direction.class);
                }
                sides.add(side);
            }
        }
        return sides == null ? Collections.emptySet() : sides;
    }

    public ActionResultType onConfigure(PlayerEntity player, Direction side) {
        return ActionResultType.PASS;
    }

    public ActionResultType onRightClick(PlayerEntity player, Direction side) {
        return ActionResultType.SUCCESS;
    }

    public abstract void takeShare();

}
