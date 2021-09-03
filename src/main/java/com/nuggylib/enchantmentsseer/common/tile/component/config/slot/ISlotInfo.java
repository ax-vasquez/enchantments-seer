package com.nuggylib.enchantmentsseer.common.tile.component.config.slot;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/component/config/slot/ISlotInfo.java"
 */
public interface ISlotInfo {

    boolean canInput();

    boolean canOutput();

    default boolean isEnabled() {
        return canInput() || canOutput();
    }
}
