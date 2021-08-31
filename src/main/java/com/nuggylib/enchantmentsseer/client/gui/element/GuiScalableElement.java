package com.nuggylib.enchantmentsseer.client.gui.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.gui.IGuiWrapper;
import net.minecraft.util.ResourceLocation;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/element/GuiScalableElement.java"
 */
public abstract class GuiScalableElement extends GuiTexturedElement {

    protected final int sideWidth;
    protected final int sideHeight;

    protected GuiScalableElement(ResourceLocation resource, IGuiWrapper gui, int x, int y, int width, int height, int sideWidth, int sideHeight) {
        super(resource, gui, x, y, width, height);
        active = false;
        this.sideWidth = sideWidth;
        this.sideHeight = sideHeight;
    }

    @Override
    public void drawBackground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(matrix, mouseX, mouseY, partialTicks);
        renderBackgroundTexture(matrix, getResource(), sideWidth, sideHeight);
    }
}
