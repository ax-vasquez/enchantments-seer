package com.nuggylib.enchantmentsseer.common.registration.impl;

import com.nuggylib.enchantmentsseer.common.registration.WrappedRegistryObject;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <CONTAINER>
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/impl/ContainerTypeRegistryObject.java"
 */
public class ContainerTypeRegistryObject<CONTAINER extends Container> extends WrappedRegistryObject<ContainerType<CONTAINER>> {

    public ContainerTypeRegistryObject(RegistryObject<ContainerType<CONTAINER>> registryObject) {
        super(registryObject);
    }

    @Nonnull
    public ContainerType<CONTAINER> getContainerType() {
        return get();
    }

    //Internal use only overwrite the registry object
    ContainerTypeRegistryObject<CONTAINER> setRegistryObject(RegistryObject<ContainerType<CONTAINER>> registryObject) {
        this.registryObject = registryObject;
        return this;
    }
}
