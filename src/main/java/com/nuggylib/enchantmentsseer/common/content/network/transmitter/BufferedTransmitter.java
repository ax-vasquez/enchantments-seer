package com.nuggylib.enchantmentsseer.common.content.network.transmitter;

import com.nuggylib.enchantmentsseer.common.lib.transmitter.DynamicBufferedNetwork;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.TransmissionType;
import com.nuggylib.enchantmentsseer.common.tile.transmitter.TileEntityTransmitter;
import net.minecraft.nbt.CompoundNBT;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <ACCEPTOR>
 * @param <NETWORK>
 * @param <BUFFER>
 * @param <TRANSMITTER>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/content/network/transmitter/BufferedTransmitter.java"
 */
public abstract class BufferedTransmitter<ACCEPTOR, NETWORK extends DynamicBufferedNetwork<ACCEPTOR, NETWORK, BUFFER, TRANSMITTER>, BUFFER,
        TRANSMITTER extends BufferedTransmitter<ACCEPTOR, NETWORK, BUFFER, TRANSMITTER>> extends Transmitter<ACCEPTOR, NETWORK, TRANSMITTER> {

    public BufferedTransmitter(TileEntityTransmitter tile, TransmissionType... transmissionTypes) {
        super(tile, transmissionTypes);
    }

    public long getTransmitterNetworkCapacity() {
        return hasTransmitterNetwork() ? getTransmitterNetwork().getCapacity() : getCapacity();
    }

    /**
     * @apiNote Only call from the server side
     */
    protected abstract void pullFromAcceptors();

    public abstract long getCapacity();

    /**
     * If the transmitter does not have a buffer this will try to fallback on the network's buffer.
     *
     * @return The transmitter's buffer, or if null the network's buffer.
     */
    @Nonnull
    public abstract BUFFER getBufferWithFallback();

    /**
     * @return True if the buffer with fallback is null (or empty)
     */
    public abstract boolean noBufferOrFallback();

    protected boolean canHaveIncompatibleNetworks() {
        return false;
    }

    @Override
    public boolean isValidTransmitter(Transmitter<?, ?, ?> transmitter) {
        if (canHaveIncompatibleNetworks() && transmitter instanceof BufferedTransmitter) {
            BufferedTransmitter<?, ?, ?, ?> other = (BufferedTransmitter<?, ?, ?, ?>) transmitter;
            if (other.canHaveIncompatibleNetworks()) {
                //If it is a transmitter, only declare it as valid, if we don't have a combination
                // of a transmitter with a network and an orphaned transmitter, but only bother if
                // we can have incompatible networks
                if (hasTransmitterNetwork() && other.isOrphan() || other.hasTransmitterNetwork() && isOrphan()) {
                    return false;
                }
            }
        }
        return super.isValidTransmitter(transmitter);
    }

    @Override
    protected void handleContentsUpdateTag(@Nonnull NETWORK network, @Nonnull CompoundNBT tag) {
        network.updateCapacity();
    }

    @Override
    protected void updateClientNetwork(@Nonnull NETWORK network) {
        super.updateClientNetwork(network);
        network.updateCapacity();
    }

    /**
     * @return Gets and releases the transmitter's buffer.
     *
     * @apiNote Should only be {@code null}, if the buffer type supports null. So things like fluid's should use the empty variant.
     */
    public abstract BUFFER releaseShare();

    /**
     * @return Gets the transmitter's buffer.
     */
    @Nonnull
    public abstract BUFFER getShare();
}