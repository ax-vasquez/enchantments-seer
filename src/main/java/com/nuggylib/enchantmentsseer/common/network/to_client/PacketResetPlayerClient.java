package com.nuggylib.enchantmentsseer.common.network.to_client;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.network.IEnchantmentsSeerPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_client/PacketResetPlayerClient.java"
 */
public class PacketResetPlayerClient implements IEnchantmentsSeerPacket {

    private final UUID uuid;

    public PacketResetPlayerClient(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        EnchantmentsSeer.playerState.clearPlayer(uuid, true);
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
    }

    public static PacketResetPlayerClient decode(PacketBuffer buffer) {
        return new PacketResetPlayerClient(buffer.readUUID());
    }
}