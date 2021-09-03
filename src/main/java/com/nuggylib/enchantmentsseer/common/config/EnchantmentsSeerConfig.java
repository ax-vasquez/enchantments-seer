package com.nuggylib.enchantmentsseer.common.config;

import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.ModLoadingContext;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/config/MekanismConfig.java"
 */
public class EnchantmentsSeerConfig {

    private EnchantmentsSeerConfig() {}

    public static final GeneralConfig general = new GeneralConfig();

    public static void registerConfigs(ModLoadingContext modLoadingContext) {
        ModContainer modContainer = modLoadingContext.getActiveContainer();
        EnchantmentsSeerConfigHelper.registerConfig(modContainer, general);
    }

}
