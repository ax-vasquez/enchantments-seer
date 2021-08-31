package com.nuggylib.enchantmentsseer.api;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/IContentsListener.java"
 */
@FunctionalInterface
public interface IContentsListener {

    /**
     * Called when the contents this listener is monitoring gets changed.
     */
    void onContentsChanged();
}
