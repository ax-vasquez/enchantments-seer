package com.nuggylib.enchantmentsseer.common.network.to_client;

import com.nuggylib.enchantmentsseer.common.network.IEnchantmentsSeerPacket;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.UUID;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_client/PacketPlayerData.java"
 */
public class PacketPlayerData implements IEnchantmentsSeerPacket {

    private final UUID uuid;

    public PacketPlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    @Override
    public void handle(NetworkEvent.Context context) {

    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeUUID(uuid);
    }

    public static PacketPlayerData decode(PacketBuffer buffer) {
        return new PacketPlayerData(buffer.readUUID());
    }
}
