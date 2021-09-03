package com.nuggylib.enchantmentsseer.client;

import com.nuggylib.enchantmentsseer.common.registration.impl.ContainerTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.registration.impl.TileEntityTypeRegistryObject;
import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.inventory.container.Container;
import net.minecraft.tileentity.TileEntity;
import net.minecraftforge.fml.client.registry.ClientRegistry;

import java.util.function.Function;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/ClientRegistrationUtil.java"
 */
public class ClientRegistrationUtil {

    private ClientRegistrationUtil() {
    }

    public static <C extends Container, U extends Screen & IHasContainer<C>> void registerScreen(ContainerTypeRegistryObject<C> type, ScreenManager.IScreenFactory<C, U> factory) {
        ScreenManager.register(type.getContainerType(), factory);
    }

    public static synchronized <T extends TileEntity> void bindTileEntityRenderer(TileEntityTypeRegistryObject<T> tileTypeRO,
                                                                                  Function<TileEntityRendererDispatcher, TileEntityRenderer<? super T>> renderFactory) {
        ClientRegistry.bindTileEntityRenderer(tileTypeRO.getTileEntityType(), renderFactory);
    }
}
