package com.nuggylib.enchantmentsseer.client.render.text;

import net.minecraft.util.text.ITextComponent;

/**
 * A simple interface to indicate that a class that implements this interface has a corresponding text component.
 *
 * Sourced from the Mekanism code
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/text/IHasTextComponent.java"
 */
public interface IHasTextComponent {

    /**
     * Gets the text component that represents this object.
     */
    ITextComponent getTextComponent();
}
