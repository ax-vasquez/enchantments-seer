package com.nuggylib.enchantmentsseer.common.config;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import net.minecraftforge.fml.ModContainer;
import net.minecraftforge.fml.config.ConfigFileTypeHandler;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;
import java.util.function.Function;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/config/MekanismModConfig.java"
 */
public class EnchantmentsSeerModConfig extends ModConfig {

    private static final EnchantmentsSeerConfigFileTypeHandler ENCH_SEER_TOML = new EnchantmentsSeerConfigFileTypeHandler();

    private final IEnchantmentsSeerConfig enchantmentsSeerConfig;

    public EnchantmentsSeerModConfig(ModContainer container, IEnchantmentsSeerConfig config) {
        super(config.getConfigType(), config.getConfigSpec(), container, EnchantmentsSeer.MOD_NAME + "/" + config.getFileName() + ".toml");
        this.enchantmentsSeerConfig = config;
    }

    @Override
    public ConfigFileTypeHandler getHandler() {
        return ENCH_SEER_TOML;
    }

    public void clearCache() {
        enchantmentsSeerConfig.clearCache();
    }

    private static class EnchantmentsSeerConfigFileTypeHandler extends ConfigFileTypeHandler {

        private static Path getPath(Path configBasePath) {
            //Intercept server config path reading for Mekanism configs and reroute it to the normal config directory
            if (configBasePath.endsWith("serverconfig")) {
                return FMLPaths.CONFIGDIR.get();
            }
            return configBasePath;
        }

        @Override
        public Function<ModConfig, CommentedFileConfig> reader(Path configBasePath) {
            return super.reader(getPath(configBasePath));
        }

        @Override
        public void unload(Path configBasePath, ModConfig config) {
            super.unload(getPath(configBasePath), config);
        }
    }

}
