package com.nuggylib.enchantmentsseer.common.network.to_server;

import com.nuggylib.enchantmentsseer.common.inventory.container.ContainerProvider;
import com.nuggylib.enchantmentsseer.common.network.IEnchantmentsSeerPacket;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkHooks;

import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from Mekanism; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_server/PacketOpenGui.java"
 */
public class PacketOpenGui implements IEnchantmentsSeerPacket {

    private final GuiType type;

    public PacketOpenGui(GuiType type) {
        this.type = type;
    }

    @Override
    public void handle(NetworkEvent.Context context) {
        ServerPlayerEntity player = context.getSender();
        if (player != null && type.shouldOpenForPlayer.test(player)) {
            NetworkHooks.openGui(player, type.containerSupplier.get());
        }
    }

    @Override
    public void encode(PacketBuffer buffer) {
        buffer.writeEnum(type);
    }

    public static PacketOpenGui decode(PacketBuffer buffer) {
        return new PacketOpenGui(buffer.readEnum(GuiType.class));
    }

    public enum GuiType {
        ;

        private final Supplier<INamedContainerProvider> containerSupplier;
        private final Predicate<PlayerEntity> shouldOpenForPlayer;

        GuiType(Supplier<INamedContainerProvider> containerSupplier) {
            this(containerSupplier, player -> true);
        }

        GuiType(Supplier<INamedContainerProvider> containerSupplier, Predicate<PlayerEntity> shouldOpenForPlayer) {
            this.containerSupplier = containerSupplier;
            this.shouldOpenForPlayer = shouldOpenForPlayer;
        }
    }
}
