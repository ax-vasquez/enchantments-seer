package com.nuggylib.enchantmentsseer.client;

import com.nuggylib.enchantmentsseer.client.gui.SeersEnchantingTableScreen;
import com.nuggylib.enchantmentsseer.client.gui.block.GuiSeersEnchantingTable;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerContainerTypes;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EnchantmentsSeer.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ClientRegistration {

    @SubscribeEvent
    public static void init(FMLClientSetupEvent event) {

    }

    @SubscribeEvent
    public static void registerContainers(RegistryEvent.Register<ContainerType<?>> event) {
        ScreenManager.register(EnchantmentsSeerContainerTypes.SEERS_ENCHANTING_TABLE.get(), GuiSeersEnchantingTable::new);
    }

    @SubscribeEvent
    public static void registerModelLoaders(ModelRegistryEvent event) {

    }

    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {

    }

}
