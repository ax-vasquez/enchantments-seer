package com.nuggylib.enchantmentsseer.common.capabilities.holder;

import net.minecraftforge.common.capabilities.Capability;

/**
 * Inspired by Mekanism
 *
 * Not all {@link Capability} types that Mekanism has are intended to "hold" something (such as items, storage, or energy),
 * <b>but the ones that do should implement this interface</b>.
 */
public interface IHolder {

    default boolean canInsert() {
        return true;
    }

    default boolean canExtract() {
        return true;
    }

}
