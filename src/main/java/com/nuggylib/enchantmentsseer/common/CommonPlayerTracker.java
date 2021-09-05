package com.nuggylib.enchantmentsseer.common;

import com.nuggylib.enchantmentsseer.common.network.to_client.PacketPlayerData;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/CommonPlayerTracker.java"
 */
public class CommonPlayerTracker {

    public CommonPlayerTracker() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onPlayerLoginEvent(PlayerEvent.PlayerLoggedInEvent event) {
        // TODO
    }

    @SubscribeEvent
    public void onPlayerLogoutEvent(PlayerEvent.PlayerLoggedOutEvent event) {
        // TODO
    }

    @SubscribeEvent
    public void onPlayerDimChangedEvent(PlayerEvent.PlayerChangedDimensionEvent event) {
        // TODO
    }

    @SubscribeEvent
    public void onPlayerStartTrackingEvent(PlayerEvent.StartTracking event) {
        if (event.getTarget() instanceof PlayerEntity && event.getPlayer() instanceof ServerPlayerEntity) {
            EnchantmentsSeer.packetHandler.sendTo(new PacketPlayerData(event.getTarget().getUUID()), (ServerPlayerEntity) event.getPlayer());
        }
    }

    @SubscribeEvent
    public void attachCaps(AttachCapabilitiesEvent<Entity> event) {
        // TODO
    }

    @SubscribeEvent
    public void cloneEvent(PlayerEvent.Clone event) {
        // TODO
    }

    @SubscribeEvent
    public void respawnEvent(PlayerEvent.PlayerRespawnEvent event) {
        // TODO
    }

    @SubscribeEvent
    public void rightClickEvent(PlayerInteractEvent.RightClickBlock blockEvent) {
        // TODO
    }
}
