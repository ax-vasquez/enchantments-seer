package com.nuggylib.enchantmentsseer.common;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

public enum EnchantmentsSeerLang implements ILangEntry {
    //Colors
    COLOR_BLACK("color", "black"),
    COLOR_DARK_BLUE("color", "dark_blue"),
    COLOR_DARK_GREEN("color", "dark_green"),
    COLOR_DARK_AQUA("color", "dark_aqua"),
    COLOR_DARK_RED("color", "dark_red"),
    COLOR_PURPLE("color", "purple"),
    COLOR_ORANGE("color", "orange"),
    COLOR_GRAY("color", "gray"),
    COLOR_DARK_GRAY("color", "dark_gray"),
    COLOR_INDIGO("color", "indigo"),
    COLOR_BRIGHT_GREEN("color", "bright_green"),
    COLOR_AQUA("color", "aqua"),
    COLOR_RED("color", "red"),
    COLOR_PINK("color", "pink"),
    COLOR_YELLOW("color", "yellow"),
    COLOR_WHITE("color", "white"),
    COLOR_BROWN("color", "brown"),
    COLOR_BRIGHT_PINK("color", "bright_pink"),
    // other
    CLOSE("gui", "close"),;

    private final String key;

    EnchantmentsSeerLang(String type, String path) {
        this(Util.makeDescriptionId(type, new ResourceLocation(EnchantmentsSeer.MOD_ID, path)));
    }

    EnchantmentsSeerLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
