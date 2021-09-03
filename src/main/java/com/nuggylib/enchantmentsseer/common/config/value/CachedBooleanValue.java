package com.nuggylib.enchantmentsseer.common.config.value;

import com.nuggylib.enchantmentsseer.common.config.IEnchantmentsSeerConfig;
import net.minecraftforge.common.ForgeConfigSpec.ConfigValue;

import java.util.function.BooleanSupplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/config/value/CachedBooleanValue.java"
 */
public class CachedBooleanValue extends CachedValue<Boolean> implements BooleanSupplier {

    private boolean resolved;
    private boolean cachedValue;

    private CachedBooleanValue(IEnchantmentsSeerConfig config, ConfigValue<Boolean> internal) {
        super(config, internal);
    }

    public static CachedBooleanValue wrap(IEnchantmentsSeerConfig config, ConfigValue<Boolean> internal) {
        return new CachedBooleanValue(config, internal);
    }

    public boolean get() {
        if (!resolved) {
            //If we don't have a cached value or need to resolve it again, get it from the actual ConfigValue
            cachedValue = internal.get();
            resolved = true;
        }
        return cachedValue;
    }

    @Override
    public boolean getAsBoolean() {
        return get();
    }

    public void set(boolean value) {
        internal.set(value);
        cachedValue = value;
    }

    @Override
    protected boolean clearCachedValue(boolean checkChanged) {
        if (!resolved) {
            //Isn't cached don't need to clear it or run any invalidation listeners
            return false;
        }
        boolean oldCachedValue = cachedValue;
        resolved = false;
        //Return if we are meant to check the changed ones, and it is different than it used to be
        return checkChanged && oldCachedValue != get();
    }
}
