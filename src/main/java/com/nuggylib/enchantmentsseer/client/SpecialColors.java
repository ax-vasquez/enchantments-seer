package com.nuggylib.enchantmentsseer.client;

import com.nuggylib.enchantmentsseer.client.render.lib.ColorAtlas;
import com.nuggylib.enchantmentsseer.client.render.lib.ColorAtlas.ColorRegistryObject;

/**
 * Inspired by Mekanism
 *
 * This class contains registry definitions for reusable colors
 */
public class SpecialColors {

    private SpecialColors() {
    }

    public static final ColorAtlas GUI_OBJECTS = new ColorAtlas("gui_objects");
    public static final ColorAtlas GUI_TEXT = new ColorAtlas("gui_text");

    public static final ColorRegistryObject TEXT_TITLE = GUI_TEXT.register(0xFF404040);
    public static final ColorRegistryObject TEXT_HEADING = GUI_TEXT.register(0xFF202020);
    public static final ColorRegistryObject TEXT_SUBHEADING = GUI_TEXT.register(0xFF787878);
    public static final ColorRegistryObject TEXT_SCREEN = GUI_TEXT.register(0xFF3CFE9A);

}
