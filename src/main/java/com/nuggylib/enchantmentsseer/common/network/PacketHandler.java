package com.nuggylib.enchantmentsseer.common.network;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.network.to_server.PacketGuiButtonPress;
import com.nuggylib.enchantmentsseer.common.network.to_server.PacketGuiInteract;
import com.nuggylib.enchantmentsseer.common.network.to_server.PacketKey;
import com.nuggylib.enchantmentsseer.common.network.to_server.PacketOpenGui;
import net.minecraftforge.fml.network.simple.SimpleChannel;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/PacketHandler.java"
 */
public class PacketHandler extends BasePacketHandler {

    private static final SimpleChannel netHandler = createChannel(EnchantmentsSeer.rl(EnchantmentsSeer.MOD_ID));

    @Override
    protected SimpleChannel getChannel() {
        return netHandler;
    }

    @Override
    public void initialize() {
        //Client to server messages
        registerClientToServer(PacketGuiButtonPress.class, PacketGuiButtonPress::decode);
        registerClientToServer(PacketGuiInteract.class, PacketGuiInteract::decode);
        registerClientToServer(PacketKey.class, PacketKey::decode);
        registerClientToServer(PacketOpenGui.class, PacketOpenGui::decode);

        // TODO: See if we need to worry about server to client message (refer to Mekanism for guidance)
    }
}
