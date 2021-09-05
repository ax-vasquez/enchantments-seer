package com.nuggylib.enchantmentsseer.common.config.value;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.config.IEnchantmentsSeerConfig;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

import java.util.HashSet;
import java.util.Set;

/**
 * Shamelessly-copied from Mekanism; all credit goes to them
 *
 * @param <T>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/config/value/CachedValue.java"
 */
public abstract class CachedValue<T> {

    protected final ConfigValue<T> internal;
    private Set<IConfigValueInvalidationListener> invalidationListeners;

    protected CachedValue(IEnchantmentsSeerConfig config, ConfigValue<T> internal) {
        this.internal = internal;
        config.addCachedValue(this);
    }

    public boolean hasInvalidationListeners() {
        return invalidationListeners != null && !invalidationListeners.isEmpty();
    }

    public void addInvalidationListener(IConfigValueInvalidationListener listener) {
        if (invalidationListeners == null) {
            invalidationListeners = new HashSet<>();
        }
        if (!invalidationListeners.add(listener)) {
            EnchantmentsSeer.logger.warn("Duplicate invalidation listener added");
        }
    }

    public void removeInvalidationListener(IConfigValueInvalidationListener listener) {
        if (invalidationListeners == null) {
            EnchantmentsSeer.logger.warn("Unable to remove specified invalidation listener, no invalidation listeners have been added.");
        } else if (!invalidationListeners.remove(listener)) {
            EnchantmentsSeer.logger.warn("Unable to remove specified invalidation listener.");
        }
    }

    protected abstract boolean clearCachedValue(boolean checkChanged);

    public void clearCache() {
        if (clearCachedValue(hasInvalidationListeners())) {
            invalidationListeners.forEach(IConfigValueInvalidationListener::run);
        }
    }

    @FunctionalInterface
    public interface IConfigValueInvalidationListener extends Runnable {
        //Note: If we ever have any invalidation listeners that end up being lazy we can easily add a method to this
        // to specify it and then not bother regrabbing the value instantly
    }
}
