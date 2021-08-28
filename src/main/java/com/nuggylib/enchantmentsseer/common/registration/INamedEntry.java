package com.nuggylib.enchantmentsseer.common.registration;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/INamedEntry.java"
 */
public interface INamedEntry {

    /**
     * Used for retrieving the path/name of a registry object before the registry object has been fully initialized
     */
    String getInternalRegistryName();
}
