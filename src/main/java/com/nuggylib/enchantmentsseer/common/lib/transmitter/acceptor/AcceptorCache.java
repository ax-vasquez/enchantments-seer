package com.nuggylib.enchantmentsseer.common.lib.transmitter.acceptor;

import com.nuggylib.enchantmentsseer.api.annotations.FieldsAreNonnullByDefault;
import com.nuggylib.enchantmentsseer.common.content.network.transmitter.Transmitter;
import com.nuggylib.enchantmentsseer.common.tile.transmitter.TileEntityTransmitter;
import com.nuggylib.enchantmentsseer.common.util.CapabilityUtils;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.common.util.NonNullConsumer;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <ACCEPTOR>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/transmitter/acceptor/AcceptorCache.java"
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class AcceptorCache<ACCEPTOR> extends AbstractAcceptorCache<ACCEPTOR, AcceptorCache.AcceptorInfo<ACCEPTOR>> {

    public AcceptorCache(Transmitter<ACCEPTOR, ?, ?> transmitter, TileEntityTransmitter transmitterTile) {
        super(transmitter, transmitterTile);
    }

    protected void updateCachedAcceptorAndListen(Direction side, TileEntity acceptorTile, LazyOptional<ACCEPTOR> acceptor) {
        updateCachedAcceptorAndListen(side, acceptorTile, acceptor, acceptor, true);
    }

    protected void updateCachedAcceptorAndListen(Direction side, TileEntity acceptorTile, LazyOptional<ACCEPTOR> acceptor, LazyOptional<?> sourceAcceptor,
                                                 boolean sourceIsSame) {
        boolean dirtyAcceptor = false;
        if (cachedAcceptors.containsKey(side)) {
            AcceptorInfo<ACCEPTOR> acceptorInfo = cachedAcceptors.get(side);
            if (acceptorTile != acceptorInfo.getTile()) {
                //The tile changed, fully invalidate it
                cachedAcceptors.put(side, new AcceptorInfo<>(acceptorTile, sourceAcceptor, acceptor));
                dirtyAcceptor = true;
            } else if (sourceAcceptor != acceptorInfo.sourceAcceptor) {
                //The source acceptor is different, make sure we update it and the actual acceptor
                // This allows us to make sure we only mark the acceptor as dirty if it actually changed
                // Use case: Wrapped energy acceptors
                acceptorInfo.updateAcceptor(sourceAcceptor, acceptor);
                dirtyAcceptor = true;
            }
        } else {
            cachedAcceptors.put(side, new AcceptorInfo<>(acceptorTile, sourceAcceptor, acceptor));
            dirtyAcceptor = true;
        }
        if (dirtyAcceptor) {
            transmitter.markDirtyAcceptor(side);
            //If the capability is present and we want to add the listener, add a listener so that once it gets invalidated
            // we recheck that side assuming that the world and position is still loaded and our tile has not been removed
            NonNullConsumer<LazyOptional<ACCEPTOR>> refreshListener = getRefreshListener(side);
            if (sourceIsSame) {
                //Add it to the actual acceptor as it is the same as the source and we can do so without any unchecked warnings
                acceptor.addListener(refreshListener);
            } else {
                //Otherwise use unchecked generics to add the listener to the source acceptor
                CapabilityUtils.addListener(sourceAcceptor, refreshListener);
            }
        }
    }

    /**
     * @implNote Grabs the acceptors from cache
     */
    @Override
    public LazyOptional<ACCEPTOR> getConnectedAcceptor(Direction side) {
        if (cachedAcceptors.containsKey(side)) {
            AcceptorInfo<ACCEPTOR> acceptorInfo = cachedAcceptors.get(side);
            if (!acceptorInfo.getTile().isRemoved()) {
                return acceptorInfo.acceptor;
            }
            //TODO: If the tile has been removed should we force an invalidation/recheck?
        }
        return LazyOptional.empty();
    }

    /**
     * @apiNote Only call this from the server side
     */
    public boolean isAcceptorAndListen(@Nullable TileEntity tile, Direction side, Capability<ACCEPTOR> capability) {
        LazyOptional<ACCEPTOR> acceptor = CapabilityUtils.getCapability(tile, capability, side.getOpposite());
        if (acceptor.isPresent()) {
            //Update the cached acceptor and if it changed, add a listener to it to listen for invalidation
            updateCachedAcceptorAndListen(side, tile, acceptor);
            return true;
        }
        return false;
    }

    public static class AcceptorInfo<ACCEPTOR> extends AbstractAcceptorInfo {

        private LazyOptional<?> sourceAcceptor;
        private LazyOptional<ACCEPTOR> acceptor;

        private AcceptorInfo(TileEntity tile, LazyOptional<?> sourceAcceptor, LazyOptional<ACCEPTOR> acceptor) {
            super(tile);
            this.acceptor = acceptor;
            this.sourceAcceptor = sourceAcceptor;
        }

        private void updateAcceptor(LazyOptional<?> sourceAcceptor, LazyOptional<ACCEPTOR> acceptor) {
            this.sourceAcceptor = sourceAcceptor;
            this.acceptor = acceptor;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) {
                return true;
            }
            if (o instanceof AcceptorInfo) {
                AcceptorInfo<?> other = (AcceptorInfo<?>) o;
                return getTile().equals(other.getTile()) && sourceAcceptor.equals(other.sourceAcceptor) && acceptor.equals(other.acceptor);
            }
            return false;
        }

        @Override
        public int hashCode() {
            return Objects.hash(getTile(), sourceAcceptor, acceptor);
        }
    }
}
