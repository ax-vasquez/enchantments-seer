package com.nuggylib.enchantmentsseer.common.base;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.network.to_client.PacketResetPlayerClient;
import it.unimi.dsi.fastutil.objects.Object2FloatMap;
import it.unimi.dsi.fastutil.objects.Object2FloatOpenHashMap;
import net.minecraft.world.IWorld;
import java.util.UUID;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/base/PlayerState.java"
 */
public class PlayerState {

    private final Object2FloatMap<UUID> stepAssistedPlayers = new Object2FloatOpenHashMap<>();

    private IWorld world;

    public void clear(boolean isRemote) {
        if (!isRemote) {
            stepAssistedPlayers.clear();
        }
    }

    public void clearPlayer(UUID uuid, boolean isRemote) {
        if (!isRemote) {
            EnchantmentsSeer.packetHandler.sendToAll(new PacketResetPlayerClient(uuid));
        }
    }

    public void init(IWorld world) {
        this.world = world;
    }

}
