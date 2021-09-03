package com.nuggylib.enchantmentsseer.common.config;

import com.nuggylib.enchantmentsseer.common.config.value.CachedValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/config/IMekanismConfig.java"
 */
public interface IEnchantmentsSeerConfig {

    String getFileName();

    ForgeConfigSpec getConfigSpec();

    ModConfig.Type getConfigType();

    void clearCache();

    void addCachedValue(CachedValue<?> configValue);

    /**
     * Should this config be added to the mods "config" files. Make this return false to only create the config. This will allow it to be tracked, but not override the
     * value that has already been added to this mod's container. As the list is from config type to mod config.
     */
    default boolean addToContainer() {
        return true;
    }

}
