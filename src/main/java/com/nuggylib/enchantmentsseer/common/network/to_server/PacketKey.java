package com.nuggylib.enchantmentsseer.common.network.to_server;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.network.IEnchantmentsSeerPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_server/PacketKey.java"
 */
public class PacketKey implements IEnchantmentsSeerPacket {

    private final int key;
    private final boolean add;

    public PacketKey(int key, boolean add) {
        this.key = key;
        this.add = add;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        PlayerEntity player = context.getSender();
        if (player != null) {
            if (add) {
                EnchantmentsSeer.keyMap.add(player.getUUID(), key);
            } else {
                EnchantmentsSeer.keyMap.remove(player.getUUID(), key);
            }
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeVarInt(key);
        buffer.writeBoolean(add);
    }

    public static PacketKey decode(PacketBuffer buffer) {
        return new PacketKey(buffer.readVarInt(), buffer.readBoolean());
    }
}
