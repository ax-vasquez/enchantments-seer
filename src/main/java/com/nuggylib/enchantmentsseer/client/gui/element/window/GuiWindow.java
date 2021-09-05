package com.nuggylib.enchantmentsseer.client.gui.element.window;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nuggylib.enchantmentsseer.client.gui.GuiEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.gui.GuiUtils;
import com.nuggylib.enchantmentsseer.client.gui.IGuiWrapper;
import com.nuggylib.enchantmentsseer.client.gui.element.GuiElement;
import com.nuggylib.enchantmentsseer.client.gui.element.GuiTexturedElement;
import com.nuggylib.enchantmentsseer.client.gui.element.button.GuiCloseButton;
import com.nuggylib.enchantmentsseer.client.render.Color;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer;
import com.nuggylib.enchantmentsseer.common.inventory.container.IEmptyContainer;
import com.nuggylib.enchantmentsseer.common.inventory.container.SelectedWindowData;
import net.minecraft.inventory.container.Container;
import org.lwjgl.glfw.GLFW;

import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from Mekanism; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/element/window/GuiWindow.java"
 */
public class GuiWindow extends GuiTexturedElement {

    private static final Color OVERLAY_COLOR = Color.rgbai(60, 60, 60, 128);

    private final SelectedWindowData windowData;
    private boolean dragging = false;
    private double dragX, dragY;
    private int prevDX, prevDY;

    private Consumer<GuiWindow> closeListener;
    private Consumer<GuiWindow> reattachListener;

    protected InteractionStrategy interactionStrategy = InteractionStrategy.CONTAINER;

    public GuiWindow(IGuiWrapper gui, int x, int y, int width, int height, SelectedWindowData windowData) {
        super(GuiEnchantmentsSeer.BASE_BACKGROUND, gui, x, y, width, height);
        this.windowData = windowData;
        isOverlay = true;
        active = true;
        if (!isFocusOverlay()) {
            addCloseButton();
        }
    }

    public void onFocusLost() {
    }

    public void onFocused() {
        gui().setSelectedWindow(windowData);
    }

    protected void addCloseButton() {
        addChild(new GuiCloseButton(gui(), this.x + 6, this.y + 6, this));
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        boolean ret = super.mouseClicked(mouseX, mouseY, button);
        // drag 'safe area'
        if (isMouseOver(mouseX, mouseY)) {
            if (mouseY < y + 18) {
                dragging = true;
                dragX = mouseX;
                dragY = mouseY;
                prevDX = 0;
                prevDY = 0;
            }
        } else if (!ret && interactionStrategy.allowContainer()) {
            if (gui() instanceof GuiEnchantmentsSeer) {
                Container c = ((GuiEnchantmentsSeer<?>) gui()).getMenu();
                // This is a somewhat unnecessary check we have - Mekanism has this check in place since they have more block types - we can keep it
                // just so we have the option to more-heavily integrate with Mekanism later (and want to continue using their design patterns)
                if (!(c instanceof IEmptyContainer)) {
                    // allow interaction with slots
                    if (mouseX >= getGuiLeft() && mouseX < getGuiLeft() + getGuiWidth() && mouseY >= getGuiTop() + getGuiHeight() - 90) {
                        return false;
                    }
                }
            }
        }
        // always return true to prevent background clicking
        return ret || !interactionStrategy.allowAll();
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double mouseXOld, double mouseYOld) {
        super.onDrag(mouseX, mouseY, mouseXOld, mouseYOld);
        if (dragging) {
            int newDX = (int) Math.round(mouseX - dragX), newDY = (int) Math.round(mouseY - dragY);
            int changeX = Math.max(-x, Math.min(minecraft.getWindow().getGuiScaledWidth() - (x + width), newDX - prevDX));
            int changeY = Math.max(-y, Math.min(minecraft.getWindow().getGuiScaledHeight() - (y + height), newDY - prevDY));
            prevDX = newDX;
            prevDY = newDY;
            move(changeX, changeY);
        }
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
        dragging = false;
    }

    @Override
    public void renderBackgroundOverlay(MatrixStack matrix, int mouseX, int mouseY) {
        if (isFocusOverlay()) {
            EnchantmentsSeerRenderer.renderColorOverlay(matrix, 0, 0, minecraft.getWindow().getGuiScaledWidth(), minecraft.getWindow().getGuiScaledHeight(), OVERLAY_COLOR.rgba());
        } else {
            RenderSystem.color4f(1, 1, 1, 0.75F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            GuiUtils.renderBackgroundTexture(matrix, GuiEnchantmentsSeer.SHADOW, 4, 4, getButtonX() - 3, getButtonY() - 3, getButtonWidth() + 6, getButtonHeight() + 6, 256, 256);
            EnchantmentsSeerRenderer.resetColor();
        }
        minecraft.textureManager.bind(getResource());
        renderBackgroundTexture(matrix, getResource(), 4, 4);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        if (super.keyPressed(keyCode, scanCode, modifiers)) {
            return true;
        }
        if (keyCode == GLFW.GLFW_KEY_ESCAPE) {
            close();
            return true;
        }
        return false;
    }

    public void setListenerTab(Supplier<? extends GuiElement> elementSupplier) {
        setTabListeners(window -> elementSupplier.get().active = true, window -> elementSupplier.get().active = false);
    }

    public void setTabListeners(Consumer<GuiWindow> closeListener, Consumer<GuiWindow> reattachListener) {
        this.closeListener = closeListener;
        this.reattachListener = reattachListener;
    }

    @Override
    public void resize(int prevLeft, int prevTop, int left, int top) {
        super.resize(prevLeft, prevTop, left, top);
        if (reattachListener != null) {
            reattachListener.accept(this);
        }
    }

    public void renderBlur(MatrixStack matrix) {
        RenderSystem.color4f(1, 1, 1, 0.3F);
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        GuiUtils.renderBackgroundTexture(matrix, GuiEnchantmentsSeer.BLUR, 4, 4, relativeX, relativeY, width, height, 256, 256);
        EnchantmentsSeerRenderer.resetColor();
    }

    public void close() {
        gui().removeWindow(this);
        children.forEach(GuiElement::onWindowClose);
        if (closeListener != null) {
            closeListener.accept(this);
        }
    }

    protected boolean isFocusOverlay() {
        return false;
    }

    public enum InteractionStrategy {
        NONE,
        CONTAINER,
        ALL;

        boolean allowContainer() {
            return this != NONE;
        }

        boolean allowAll() {
            return this == ALL;
        }
    }
}
