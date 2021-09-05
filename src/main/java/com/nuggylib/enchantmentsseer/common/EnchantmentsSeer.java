/*
 *     A Minecraft mod that adds an alternative way to enchant items.
 *     Copyright (C) 2021 Armando Vasquez
 *
 *     This program is free software: you can redistribute it and/or modify
 *     it under the terms of the GNU General Public License as published by
 *     the Free Software Foundation, either version 3 of the License, or
 *     (at your option) any later version.
 *
 *     This program is distributed in the hope that it will be useful,
 *     but WITHOUT ANY WARRANTY; without even the implied warranty of
 *     MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *     GNU General Public License for more details.
 *
 *     You should have received a copy of the GNU General Public License
 *     along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.nuggylib.enchantmentsseer.common;

import com.mojang.authlib.GameProfile;
import com.nuggylib.enchantmentsseer.common.base.EnchantmentSeerFakePlayer;
import com.nuggylib.enchantmentsseer.common.base.KeySync;
import com.nuggylib.enchantmentsseer.common.base.PlayerState;
import com.nuggylib.enchantmentsseer.common.config.EnchantmentsSeerConfig;
import com.nuggylib.enchantmentsseer.common.config.EnchantmentsSeerModConfig;
import com.nuggylib.enchantmentsseer.common.lib.Version;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.TransmitterNetworkRegistry;
import com.nuggylib.enchantmentsseer.common.network.PacketHandler;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerBlocks;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerContainerTypes;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerItems;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerTileEntityTypes;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils.ResourceType;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.ForgeChunkManager;
import net.minecraftforge.event.world.WorldEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.InterModProcessEvent;
import net.minecraftforge.fml.event.server.FMLServerStoppedEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

/**
 * The main class for the Enchantments Seer mod
 *
 * @see "https://mcforge.readthedocs.io/en/1.16.x/concepts/registries/#deferredregister"
 */
@Mod("enchantments-seer")
public class EnchantmentsSeer
{
    public static final String MOD_ID = "enchantments-seer";
    public static final String MOD_NAME = "Enchantments Seer";
    public static final String LOG_TAG = '[' + MOD_NAME + ']';
    public static final Logger logger = LogManager.getLogger();
    public static final PlayerState playerState = new PlayerState();
    /**
     * EnchantmentsSeer Packet Pipeline
     */
    public static final PacketHandler packetHandler = new PacketHandler();
    /**
     * EnchantmentsSeer mod instance
     */
    public static EnchantmentsSeer instance;
    /**
     * EnchantmentsSeer version
     */
    public final Version versionNumber;
    /**
     * The server's world tick handler.
     */
    public static final CommonWorldTickHandler worldTickHandler = new CommonWorldTickHandler();

    public static final GameProfile gameProfile = new GameProfile(UUID.nameUUIDFromBytes("enchantments-seer.common".getBytes(StandardCharsets.UTF_8)), EnchantmentsSeer.LOG_TAG);


    // Items
    public static final KeySync keyMap = new KeySync();

    public EnchantmentsSeer() {
        instance = this;
        EnchantmentsSeerConfig.registerConfigs(ModLoadingContext.get());

        MinecraftForge.EVENT_BUS.addListener(this::onWorldLoad);
        MinecraftForge.EVENT_BUS.addListener(this::onWorldUnload);
        MinecraftForge.EVENT_BUS.addListener(this::serverStopped);
        EnchantmentsSeerItems.ITEMS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentsSeerBlocks.BLOCKS.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentsSeerTileEntityTypes.TILE_ENTITY_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        EnchantmentsSeerContainerTypes.CONTAINER_TYPES.register(FMLJavaModLoadingContext.get().getModEventBus());
        versionNumber = new Version(ModLoadingContext.get().getActiveContainer());

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::commonSetup);
        modEventBus.addListener(this::onConfigLoad);

    }

    private void commonSetup(FMLCommonSetupEvent event) {
        // TODO: See if we need to utilize capabilities for our mod
        // Capabilities.registerCapabilities();

        //Register player tracker
        MinecraftForge.EVENT_BUS.register(new CommonPlayerTracker());
        MinecraftForge.EVENT_BUS.register(new CommonPlayerTickHandler());
        MinecraftForge.EVENT_BUS.register(EnchantmentsSeer.worldTickHandler);

        //Initialization notification
        logger.info("Version {} initializing...", versionNumber);

        //Register with TransmitterNetworkRegistry
        TransmitterNetworkRegistry.initiate();

        //Packet registrations
        packetHandler.initialize();

        //Fake player info
        logger.info("Fake player readout: UUID = {}, name = {}", gameProfile.getId(), gameProfile.getName());

        //Completion notification
        logger.info("Loading complete.");

        //Success message
        logger.info("Mod loaded.");
    }

    private void onConfigLoad(ModConfig.ModConfigEvent configEvent) {
        //Note: We listen to both the initial load and the reload, so as to make sure that we fix any accidentally
        // cached values from calls before the initial loading
        ModConfig config = configEvent.getConfig();
        //Make sure it is for the same modid as us
        if (config.getModId().equals(MOD_ID) && config instanceof EnchantmentsSeerModConfig) {
            ((EnchantmentsSeerModConfig) config).clearCache();
        }
    }

    private void onWorldLoad(WorldEvent.Load event) {
        playerState.init(event.getWorld());
    }

    private void onWorldUnload(WorldEvent.Unload event) {
        // Make sure the global fake player drops its reference to the World
        // when the server shuts down
        if (event.getWorld() instanceof ServerWorld) {
            EnchantmentSeerFakePlayer.releaseInstance(event.getWorld());
        }
    }

    /**
     * Gets a ResourceLocation with a defined resource type and name.
     *
     * @param type - type of resource to retrieve
     * @param name - simple name of file to retrieve as a ResourceLocation
     *
     * @return the corresponding ResourceLocation
     */
    public static ResourceLocation getResource(ResourceType type, String name) {
        return rl(type.getPrefix() + name);
    }

    public static ResourceLocation rl(String path) {
        return new ResourceLocation(MOD_ID, path);
    }

    /**
     * Mekanism creative tab
     */
    public static final CreativeTabEnchantmentsSeer tabEnchantmentsSeer = new CreativeTabEnchantmentsSeer();

    private void serverStopped(FMLServerStoppedEvent event) {
        //Clear all cache data, wait until server stopper though so that we make sure saving can use any data it needs
        playerState.clear(false);

        //Reset consistent managers
        TransmitterNetworkRegistry.reset();
    }
}
