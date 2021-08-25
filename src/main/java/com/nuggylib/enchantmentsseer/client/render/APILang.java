package com.nuggylib.enchantmentsseer.client.render;

import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Util;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/text/APILang.java"
 */
public enum APILang implements ILangEntry {
    //Generic
    @Deprecated//TODO - 1.17: Remove this generic lang entry in favor of just having string text components be used
            GENERIC("generic", "format"),
    //Directions
    DOWN("direction", "down"),
    UP("direction", "up"),
    NORTH("direction", "north"),
    SOUTH("direction", "south"),
    WEST("direction", "west"),
    EAST("direction", "east"),
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
    COLOR_BRIGHT_PINK("color", "bright_pink");
    //Chemical Attributes

    private final String key;

    APILang(String type, String path) {
        this(Util.makeDescriptionId(type, new ResourceLocation(EnchantmentsSeer.MOD_ID, path)));
    }

    APILang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
