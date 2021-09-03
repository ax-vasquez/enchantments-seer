package com.nuggylib.enchantmentsseer.common.util;

public class EnchantmentsSeerUtils {

    /**
     * Copied from the Mekanism codebase; all credit goes to them
     *
     * This enum was pulled from within the `MekanismUtils` class
     *
     * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/util/MekanismUtils.java"
     */
    public enum ResourceType {
        // TODO: Figure out how Mekanism included the "texture" portion of the path without manually adding it here
        GUI("textures/gui"),
        GUI_BUTTON("gui/button"),
        GUI_BAR("gui/bar"),
        GUI_HUD("gui/hud"),
        GUI_PROGRESS("gui/progress"),
        GUI_SLOT("gui/slot"),
        GUI_TAB("gui/tabs"),
        SOUND("sound"),
        RENDER("render"),
        TEXTURE_BLOCKS("textures/block"),
        TEXTURE_ITEMS("textures/item"),
        MODEL("models")
        ;

        private final String prefix;

        ResourceType(String s) {
            prefix = s;
        }

        public String getPrefix() {
            return prefix + "/";
        }
    }

}
