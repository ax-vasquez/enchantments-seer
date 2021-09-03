package com.nuggylib.enchantmentsseer.client.gui.element.slot;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils.ResourceType;
import net.minecraft.util.ResourceLocation;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/element/slot/SlotType.java"
 */
public enum SlotType {
    NORMAL("normal.png", 18, 18);

    private final ResourceLocation texture;
    private final int width;
    private final int height;

    SlotType(String texture, int width, int height) {
        this.texture = EnchantmentsSeer.getResource(ResourceType.GUI_SLOT, texture);
        this.width = width;
        this.height = height;
    }

    public ResourceLocation getTexture() {
        return texture;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

}
