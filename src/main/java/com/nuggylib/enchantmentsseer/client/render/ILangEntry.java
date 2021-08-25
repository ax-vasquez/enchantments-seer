package com.nuggylib.enchantmentsseer.client.render;

import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.TranslationTextComponent;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/text/ILangEntry.java"
 */
public interface ILangEntry extends IHasTranslationKey {

    default TranslationTextComponent translate(Object... args) {
        return TextComponentUtil.smartTranslate(getTranslationKey(), args);
    }

    default IFormattableTextComponent translateColored(EnumColor color, Object... args) {
        return TextComponentUtil.build(color, translate(args));
    }
}
