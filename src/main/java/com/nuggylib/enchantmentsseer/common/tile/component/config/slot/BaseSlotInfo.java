package com.nuggylib.enchantmentsseer.common.tile.component.config.slot;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/component/config/slot/BaseSlotInfo.java"
 */
public abstract class BaseSlotInfo implements ISlotInfo {

    private final boolean canInput;
    private final boolean canOutput;

    protected BaseSlotInfo(boolean canInput, boolean canOutput) {
        this.canInput = canInput;
        this.canOutput = canOutput;
    }

    @Override
    public boolean canInput() {
        return canInput;
    }

    @Override
    public boolean canOutput() {
        return canOutput;
    }
}
