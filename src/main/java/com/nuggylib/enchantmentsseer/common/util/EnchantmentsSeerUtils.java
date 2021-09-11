package com.nuggylib.enchantmentsseer.common.util;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import net.minecraft.util.ResourceLocation;

/**
 * Utility class inspired by Mekanism
 */
public class EnchantmentsSeerUtils {

    /**
     * Gets a ResourceLocation with a defined resource type and name.
     *
     * @param type - type of resource to retrieve
     * @param name - simple name of file to retrieve as a ResourceLocation
     *
     * @return the corresponding ResourceLocation
     */
    public static ResourceLocation getResource(ResourceType type, String name) {
        return EnchantmentsSeer.rl(type.getPrefix() + name);
    }

    public enum ResourceType {
        GUI("textures/gui"),
        GUI_BUTTON("textures/gui/button"),
        GUI_BAR("textures/gui/bar"),
        GUI_HUD("textures/gui/hud"),
        GUI_GAUGE("textures/gui/gauge"),
        GUI_PROGRESS("textures/gui/progress"),
        GUI_SLOT("textures/gui/slot"),
        GUI_TAB("textures/gui/tabs"),
        SOUND("sound"),
        RENDER("render"),
        TEXTURE_BLOCKS("textures/block"),
        TEXTURE_ITEMS("textures/item"),
        MODEL("models");

        private final String prefix;

        ResourceType(String s) {
            prefix = s;
        }

        public String getPrefix() {
            return prefix + "/";
        }
    }

}
