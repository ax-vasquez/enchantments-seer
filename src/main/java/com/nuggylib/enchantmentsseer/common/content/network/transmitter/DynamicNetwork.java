package com.nuggylib.enchantmentsseer.common.content.network.transmitter;

import com.nuggylib.enchantmentsseer.client.render.text.IHasTextComponent;
import com.nuggylib.enchantmentsseer.common.content.network.transmitter.acceptor.NetworkAcceptorCache;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.TransmitterNetworkRegistry;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.thread.EffectiveSide;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <ACCEPTOR>
 * @param <NETWORK>
 * @param <TRANSMITTER>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/transmitter/DynamicNetwork.java"
 */
public abstract class DynamicNetwork<ACCEPTOR, NETWORK extends DynamicNetwork<ACCEPTOR, NETWORK, TRANSMITTER>,
        TRANSMITTER extends Transmitter<ACCEPTOR, NETWORK, TRANSMITTER>> implements INetworkDataHandler, IHasTextComponent {

    protected final Set<TRANSMITTER> transmitters = new ObjectOpenHashSet<>();
    protected final Set<TRANSMITTER> transmittersToAdd = new ObjectOpenHashSet<>();
    protected final NetworkAcceptorCache<ACCEPTOR> acceptorCache = new NetworkAcceptorCache<>();
    @Nullable
    protected World world;
    private final UUID uuid;

    protected DynamicNetwork() {
        this(UUID.randomUUID());
    }

    protected DynamicNetwork(UUID networkID) {
        this.uuid = networkID;
    }

    public UUID getUUID() {
        return uuid;
    }

    protected NETWORK getNetwork() {
        return (NETWORK) this;
    }

    public void addNewTransmitters(Collection<TRANSMITTER> newTransmitters) {
        transmittersToAdd.addAll(newTransmitters);
    }

    public void commit() {
        if (!transmittersToAdd.isEmpty()) {
            boolean addedValidTransmitters = false;
            List<TRANSMITTER> transmittersToUpdate = new ArrayList<>();
            for (TRANSMITTER transmitter : transmittersToAdd) {
                //Note: Transmitter should not be able to be null here, but I ran into a null pointer
                // pointing to it being null that I could not reproduce, so just added this as a safety check
                if (transmitter != null && transmitter.isValid()) {
                    addedValidTransmitters = true;
                    if (world == null) {
                        world = transmitter.getTileWorld();
                    }
                    for (Direction side : EnumUtils.DIRECTIONS) {
                        acceptorCache.updateTransmitterOnSide(transmitter, side);
                    }
                    if (transmitter.setTransmitterNetwork(getNetwork(), false)) {
                        transmittersToUpdate.add(transmitter);
                    }
                    addTransmitterFromCommit(transmitter);
                }
            }
            transmittersToAdd.clear();
            if (addedValidTransmitters) {
                validTransmittersAdded();
                transmittersToUpdate.forEach(Transmitter::requestsUpdate);
            }
        }
        acceptorCache.commit();
    }

    protected void addTransmitterFromCommit(TRANSMITTER transmitter) {
        transmitters.add(transmitter);
    }

    protected void validTransmittersAdded() {
    }

    public boolean isRemote() {
        return world == null ? EffectiveSide.get().isClient() : world.isClientSide;
    }

    public void invalidate(@Nullable TRANSMITTER triggerTransmitter) {
        if (transmitters.size() == 1 && triggerTransmitter != null) {
            //We're destroying the last transmitter in the network
            onLastTransmitterRemoved(triggerTransmitter);
        }
        removeInvalid(triggerTransmitter);
        //Now invalidate the transmitters
        if (!isRemote()) {
            for (TRANSMITTER transmitter : transmitters) {
                if (transmitter.isValid()) {
                    transmitter.takeShare();
                    transmitter.setTransmitterNetwork(null);
                    TransmitterNetworkRegistry.registerOrphanTransmitter(transmitter);
                }
            }
        }
        deregister();
    }

    protected void onLastTransmitterRemoved(@Nonnull TRANSMITTER triggerTransmitter) {
    }

    protected void removeInvalid(@Nullable TRANSMITTER triggerTransmitter) {
        //Remove invalid transmitters first for share calculations
        transmitters.removeIf(transmitter -> !transmitter.isValid());
    }

    public void acceptorChanged(TRANSMITTER transmitter, Direction side) {
        acceptorCache.acceptorChanged(transmitter, side);
    }

    public List<TRANSMITTER> adoptTransmittersAndAcceptorsFrom(NETWORK net) {
        List<TRANSMITTER> transmittersToUpdate = new ArrayList<>();
        for (TRANSMITTER transmitter : net.transmitters) {
            transmitters.add(transmitter);
            if (transmitter.setTransmitterNetwork(getNetwork(), false)) {
                transmittersToUpdate.add(transmitter);
            }
        }
        transmittersToAdd.addAll(net.transmittersToAdd);
        acceptorCache.adoptAcceptors(net.acceptorCache);
        return transmittersToUpdate;
    }

    protected void adoptAllAndRegister(Collection<NETWORK> networks) {
        List<TRANSMITTER> transmittersToUpdate = new ArrayList<>();
        for (NETWORK net : networks) {
            if (net != null) {
                transmittersToUpdate.addAll(adoptTransmittersAndAcceptorsFrom(net));
                net.deregister();
            }
        }
        register();
        transmittersToUpdate.forEach(Transmitter::requestsUpdate);
    }

    public void register() {
        if (isRemote()) {
            TransmitterNetworkRegistry.getInstance().addClientNetwork(getUUID(), this);
        } else {
            TransmitterNetworkRegistry.getInstance().registerNetwork(this);
        }
    }

    public void deregister() {
        transmitters.clear();
        transmittersToAdd.clear();
        if (isRemote()) {
            TransmitterNetworkRegistry.getInstance().removeClientNetwork(this);
        } else {
            TransmitterNetworkRegistry.getInstance().removeNetwork(this);
        }
    }

    public boolean isEmpty() {
        return transmitters.isEmpty();
    }

    public int getAcceptorCount() {
        return acceptorCache.getAcceptorCount();
    }

    @Nullable
    public World getWorld() {
        return world;
    }

    /**
     * @apiNote Only called on the server
     */
    public void onUpdate() {
    }

    public Set<TRANSMITTER> getTransmitters() {
        return transmitters;
    }

    public void addTransmitter(TRANSMITTER transmitter) {
        transmitters.add(transmitter);
    }

    public void removeTransmitter(TRANSMITTER transmitter) {
        transmitters.remove(transmitter);
        if (transmitters.isEmpty()) {
            deregister();
        }
    }

    public int transmittersSize() {
        return transmitters.size();
    }

    public boolean hasAcceptor(BlockPos acceptorPos) {
        return acceptorCache.hasAcceptor(acceptorPos);
    }

    public Set<Direction> getAcceptorDirections(BlockPos pos) {
        return acceptorCache.getAcceptorDirections(pos);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }
        if (o instanceof DynamicNetwork) {
            return uuid.equals(((DynamicNetwork<?, ?, ?>) o).uuid);
        }
        return false;
    }

    @Override
    public int hashCode() {
        return uuid.hashCode();
    }
}

