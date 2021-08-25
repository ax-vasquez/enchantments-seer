package com.nuggylib.enchantmentsseer.client.render;

import net.minecraft.util.text.ITextComponent;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/text/IHasTextComponent.java"
 */
public interface IHasTextComponent {

    /**
     * Gets the text component that represents this object.
     */
    ITextComponent getTextComponent();
}
