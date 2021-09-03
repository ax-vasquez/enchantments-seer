package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils.ResourceType;
import net.minecraft.util.ResourceLocation;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/SlotOverlay.java"
 */
public enum SlotOverlay {
    MINUS("overlay_minus.png", 18, 18),
    PLUS("overlay_plus.png", 18, 18),
    INPUT("overlay_input.png", 18, 18),
    CHECK("overlay_check.png", 18, 18),
    X("overlay_x.png", 18, 18),
    FORMULA("overlay_formula.png", 18, 18),
    SELECT("overlay_select.png", 18, 18),
    MODULE("overlay_module.png", 18, 18);

    private final ResourceLocation texture;
    private final int width;
    private final int height;

    SlotOverlay(String texture, int width, int height) {
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
