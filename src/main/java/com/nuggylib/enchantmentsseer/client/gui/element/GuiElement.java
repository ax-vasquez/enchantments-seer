package com.nuggylib.enchantmentsseer.client.gui.element;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.gui.GuiEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.gui.GuiUtils;
import com.nuggylib.enchantmentsseer.client.gui.IGuiWrapper;
import com.nuggylib.enchantmentsseer.client.gui.element.window.GuiWindow;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer;
import com.nuggylib.enchantmentsseer.client.render.text.IFancyFontRenderer;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

/**
 * Inspired by the Mekanism code
 *
 * This class adds convenience methods to assist in working with the GUI.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/element/GuiElement.java"
 */
public class GuiElement extends Widget implements IFancyFontRenderer {

    private static final int BUTTON_TEX_X = 200, BUTTON_TEX_Y = 60;
    protected final List<GuiElement> children = new ArrayList<>();
    /**
     * Children that don't get drawn or checked for beyond transferring data. This is mainly a helper to make it easier to update positioning information of background
     * helpers.
     */
    private final List<GuiElement> positionOnlyChildren = new ArrayList<>();
    public static final Minecraft minecraft = Minecraft.getInstance();

    protected ButtonBackground buttonBackground = ButtonBackground.NONE;

    private IGuiWrapper guiObj;

    protected boolean playClickSound;
    public boolean isOverlay;

    public GuiElement(IGuiWrapper gui, int x, int y, int width, int height, ITextComponent text) {
        super(x, y, width, height, text);
        guiObj = gui;
    }

    /**
     * Transfers this {@link GuiElement} to a new parent {@link IGuiWrapper}, and moves elements as needed.
     */
    public void transferToNewGui(IGuiWrapper gui) {
        int prevLeft = getGuiLeft();
        int prevTop = getGuiTop();
        //Use a separate method to update the guiObj for the element and all children
        // so that resize only gets called once
        transferToNewGuiInternal(gui);
        resize(prevLeft, prevTop, getGuiLeft(), getGuiTop());
    }

    private void transferToNewGuiInternal(IGuiWrapper gui) {
        guiObj = gui;
        children.forEach(child -> child.transferToNewGuiInternal(gui));
        //Transfer position only children as well
        positionOnlyChildren.forEach(child -> child.transferToNewGuiInternal(gui));
    }

    protected <ELEMENT extends GuiElement> ELEMENT addChild(ELEMENT element) {
        children.add(element);
        if (isOverlay) {
            element.isOverlay = true;
        }
        return element;
    }

    protected <ELEMENT extends GuiElement> ELEMENT addPositionOnlyChild(ELEMENT element) {
        //TODO - 10.1: Re-evaluate "children" added here. As it may make sense to try and move some of them to being normal children
        // or it may turn out that some of them are supposed to be ticking or something
        positionOnlyChildren.add(element);
        return element;
    }

    public final IGuiWrapper gui() {
        return guiObj;
    }

    public final int getGuiLeft() {
        return guiObj.getLeft();
    }

    public final int getGuiTop() {
        return guiObj.getTop();
    }

    public final int getGuiWidth() {
        return guiObj.getWidth();
    }

    public final int getGuiHeight() {
        return guiObj.getHeight();
    }

    public List<GuiElement> children() {
        return children;
    }

    public void tick() {
        children.forEach(GuiElement::tick);
    }

    /**
     * @apiNote prevLeft and prevTop may be equal to left and top when things are being reinitialized such as when returning from viewing recipes in JEI.
     */
    public void resize(int prevLeft, int prevTop, int left, int top) {
        x = x - prevLeft + left;
        y = y - prevTop + top;
        children.forEach(child -> child.resize(prevLeft, prevTop, left, top));
        positionOnlyChildren.forEach(child -> child.resize(prevLeft, prevTop, left, top));
    }

    public boolean childrenContainsElement(Predicate<GuiElement> checker) {
        return children.stream().anyMatch(e -> e.containsElement(checker));
    }

    public boolean containsElement(Predicate<GuiElement> checker) {
        return checker.test(this) || childrenContainsElement(checker);
    }

    @Override
    public void setFocused(boolean focused) {
        // change access modifier to public
        super.setFocused(focused);
    }

    @Override
    public FontRenderer getFont() {
        return guiObj.getFont();
    }

    @Override
    public int getXSize() {
        return width;
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return GuiUtils.checkChildren(children, child -> child.mouseClicked(mouseX, mouseY, button)) || super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return GuiUtils.checkChildren(children, child -> child.keyPressed(keyCode, scanCode, modifiers)) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int keyCode) {
        return GuiUtils.checkChildren(children, child -> child.charTyped(c, keyCode)) || super.charTyped(c, keyCode);
    }

    @Override
    public void onDrag(double mouseX, double mouseY, double mouseXOld, double mouseYOld) {
        super.onDrag(mouseX, mouseY, mouseXOld, mouseYOld);
    }

    @Override
    public void onRelease(double mouseX, double mouseY) {
        super.onRelease(mouseX, mouseY);
    }

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double delta) {
        return GuiUtils.checkChildren(children, child -> child.mouseScrolled(mouseX, mouseY, delta)) || super.mouseScrolled(mouseX, mouseY, delta);
    }

    @Override
    public void renderToolTip(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        //TODO - 10.1: Evaluate this and other methods that loop children and see if any overrides should be calling super
        children.stream().filter(child -> child.isMouseOver(mouseX + getGuiLeft(), mouseY + getGuiTop()))
                .forEach(child -> child.renderToolTip(matrix, mouseX, mouseY));
    }

    public void move(int changeX, int changeY) {
        x += changeX;
        y += changeY;
        children.forEach(child -> child.move(changeX, changeY));
        positionOnlyChildren.forEach(child -> child.move(changeX, changeY));
    }

    /**
     * Override this to render the button with a different x position than this GuiElement
     */
    protected int getButtonX() {
        return x;
    }

    /**
     * Override this to render the button with a different y position than this GuiElement
     */
    protected int getButtonY() {
        return y;
    }

    /**
     * Override this to render the button with a different width than this GuiElement
     */
    protected int getButtonWidth() {
        return width;
    }

    /**
     * Override this to render the button with a different height than this GuiElement
     */
    protected int getButtonHeight() {
        return height;
    }

    public void renderBackgroundOverlay(MatrixStack matrix, int mouseX, int mouseY) {
    }

    protected void renderBackgroundTexture(MatrixStack matrix, ResourceLocation resource, int sideWidth, int sideHeight) {
        GuiUtils.renderBackgroundTexture(matrix, resource, sideWidth, sideHeight, getButtonX(), getButtonY(), getButtonWidth(), getButtonHeight(), 256, 256);
    }

    public void onWindowClose() {
        children.forEach(GuiElement::onWindowClose);
    }

    public void setButtonBackground(ButtonBackground buttonBackground) {
        this.buttonBackground = buttonBackground;
    }

    public void drawBackground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        if (buttonBackground != ButtonBackground.NONE) {
            drawButton(matrix, mouseX, mouseY);
        }
    }

    /**
     * Override this if you do not want {@link #drawButton(MatrixStack, int, int)} to reset the color before drawing.
     */
    protected boolean resetColorBeforeRender() {
        return true;
    }

    @Override
    public boolean isMouseOver(double mouseX, double mouseY) {
        return super.isMouseOver(mouseX, mouseY) || children.stream().anyMatch(child -> child.isMouseOver(mouseX, mouseY));
    }

    /**
     * Does the same as {@link #isMouseOver(double, double)}, but validates there is no window in the way
     */
    public final boolean isMouseOverCheckWindows(double mouseX, double mouseY) {
        //TODO: Ideally we would have the various places that call this instead check isHovered if we can properly override setting that
        boolean isHovering = isMouseOver(mouseX, mouseY);
        if (isHovering) {
            //If the mouse is over this element, check if there is a window that would intercept the mouse
            GuiWindow window = guiObj.getWindowHovering(mouseX, mouseY);
            if (window != null && !window.childrenContainsElement(e -> e == this)) {
                //If there is and this element is not part of that window,
                // then mark that our mouse is not over the element
                isHovering = false;
            }
        }
        return isHovering;
    }

    protected void drawButton(MatrixStack matrix, int mouseX, int mouseY) {
        if (resetColorBeforeRender()) {
            //TODO: Support alpha like super? Is there a point
            EnchantmentsSeerRenderer.resetColor();
        }
        //TODO: Convert this to being two different 16x48 images, one for with border and one for buttons without a black border?
        // And then make it so that they can stretch out to be any size (make this make use of the renderExtendedTexture method
        EnchantmentsSeerRenderer.bindTexture(buttonBackground.getTexture());
        int i = getYImage(isMouseOverCheckWindows(mouseX, mouseY));
        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();
        RenderSystem.enableDepthTest();

        int width = getButtonWidth();
        int height = getButtonHeight();
        int halfWidthLeft = width / 2;
        int halfWidthRight = width % 2 == 0 ? halfWidthLeft : halfWidthLeft + 1;
        int halfHeightTop = height / 2;
        int halfHeightBottom = height % 2 == 0 ? halfHeightTop : halfHeightTop + 1;
        int position = i * 20;

        int x = getButtonX();
        int y = getButtonY();
        //Left Top Corner
        blit(matrix, x, y, 0, position, halfWidthLeft, halfHeightTop, BUTTON_TEX_X, BUTTON_TEX_Y);
        //Left Bottom Corner
        blit(matrix, x, y + halfHeightTop, 0, position + 20 - halfHeightBottom, halfWidthLeft, halfHeightBottom, BUTTON_TEX_X, BUTTON_TEX_Y);
        //Right Top Corner
        blit(matrix, x + halfWidthLeft, y, 200 - halfWidthRight, position, halfWidthRight, halfHeightTop, BUTTON_TEX_X, BUTTON_TEX_Y);
        //Right Bottom Corner
        blit(matrix, x + halfWidthLeft, y + halfHeightTop, 200 - halfWidthRight, position + 20 - halfHeightBottom, halfWidthRight, halfHeightBottom, BUTTON_TEX_X, BUTTON_TEX_Y);

        //TODO: Add support for buttons that are larger than 200x20 in either direction (most likely would be in the height direction
        // Can use a lot of the same logic as GuiMekanism does for its background

        renderBg(matrix, minecraft, mouseX, mouseY);
        //TODO - 10.1: Re-evaluate this and FilterSelectButton#drawBackground as vanilla doesn't disable these after
        // it draws a button but I am not sure if that is intentional or causes issues
        RenderSystem.disableBlend();
        RenderSystem.disableDepthTest();
    }

    public void displayTooltip(MatrixStack matrix, ITextComponent component, int xAxis, int yAxis) {
        guiObj.displayTooltip(matrix, component, xAxis, yAxis);
    }

    public void displayTooltips(MatrixStack matrix, List<ITextComponent> list, int xAxis, int yAxis) {
        guiObj.displayTooltips(matrix, list, xAxis, yAxis);
    }

    public final void onRenderForeground(MatrixStack matrix, int mouseX, int mouseY, int zOffset, int totalOffset) {
        if (visible) {
            matrix.translate(0, 0, zOffset);
            // update the max total offset to prevent clashing of future overlays
            GuiEnchantmentsSeer.maxZOffset = Math.max(totalOffset, GuiEnchantmentsSeer.maxZOffset);
            // fix render offset for background drawing
            matrix.translate(-getGuiLeft(), -getGuiTop(), 0);
            // render background overlay and children above everything else
            renderBackgroundOverlay(matrix, mouseX, mouseY);
            // render children just above background overlay
            children.forEach(child -> child.render(matrix, mouseX, mouseY, 0));
            children.forEach(child -> child.onDrawBackground(matrix, mouseX, mouseY, 0));
            // translate back to top right corner and forward to render foregrounds
            matrix.translate(getGuiLeft(), getGuiTop(), 0);
            renderForeground(matrix, mouseX, mouseY);
            // translate forward to render child foreground
            children.forEach(child -> child.onRenderForeground(matrix, mouseX, mouseY, 50, totalOffset + 50));
        }
    }

    public void renderForeground(MatrixStack matrix, int mouseX, int mouseY) {
        drawButtonText(matrix, mouseX, mouseY);
    }

    public enum ButtonBackground {
        DEFAULT(EnchantmentsSeer.getResource(ResourceType.GUI, "button.png")),
        DIGITAL(EnchantmentsSeer.getResource(ResourceType.GUI, "button_digital.png")),
        NONE(null);

        private final ResourceLocation texture;

        ButtonBackground(ResourceLocation texture) {
            this.texture = texture;
        }

        public ResourceLocation getTexture() {
            return texture;
        }
    }

    @FunctionalInterface
    public interface IHoverable {

        void onHover(GuiElement element, MatrixStack matrix, int mouseX, int mouseY);
    }

    @FunctionalInterface
    public interface IClickable {

        void onClick(GuiElement element, int mouseX, int mouseY);
    }

    public final void onDrawBackground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        if (visible) {
            drawBackground(matrix, mouseX, mouseY, partialTicks);
        }
    }

    protected int getButtonTextColor(int mouseX, int mouseY) {
        return getFGColor();
    }

    protected void drawButtonText(MatrixStack matrix, int mouseX, int mouseY) {
        ITextComponent text = getMessage();
        //Only attempt to draw the message if we have a message to draw
        if (!text.getString().isEmpty()) {
            int color = getButtonTextColor(mouseX, mouseY) | MathHelper.ceil(alpha * 255.0F) << 24;
            drawCenteredTextScaledBound(matrix, text, width - 4, x - getGuiLeft(), y - getGuiTop() + height / 2F - 4, color);
        }
    }

    public boolean hasPersistentData() {
        return children.stream().anyMatch(GuiElement::hasPersistentData);
    }

    public void syncFrom(GuiElement element) {
        int numChildren = children.size();
        if (numChildren > 0) {
            for (int i = 0; i < element.children.size(); i++) {
                GuiElement prevChild = element.children.get(i);
                if (prevChild.hasPersistentData() && i < numChildren) {
                    GuiElement child = children.get(i);
                    // we're forced to assume that the children list is the same before and after the resize.
                    // for verification, we run a lightweight class equality check
                    if (child.getClass() == prevChild.getClass()) {
                        child.syncFrom(prevChild);
                    }
                }
            }
        }
    }
}
