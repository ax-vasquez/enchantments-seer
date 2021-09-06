package com.nuggylib.enchantmentsseer.client;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.gui.block.GuiSeersEnchantingTable;
import com.nuggylib.enchantmentsseer.client.render.tileentity.RenderSeersEnchantingTable;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerContainerTypes;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerTileEntityTypes;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

/**
 * Inspired by Mekanism code
 *
 * This class contains all registration logic for client-side only objects.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/ClientRegistration.java"
 */
@Mod.EventBusSubscriber(modid = EnchantmentsSeer.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {
        ClientRegistrationUtil.bindTileEntityRenderer(EnchantmentsSeerTileEntityTypes.SEERS_ENCHANTING_TABLE, RenderSeersEnchantingTable::new);
    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {

        ClientRegistrationUtil.registerScreen(EnchantmentsSeerContainerTypes.SEERS_ENCHANTING_TABLE, GuiSeersEnchantingTable::new);
    }

    @SubscribeEvent
    public static void registerModelLoaders(ModelRegistryEvent event) {
        // TODO: See what we may need to do here
    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {
        // TODO: See what we may need to do here (and also why Mekanism uses this event in both this class and their variant of EnchantmentsSeerRenderer)
    }

}
