package com.nuggylib.enchantmentsseer.common.tile.component.config;

import com.nuggylib.enchantmentsseer.client.render.text.EnumColor;
import com.nuggylib.enchantmentsseer.client.render.text.IHasTranslationKey;
import com.nuggylib.enchantmentsseer.client.render.text.ILangEntry;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import com.nuggylib.enchantmentsseer.api.IIncrementalEnum;
import com.nuggylib.enchantmentsseer.api.math.MathUtils;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/component/config/DataType.java"
 */
public enum DataType implements IIncrementalEnum<DataType>, IHasTranslationKey {
    NONE(EnchantmentsSeerLang.SIDE_DATA_NONE, EnumColor.GRAY);

    private static final DataType[] TYPES = values();
    private final EnumColor color;
    private final ILangEntry langEntry;

    DataType(ILangEntry langEntry, EnumColor color) {
        this.color = color;
        this.langEntry = langEntry;
    }

    public EnumColor getColor() {
        return color;
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }

    @Nonnull
    @Override
    public DataType byIndex(int index) {
        return byIndexStatic(index);
    }

    public static DataType byIndexStatic(int index) {
        return MathUtils.getByIndexMod(TYPES, index);
    }
}
