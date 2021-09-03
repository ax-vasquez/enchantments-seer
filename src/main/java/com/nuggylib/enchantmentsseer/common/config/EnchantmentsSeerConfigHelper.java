package com.nuggylib.enchantmentsseer.common.config;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

/**
 * Shamelessly-copied from Mekanism; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/config/MekanismConfigHelper.java"
 */
public class EnchantmentsSeerConfigHelper {

    private EnchantmentsSeerConfigHelper() {}

    public static final Path CONFIG_DIR;

    static {
        CONFIG_DIR = FMLPaths.getOrCreateGameRelativePath(FMLPaths.CONFIGDIR.get().resolve(EnchantmentsSeer.MOD_NAME), EnchantmentsSeer.MOD_NAME);
    }

    /**
     * Creates a mod config so that {@link net.minecraftforge.fml.config.ConfigTracker} will track it and sync server configs from server to client.
     */
    public static void registerConfig(ModContainer modContainer, IEnchantmentsSeerConfig config) {
        EnchantmentsSeerModConfig modConfig = new EnchantmentsSeerModConfig(modContainer, config);
        if (config.addToContainer()) {
            modContainer.addConfig(modConfig);
        }
    }
}
