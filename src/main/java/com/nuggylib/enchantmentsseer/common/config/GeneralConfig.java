package com.nuggylib.enchantmentsseer.common.config;

import com.nuggylib.enchantmentsseer.common.config.value.CachedBooleanValue;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig.Type;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/config/GeneralConfig.java"
 */
public class GeneralConfig extends BaseEnchantmentsSeerConfig {

    private final ForgeConfigSpec configSpec;

    public final CachedBooleanValue logPackets;

    GeneralConfig() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        builder.comment("General Config. This config is synced from server to client.").push("general");

        logPackets = CachedBooleanValue.wrap(this, builder.comment("Log Enchantments Seer packet names. Debug setting.")
                .define("logPackets", false));

        configSpec = builder.build();
    }

    @Override
    public String getFileName() {
        return "general";
    }

    @Override
    public ForgeConfigSpec getConfigSpec() {
        return configSpec;
    }

    @Override
    public Type getConfigType() {
        return Type.SERVER;
    }
}
