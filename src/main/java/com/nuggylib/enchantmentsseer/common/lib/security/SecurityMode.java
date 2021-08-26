package com.nuggylib.enchantmentsseer.common.lib.security;

import com.nuggylib.enchantmentsseer.api.math.MathUtils;
import com.nuggylib.enchantmentsseer.client.render.EnumColor;
import com.nuggylib.enchantmentsseer.client.render.IHasTextComponent;
import com.nuggylib.enchantmentsseer.client.render.IIncrementalEnum;
import com.nuggylib.enchantmentsseer.client.render.ILangEntry;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/security/SecurityMode.java"
 */
public enum SecurityMode implements IIncrementalEnum<SecurityMode>, IHasTextComponent {
    PUBLIC(EnchantmentsSeerLang.PUBLIC, EnumColor.BRIGHT_GREEN),
    PRIVATE(EnchantmentsSeerLang.PRIVATE, EnumColor.RED),
    TRUSTED(EnchantmentsSeerLang.TRUSTED, EnumColor.INDIGO);

    private static final SecurityMode[] MODES = values();

    private final ILangEntry langEntry;
    private final EnumColor color;

    SecurityMode(ILangEntry langEntry, EnumColor color) {
        this.langEntry = langEntry;
        this.color = color;
    }

    @Override
    public ITextComponent getTextComponent() {
        return langEntry.translateColored(color);
    }

    @Nonnull
    @Override
    public SecurityMode byIndex(int index) {
        return byIndexStatic(index);
    }

    public static SecurityMode byIndexStatic(int index) {
        return MathUtils.getByIndexMod(MODES, index);
    }
}
