package com.nuggylib.enchantmentsseer.common.capabilities.holder;

import net.minecraft.util.Direction;

import javax.annotation.Nullable;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/holder/IHolder.java"
 */
public interface IHolder {

    default boolean canInsert(@Nullable Direction direction) {
        return true;
    }

    default boolean canExtract(@Nullable Direction direction) {
        return true;
    }
}
