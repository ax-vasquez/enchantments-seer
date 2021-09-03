package com.nuggylib.enchantmentsseer.common.content.network.transmitter.acceptor;

import com.nuggylib.enchantmentsseer.common.content.network.transmitter.Transmitter;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.TransmitterNetworkRegistry;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.common.util.LazyOptional;

import java.util.*;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <ACCEPTOR>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/transmitter/acceptor/NetworkAcceptorCache.java"
 */
public class NetworkAcceptorCache<ACCEPTOR> {

    private final Map<BlockPos, Map<Direction, LazyOptional<ACCEPTOR>>> cachedAcceptors = new Object2ObjectOpenHashMap<>();
    private final Map<Transmitter<ACCEPTOR, ?, ?>, Set<Direction>> changedAcceptors = new Object2ObjectOpenHashMap<>();

    public void updateTransmitterOnSide(Transmitter<ACCEPTOR, ?, ?> transmitter, Direction side) {
        LazyOptional<ACCEPTOR> acceptor = transmitter.canConnectToAcceptor(side) ? transmitter.getAcceptor(side) : LazyOptional.empty();
        BlockPos acceptorPos = transmitter.getTilePos().relative(side);
        if (acceptor.isPresent()) {
            cachedAcceptors.computeIfAbsent(acceptorPos, pos -> new EnumMap<>(Direction.class)).put(side.getOpposite(), acceptor);
        } else if (cachedAcceptors.containsKey(acceptorPos)) {
            Map<Direction, LazyOptional<ACCEPTOR>> cached = cachedAcceptors.get(acceptorPos);
            cached.remove(side.getOpposite());
            if (cached.isEmpty()) {
                cachedAcceptors.remove(acceptorPos);
            }
        } else {
            cachedAcceptors.remove(acceptorPos);
        }
    }

    public void adoptAcceptors(NetworkAcceptorCache<ACCEPTOR> other) {
        for (Map.Entry<BlockPos, Map<Direction, LazyOptional<ACCEPTOR>>> entry : other.cachedAcceptors.entrySet()) {
            BlockPos pos = entry.getKey();
            if (cachedAcceptors.containsKey(pos)) {
                Map<Direction, LazyOptional<ACCEPTOR>> cached = cachedAcceptors.get(pos);
                entry.getValue().forEach(cached::put);
            } else {
                cachedAcceptors.put(pos, entry.getValue());
            }
        }
    }

    public void acceptorChanged(Transmitter<ACCEPTOR, ?, ?> transmitter, Direction side) {
        changedAcceptors.computeIfAbsent(transmitter, t -> EnumSet.noneOf(Direction.class)).add(side);
        TransmitterNetworkRegistry.registerChangedNetwork(transmitter.getTransmitterNetwork());
    }

    public void commit() {
        if (!changedAcceptors.isEmpty()) {
            for (Map.Entry<Transmitter<ACCEPTOR, ?, ?>, Set<Direction>> entry : changedAcceptors.entrySet()) {
                Transmitter<ACCEPTOR, ?, ?> transmitter = entry.getKey();
                if (transmitter.isValid()) {
                    //Update all the changed directions
                    for (Direction side : entry.getValue()) {
                        updateTransmitterOnSide(transmitter, side);
                    }
                }
            }
            changedAcceptors.clear();
        }
    }

    /**
     * @apiNote Listeners should not be added to these LazyOptionals here as they may not correspond to an actual handler and may not get invalidated.
     */
    public Set<Map.Entry<BlockPos, Map<Direction, LazyOptional<ACCEPTOR>>>> getAcceptorEntrySet() {
        return cachedAcceptors.entrySet();
    }

    /**
     * @apiNote Listeners should not be added to these LazyOptionals here as they may not correspond to an actual handler and may not get invalidated.
     */
    public Collection<Map<Direction, LazyOptional<ACCEPTOR>>> getAcceptorValues() {
        return cachedAcceptors.values();
    }

    public int getAcceptorCount() {
        //Count multiple connections to the same position as multiple acceptors
        return cachedAcceptors.values().stream().mapToInt(Map::size).sum();
    }

    public boolean hasAcceptor(BlockPos acceptorPos) {
        return cachedAcceptors.containsKey(acceptorPos);
    }

    public Set<Direction> getAcceptorDirections(BlockPos pos) {
        //TODO: Do this better?
        return cachedAcceptors.get(pos).keySet();
    }
}
