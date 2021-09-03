package com.nuggylib.enchantmentsseer.common.lib.transmitter.acceptor;

import com.nuggylib.enchantmentsseer.api.annotations.FieldsAreNonnullByDefault;
import com.nuggylib.enchantmentsseer.common.content.network.transmitter.Transmitter;
import com.nuggylib.enchantmentsseer.common.tile.transmitter.TileEntityTransmitter;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.lang.ref.WeakReference;
import java.util.*;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <ACCEPTOR>
 * @param <INFO>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/transmitter/acceptor/AbstractAcceptorCache.java"
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public abstract class AbstractAcceptorCache<ACCEPTOR, INFO extends AbstractAcceptorInfo> {

    private final Map<Direction, NonNullConsumer<LazyOptional<ACCEPTOR>>> cachedListeners = new EnumMap<>(Direction.class);
    protected final Map<Direction, INFO> cachedAcceptors = new EnumMap<>(Direction.class);
    protected final Transmitter<ACCEPTOR, ?, ?> transmitter;
    private final TileEntityTransmitter transmitterTile;
    public byte currentAcceptorConnections = 0x00;

    protected AbstractAcceptorCache(Transmitter<ACCEPTOR, ?, ?> transmitter, TileEntityTransmitter transmitterTile) {
        this.transmitter = transmitter;
        this.transmitterTile = transmitterTile;
    }

    public void clear() {
        cachedListeners.clear();
        cachedAcceptors.clear();
    }

    public void invalidateCachedAcceptor(Direction side) {
        if (cachedAcceptors.containsKey(side)) {
            //Fully remove the cached acceptor, both the tile, and the value
            cachedAcceptors.remove(side);
            transmitter.markDirtyAcceptor(side);
            //Note: as our listeners don't care about the actual implementation of the tile on a given side
            // we don't bother invalidating and getting rid of the old listener when we remove the cached
            // acceptor as we don't want to accidentally flood the acceptor with listeners if something
            // breaks on our end.
        }
    }

    public LazyOptional<ACCEPTOR> getCachedAcceptor(Direction side) {
        return Transmitter.connectionMapContainsSide(currentAcceptorConnections, side) ? getConnectedAcceptor(side) : LazyOptional.empty();
    }

    public List<ACCEPTOR> getConnectedAcceptors(Set<Direction> sides) {
        List<ACCEPTOR> acceptors = new ArrayList<>(sides.size());
        for (Direction side : sides) {
            getConnectedAcceptor(side).ifPresent(acceptors::add);
        }
        return acceptors;
    }

    protected abstract LazyOptional<ACCEPTOR> getConnectedAcceptor(Direction side);

    /**
     * Gets the listener that will refresh connections on a given side.
     */
    protected NonNullConsumer<LazyOptional<ACCEPTOR>> getRefreshListener(@Nonnull Direction side) {
        return cachedListeners.computeIfAbsent(side, s -> new RefreshListener<>(transmitterTile, s));
    }

    private static class RefreshListener<ACCEPTOR> implements NonNullConsumer<LazyOptional<ACCEPTOR>> {

        //Note: We only keep a weak reference to the tile from inside the listener so that if it gets unloaded it can be released from memory
        // instead of being referenced by the listener still in the tile in a neighboring chunk
        private final WeakReference<TileEntityTransmitter> tile;
        private final Direction side;

        private RefreshListener(TileEntityTransmitter tile, Direction side) {
            this.tile = new WeakReference<>(tile);
            this.side = side;
        }

        @Override
        public void accept(@Nonnull LazyOptional<ACCEPTOR> ignored) {
            // TODO: See if we need this
        }
    }
}
