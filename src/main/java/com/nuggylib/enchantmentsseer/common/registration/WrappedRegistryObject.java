package com.nuggylib.enchantmentsseer.common.registration;

import com.nuggylib.enchantmentsseer.api.annotations.FieldsAreNonnullByDefault;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.IForgeRegistryEntry;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <T>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/WrappedRegistryObject.java"
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
public class WrappedRegistryObject<T extends IForgeRegistryEntry<? super T>> implements Supplier<T>, INamedEntry {

    protected RegistryObject<T> registryObject;

    protected WrappedRegistryObject(RegistryObject<T> registryObject) {
        this.registryObject = registryObject;
    }

    @Nonnull
    @Override
    public T get() {
        return registryObject.get();
    }

    @Override
    public String getInternalRegistryName() {
        return registryObject.getId().getPath();
    }
}
