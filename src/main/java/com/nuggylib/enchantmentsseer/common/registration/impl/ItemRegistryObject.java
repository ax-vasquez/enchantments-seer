package com.nuggylib.enchantmentsseer.common.registration.impl;

import com.nuggylib.enchantmentsseer.api.providers.IItemProvider;
import com.nuggylib.enchantmentsseer.common.registration.WrappedRegistryObject;
import net.minecraft.item.Item;
import net.minecraftforge.fml.RegistryObject;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/impl/ItemRegistryObject.java"
 */
public class ItemRegistryObject<ITEM extends Item> extends WrappedRegistryObject<ITEM> implements IItemProvider {

    public ItemRegistryObject(RegistryObject<ITEM> registryObject) {
        super(registryObject);
    }

    @Nonnull
    @Override
    public ITEM getItem() {
        return get();
    }
}
