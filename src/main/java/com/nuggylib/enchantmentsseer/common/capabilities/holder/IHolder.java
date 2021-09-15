package com.nuggylib.enchantmentsseer.common.capabilities.holder;

import net.minecraftforge.common.capabilities.Capability;

/**
 * Inspired by Mekanism
 *
 * Not all {@link Capability} types that Mekanism has are intended to "hold" something (such as items, storage, or energy),
 * <b>but the ones that do should implement this interface</b>.
 *
 * <h2>How Mekanism uses this interface</h2>
 * Aside from our use case (with items), Mekanism has custom implementations to support a variety of custom use cases.
 * Know that <b>we do not need any of these use cases; they are merely examples to help better-understand this interface.</b>
 * <ul>
 *     <li>Chemical and fluid tanks</li>
 *     <li>Energy containers</li>
 *     <li>Heat capacitor</li>
 * </ul>
 */
public interface IHolder {

    default boolean canInsert() {
        return true;
    }

    default boolean canExtract() {
        return true;
    }

}
