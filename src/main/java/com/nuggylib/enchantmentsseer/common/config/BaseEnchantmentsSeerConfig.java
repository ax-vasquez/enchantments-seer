package com.nuggylib.enchantmentsseer.common.config;

import com.nuggylib.enchantmentsseer.common.config.value.CachedValue;

import java.util.ArrayList;
import java.util.List;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/config/BaseMekanismConfig.java"
 */
public abstract class BaseEnchantmentsSeerConfig implements IEnchantmentsSeerConfig {

    private final List<CachedValue<?>> cachedConfigValues = new ArrayList<>();

    @Override
    public void clearCache() {
        cachedConfigValues.forEach(CachedValue::clearCache);
    }

    @Override
    public void addCachedValue(CachedValue<?> configValue) {
        cachedConfigValues.add(configValue);
    }
}
