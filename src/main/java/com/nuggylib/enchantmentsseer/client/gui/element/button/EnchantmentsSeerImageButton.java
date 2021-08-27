package com.nuggylib.enchantmentsseer.client.gui.element.button;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.gui.IGuiWrapper;
import com.nuggylib.enchantmentsseer.client.gui.element.GuiElement;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;

import javax.annotation.Nonnull;

public class EnchantmentsSeerImageButton extends EnchantmentsSeerButton {

    private final ResourceLocation resourceLocation;
    private final int textureWidth;
    private final int textureHeight;

    public EnchantmentsSeerImageButton(IGuiWrapper gui, int x, int y, int size, ResourceLocation resource, Runnable onPress) {
        this(gui, x, y, size, size, resource, onPress);
    }

    public EnchantmentsSeerImageButton(IGuiWrapper gui, int x, int y, int size, ResourceLocation resource, Runnable onPress, GuiElement.IHoverable onHover) {
        this(gui, x, y, size, size, resource, onPress, onHover);
    }

    public EnchantmentsSeerImageButton(IGuiWrapper gui, int x, int y, int size, int textureSize, ResourceLocation resource, Runnable onPress) {
        this(gui, x, y, size, textureSize, resource, onPress, null);
    }

    public EnchantmentsSeerImageButton(IGuiWrapper gui, int x, int y, int size, int textureSize, ResourceLocation resource, Runnable onPress, GuiElement.IHoverable onHover) {
        this(gui, x, y, size, size, textureSize, textureSize, resource, onPress, onHover);
    }

    public EnchantmentsSeerImageButton(IGuiWrapper gui, int x, int y, int width, int height, int textureWidth, int textureHeight, ResourceLocation resource, Runnable onPress) {
        this(gui, x, y, width, height, textureWidth, textureHeight, resource, onPress, null);
    }

    public EnchantmentsSeerImageButton(IGuiWrapper gui, int x, int y, int width, int height, int textureWidth, int textureHeight, ResourceLocation resource, Runnable onPress, GuiElement.IHoverable onHover) {
        super(gui, x, y, width, height, StringTextComponent.EMPTY, onPress, onHover);
        this.resourceLocation = resource;
        this.textureWidth = textureWidth;
        this.textureHeight = textureHeight;
    }

    @Override
    public void drawBackground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.drawBackground(matrix, mouseX, mouseY, partialTicks);
        EnchantmentsSeerRenderer.bindTexture(getResource());
        blit(matrix, x, y, width, height, 0, 0, textureWidth, textureHeight, textureWidth, textureHeight);
    }

    protected ResourceLocation getResource() {
        return resourceLocation;
    }
}
