package com.nuggylib.enchantmentsseer.client.render.text;

/**
 * A simple interface to indicate that a class that implements this interface has a corresponding text component.
 *
 * Sourced from the Mekanism code
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/text/IHasTranslationKey.java"
 */
public interface IHasTranslationKey {

    /**
     * Gets the translation key for this object.
     */
    String getTranslationKey();
}
