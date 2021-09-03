package com.nuggylib.enchantmentsseer.common.content.network.transmitter;

import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/transmitter/INetworkDataHandler.java"
 */
public interface INetworkDataHandler {

    @Nullable
    default ITextComponent getNeededInfo() {
        return null;
    }

    @Nullable
    default ITextComponent getStoredInfo() {
        return null;
    }

    @Nullable
    default ITextComponent getFlowInfo() {
        return null;
    }

    @Nullable
    default Object getNetworkReaderCapacity() {
        return null;
    }
}
