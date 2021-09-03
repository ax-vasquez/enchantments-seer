package com.nuggylib.enchantmentsseer.common.lib.transmitter;

import com.nuggylib.enchantmentsseer.api.IIncrementalEnum;
import com.nuggylib.enchantmentsseer.api.math.MathUtils;
import com.nuggylib.enchantmentsseer.client.render.text.IHasTranslationKey;
import com.nuggylib.enchantmentsseer.client.render.text.ILangEntry;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import net.minecraft.util.IStringSerializable;

import javax.annotation.Nonnull;
import java.util.Locale;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/transmitter/ConnectionType.java"
 */
public enum ConnectionType implements IIncrementalEnum<ConnectionType>, IStringSerializable, IHasTranslationKey {
    NORMAL(EnchantmentsSeerLang.CONNECTION_NORMAL),
    PUSH(EnchantmentsSeerLang.CONNECTION_PUSH),
    PULL(EnchantmentsSeerLang.CONNECTION_PULL),
    NONE(EnchantmentsSeerLang.CONNECTION_NONE);

    private static final ConnectionType[] TYPES = values();
    private final ILangEntry langEntry;

    ConnectionType(ILangEntry langEntry) {
        this.langEntry = langEntry;
    }

    @Override
    public String getSerializedName() {
        return name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }

    @Nonnull
    @Override
    public ConnectionType byIndex(int index) {
        return byIndexStatic(index);
    }

    public static ConnectionType byIndexStatic(int index) {
        return MathUtils.getByIndexMod(TYPES, index);
    }
}
