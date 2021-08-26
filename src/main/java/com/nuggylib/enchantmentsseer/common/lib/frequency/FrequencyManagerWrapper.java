package com.nuggylib.enchantmentsseer.common.lib.frequency;

import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.tileentity.TileEntity;

import java.util.*;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/frequency/FrequencyManagerWrapper.java"
 */
public class FrequencyManagerWrapper<FREQ extends Frequency> {

    private final Type type;
    private final FrequencyType<FREQ> frequencyType;
    private FrequencyManager<FREQ> publicManager;
    private Map<UUID, FrequencyManager<FREQ>> privateManagers;

    private FrequencyManagerWrapper(Type type, FrequencyType<FREQ> frequencyType) {
        this.type = type;
        this.frequencyType = frequencyType;

        if (type.supportsPublic()) {
            publicManager = new FrequencyManager<>(frequencyType);
        }
        if (type.supportsPrivate()) {
            privateManagers = new Object2ObjectOpenHashMap<>();
        }
    }

    public static <FREQ extends Frequency> FrequencyManagerWrapper<FREQ> create(FrequencyType<FREQ> frequencyType, Type type) {
        return new FrequencyManagerWrapper<>(type, frequencyType);
    }

    public FrequencyManager<FREQ> getPublicManager() {
        if (!type.supportsPublic()) {
            EnchantmentsSeer.MAIN_LOGGER.error("Attempted to access public frequency manager of type {}. This shouldn't happen!", frequencyType.getName());
            return null;
        }

        return publicManager;
    }

    public FrequencyManager<FREQ> getPrivateManager(UUID ownerUUID) {
        if (!type.supportsPrivate()) {
            EnchantmentsSeer.MAIN_LOGGER.error("Attempted to access private frequency manager of type {}. This shouldn't happen!", frequencyType.getName());
            return null;
        } else if (ownerUUID == null) {
            EnchantmentsSeer.MAIN_LOGGER.error("Attempted to access private frequency manager of type {} with no owner. This shouldn't happen!", frequencyType.getName());
            return null;
        }

        if (!privateManagers.containsKey(ownerUUID)) {
            FrequencyManager<FREQ> manager = new FrequencyManager<>(frequencyType, ownerUUID);
            privateManagers.put(ownerUUID, manager);
            manager.createOrLoad();
        }
        return privateManagers.get(ownerUUID);
    }

    public List<FREQ> getPublicFrequencies(TileEntity tile, List<FREQ> cache) {
        return tile.getLevel().isClientSide() ? cache : new ArrayList<>(getPublicManager().getFrequencies());
    }

    public <TILE extends TileEntity & ISecurityTile> List<FREQ> getPrivateFrequencies(TILE tile, List<FREQ> cache) {
        if (tile.getLevel().isClientSide()) {
            return cache;
        }
        UUID ownerUUID = tile.getOwnerUUID();
        if (ownerUUID == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(getPrivateManager(ownerUUID).getFrequencies());
    }

    public void clear() {
        if (privateManagers != null) {
            privateManagers.clear();
        }
    }

    public enum Type {
        PUBLIC_ONLY,
        PRIVATE_ONLY,
        PUBLIC_PRIVATE;

        boolean supportsPublic() {
            return this == PUBLIC_ONLY || this == PUBLIC_PRIVATE;
        }

        boolean supportsPrivate() {
            return this == PRIVATE_ONLY || this == PUBLIC_PRIVATE;
        }
    }
}

