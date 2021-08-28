package com.nuggylib.enchantmentsseer.client.gui.element.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.gui.IGuiWrapper;
import com.nuggylib.enchantmentsseer.client.gui.element.window.GuiWindow;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils.ResourceType;

import javax.annotation.Nonnull;

public class GuiCloseButton extends EnchantmentsSeerImageButton {

    public GuiCloseButton(IGuiWrapper gui, int x, int y, GuiWindow window) {
        super(gui, x, y, 8, EnchantmentsSeer.getResource(ResourceType.GUI_BUTTON, "close.png"), window::close);
    }

    @Override
    public void renderToolTip(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        displayTooltip(matrix, EnchantmentsSeerLang.CLOSE.translate(), mouseX, mouseY);
    }

    @Override
    public boolean resetColorBeforeRender() {
        return false;
    }
}
