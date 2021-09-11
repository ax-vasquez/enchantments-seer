package com.nuggylib.enchantmentsseer.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.GuiSlot;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.GuiVirtualSlot;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.SlotType;
import com.nuggylib.enchantmentsseer.client.gui.element.window.GuiWindow;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer;
import com.nuggylib.enchantmentsseer.client.render.IFancyFontRenderer;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.SelectedWindowData;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.ContainerSlotType;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.IVirtualSlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.InventoryContainerSlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.SlotOverlay;
import com.nuggylib.enchantmentsseer.common.lib.collection.LRU;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.IGuiEventListener;
import net.minecraft.client.gui.widget.Widget;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.tuple.Pair;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BooleanSupplier;
import java.util.function.Supplier;

public class GuiEnchantmentsSeer<CONTAINER extends Container> extends VirtualSlotContainerScreen<CONTAINER> implements IGuiWrapper, IFancyFontRenderer {

    public static final ResourceLocation BASE_BACKGROUND = EnchantmentsSeerUtils.getResource(ResourceType.GUI, "base.png");
    public static final ResourceLocation SHADOW = EnchantmentsSeerUtils.getResource(ResourceType.GUI, "shadow.png");
    public static final ResourceLocation BLUR = EnchantmentsSeerUtils.getResource(ResourceType.GUI, "blur.png");
    //TODO: Look into defaulting this to true
    protected boolean dynamicSlots;
    protected final LRU<GuiWindow> windows = new LRU<>();
    protected final List<GuiElement> focusListeners = new ArrayList<>();
    public boolean switchingToJEI;

    private boolean hasClicked = false;

    public static int maxZOffset;

    protected GuiEnchantmentsSeer(CONTAINER container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    @Override
    public void removed() {
        if (!switchingToJEI) {
            //If we are not switching to JEI then run the super close method
            // which will exit the container. We don't want to mark the
            // container as exited if it will be revived when leaving JEI
            super.removed();
        }
    }

    @Override
    public void init() {
        super.init();
        addGuiElements();
    }

    /**
     * Called to add gui elements to the GUI. Add elements before calling super if they should be before the slots, and after if they should be after the slots. Most
     * elements can and should be added after the slots.
     */
    protected void addGuiElements() {
        if (dynamicSlots) {
            addSlots();
        }
    }

    @Override
    public void tick() {
        super.tick();
        children.stream().filter(child -> child instanceof GuiElement).map(child -> (GuiElement) child).forEach(GuiElement::tick);
        windows.forEach(GuiWindow::tick);
    }

    @Override
    public void addFocusListener(GuiElement element) {
        focusListeners.add(element);
    }

    @Override
    public void removeFocusListener(GuiElement element) {
        focusListeners.remove(element);
    }

    @Override
    public void focusChange(GuiElement changed) {
        focusListeners.stream().filter(e -> e != changed).forEach(e -> e.setFocused(false));
    }

    @Override
    public void incrementFocus(GuiElement current) {
        int index = focusListeners.indexOf(current);
        if (index != -1) {
            GuiElement next = focusListeners.get((index + 1) % focusListeners.size());
            next.setFocused(true);
            focusChange(next);
        }
    }

    @Override
    protected boolean hasClickedOutside(double mouseX, double mouseY, int guiLeftIn, int guiTopIn, int mouseButton) {
        return getWindowHovering(mouseX, mouseY) == null && super.hasClickedOutside(mouseX, mouseY, guiLeftIn, guiTopIn, mouseButton);
    }

    @Override
    public void init(@Nonnull Minecraft minecraft, int width, int height) {
        //Mark that we are not switching to JEI if we start being initialized again
        switchingToJEI = false;
        //Note: We are forced to do the logic that normally would be inside the "resize" method
        // here in init, as when mods like JEI take over the screen to show recipes, and then
        // return the screen to the "state" it was beforehand it does not actually properly
        // transfer the state from the previous instance to the new instance. If we run the
        // code we normally would run for when things get resized, we then are able to
        // properly reinstate/transfer the states of the various elements
        List<Pair<Integer, GuiElement>> prevElements = new ArrayList<>();
        for (int i = 0; i < buttons.size(); i++) {
            Widget widget = buttons.get(i);
            if (widget instanceof GuiElement && ((GuiElement) widget).hasPersistentData()) {
                prevElements.add(Pair.of(i, (GuiElement) widget));
            }
        }
        // flush the focus listeners list unless it's an overlay
        focusListeners.removeIf(element -> !element.isOverlay);
        int prevLeft = leftPos, prevTop = topPos;
        super.init(minecraft, width, height);

        windows.forEach(window -> {
            window.resize(prevLeft, prevTop, leftPos, topPos);
            children.add(window);
        });

        prevElements.forEach(e -> {
            if (e.getLeft() < buttons.size()) {
                Widget widget = buttons.get(e.getLeft());
                // we're forced to assume that the children list is the same before and after the resize.
                // for verification, we run a lightweight class equality check
                // Note: We do not perform an instance check on widget to ensure it is a GuiElement, as that is
                // ensured by the class comparison, and the restrictions of what can go in prevElements
                if (widget.getClass() == e.getRight().getClass()) {
                    ((GuiElement) widget).syncFrom(e.getRight());
                }
            }
        });
    }

    @Override
    protected void renderLabels(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        matrix.translate(0, 0, 300);
        RenderSystem.translatef(-leftPos, -topPos, 0);
        children().stream().filter(c -> c instanceof GuiElement).forEach(c -> ((GuiElement) c).onDrawBackground(matrix, mouseX, mouseY, EnchantmentsSeerRenderer.getPartialTick()));
        RenderSystem.translatef(leftPos, topPos, 0);
        drawForegroundText(matrix, mouseX, mouseY);
        int xAxis = mouseX - leftPos;
        int yAxis = mouseY - topPos;
        // first render general foregrounds
        maxZOffset = 200;
        int zOffset = 200;
        for (Widget widget : this.buttons) {
            if (widget instanceof GuiElement) {
                matrix.pushPose();
                ((GuiElement) widget).onRenderForeground(matrix, mouseX, mouseY, zOffset, zOffset);
                matrix.popPose();
            }
        }

        // now render overlays in reverse-order (i.e. back to front)
        zOffset = maxZOffset;
        for (LRU<GuiWindow>.LRUIterator iter = getWindowsDescendingIterator(); iter.hasNext(); ) {
            GuiWindow overlay = iter.next();
            zOffset += 150;
            matrix.pushPose();
            overlay.onRenderForeground(matrix, mouseX, mouseY, zOffset, zOffset);
            if (iter.hasNext()) {
                // if this isn't the focused window, render a 'blur' effect over it
                overlay.renderBlur(matrix);
            }
            matrix.popPose();
        }
        // then render tooltips, translating above max z offset to prevent clashing
        GuiElement tooltipElement = getWindowHovering(mouseX, mouseY);
        if (tooltipElement == null) {
            for (int i = buttons.size() - 1; i >= 0; i--) {
                Widget widget = buttons.get(i);
                if (widget instanceof GuiElement && widget.isMouseOver(mouseX, mouseY)) {
                    tooltipElement = (GuiElement) widget;
                    break;
                }
            }
        }

        // translate forwards using RenderSystem. this should never have to happen as we do all the necessary translations with MatrixStacks,
        // but Minecraft has decided to not fully adopt MatrixStacks for many crucial ContainerScreen render operations. should be re-evaluated
        // when mc updates related logic on their end (IMPORTANT)
        RenderSystem.translatef(0, 0, maxZOffset);

        if (tooltipElement != null) {
            tooltipElement.renderToolTip(matrix, xAxis, yAxis);
        }

        // render item tooltips
        RenderSystem.translatef(-leftPos, -topPos, 0);
        renderTooltip(matrix, mouseX, mouseY);
        RenderSystem.translatef(leftPos, topPos, 0);

        // IMPORTANT: additional hacky translation so held items render okay. re-evaluate as discussed above
        RenderSystem.translatef(0, 0, 200);
    }

    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
    }

    @Nonnull
    @Override
    public Optional<IGuiEventListener> getChildAt(double mouseX, double mouseY) {
        GuiWindow window = getWindowHovering(mouseX, mouseY);
        return window != null ? Optional.of(window) : super.getChildAt(mouseX, mouseY);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        hasClicked = true;
        // first try to send the mouse event to our overlays
        GuiWindow top = windows.isEmpty() ? null : windows.iterator().next();
        GuiWindow focused = windows.stream().filter(overlay -> overlay.mouseClicked(mouseX, mouseY, button)).findFirst().orElse(null);
        if (focused != null) {
            if (windows.contains(focused)) {
                //Validate that the focused window is still one of our windows, as if it wasn't focused/on top and
                // it is being closed, we don't want to update and mark it as focused, as our defocusing code won't
                // run as we ran it when we pressed the button
                setFocused(focused);
                if (button == 0) {
                    setDragging(true);
                }
                // this check prevents us from moving the window to the top of the stack if the clicked window opened up an additional window
                if (top != focused) {
                    top.onFocusLost();
                    windows.moveUp(focused);
                    focused.onFocused();
                }
            }
            return true;
        }
        // otherwise we send it to the current element
        for (int i = buttons.size() - 1; i >= 0; i--) {
            IGuiEventListener listener = buttons.get(i);
            if (listener.mouseClicked(mouseX, mouseY, button)) {
                setFocused(listener);
                if (button == 0) {
                    setDragging(true);
                }
                return true;
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    public boolean mouseReleased(double mouseX, double mouseY, int button) {
        if (hasClicked) {
            // always pass mouse released events to windows for drag checks
            windows.forEach(w -> w.onRelease(mouseX, mouseY));
            return super.mouseReleased(mouseX, mouseY, button);
        }
        return false;
    }

    @Override
    public boolean keyPressed(int keyCode, int scanCode, int modifiers) {
        return windows.stream().anyMatch(window -> window.keyPressed(keyCode, scanCode, modifiers)) ||
                GuiUtils.checkChildren(buttons, child -> child.keyPressed(keyCode, scanCode, modifiers)) || super.keyPressed(keyCode, scanCode, modifiers);
    }

    @Override
    public boolean charTyped(char c, int keyCode) {
        return windows.stream().anyMatch(window -> window.charTyped(c, keyCode)) || GuiUtils.checkChildren(buttons, child -> child.charTyped(c, keyCode)) ||
                super.charTyped(c, keyCode);
    }

    /**
     * @apiNote mouseXOld and mouseYOld are just guessed mappings I couldn't find any usage from a quick glance.
     */
    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double mouseXOld, double mouseYOld) {
        super.mouseDragged(mouseX, mouseY, button, mouseXOld, mouseYOld);
        return getFocused() != null && isDragging() && button == 0 && getFocused().mouseDragged(mouseX, mouseY, button, mouseXOld, mouseYOld);
    }

    @Nullable
    @Override
    @Deprecated//Don't use directly, this is normally private in ContainerScreen
    protected Slot findSlot(double mouseX, double mouseY) {
        //We override the implementation we have in VirtualSlotContainerScreen so that we can cache getting our window
        // and have some general performance improvements given we can batch a bunch of lookups together
        boolean checkedWindow = false;
        boolean overNoButtons = false;
        GuiWindow window = null;
        for (Slot slot : menu.slots) {
            boolean virtual = slot instanceof IVirtualSlot;
            int xPos = slot.x;
            int yPos = slot.y;
            if (virtual) {
                //Virtual slots need special handling to allow for matching them to the window they may be attached to
                IVirtualSlot virtualSlot = (IVirtualSlot) slot;
                xPos = virtualSlot.getActualX();
                yPos = virtualSlot.getActualY();
            }
            if (super.isHovering(xPos, yPos, 16, 16, mouseX, mouseY)) {
                if (!checkedWindow) {
                    //Only lookup the window once
                    checkedWindow = true;
                    window = getWindowHovering(mouseX, mouseY);
                    overNoButtons = overNoButtons(window, mouseX, mouseY);
                }
                if (overNoButtons && slot.isActive()) {
                    if (window == null) {
                        return slot;
                    } else if (virtual && window.childrenContainsElement(element ->
                            element instanceof GuiVirtualSlot && ((GuiVirtualSlot) element).isElementForSlot((IVirtualSlot) slot))) {
                        return slot;
                    }
                }
            }
        }
        return null;
    }

    @Override
    protected boolean isMouseOverSlot(@Nonnull Slot slot, double mouseX, double mouseY) {
        if (slot instanceof IVirtualSlot) {
            //Virtual slots need special handling to allow for matching them to the window they may be attached to
            IVirtualSlot virtualSlot = (IVirtualSlot) slot;
            int xPos = virtualSlot.getActualX();
            int yPos = virtualSlot.getActualY();
            if (super.isHovering(xPos, yPos, 16, 16, mouseX, mouseY)) {
                GuiWindow window = getWindowHovering(mouseX, mouseY);
                //If we are hovering over a window, check if the virtual slot is a child of the window
                if (window == null || window.childrenContainsElement(element -> element instanceof GuiVirtualSlot && ((GuiVirtualSlot) element).isElementForSlot(virtualSlot))) {
                    return overNoButtons(window, mouseX, mouseY);
                }
            }
            return false;
        }
        return isHovering(slot.x, slot.y, 16, 16, mouseX, mouseY);
    }

    private boolean overNoButtons(@Nullable GuiWindow window, double mouseX, double mouseY) {
        if (window == null) {
            return buttons.stream().noneMatch(button -> button.isMouseOver(mouseX, mouseY));
        }
        return !window.childrenContainsElement(e -> e.isMouseOver(mouseX, mouseY));
    }

    @Override
    protected boolean isHovering(int x, int y, int width, int height, double mouseX, double mouseY) {
        // overridden to prevent slot interactions when a GuiElement is blocking
        return super.isHovering(x, y, width, height, mouseX, mouseY) && getWindowHovering(mouseX, mouseY) == null &&
                overNoButtons(null, mouseX, mouseY);
    }

    /**
     * Adds the slots defined for the corresponding container using its container slots
     */
    protected void addSlots() {
        int size = menu.slots.size();
        for (int i = 0; i < size; i++) {
            Slot slot = menu.slots.get(i);
            if (slot instanceof InventoryContainerSlot) {
                InventoryContainerSlot containerSlot = (InventoryContainerSlot) slot;
                ContainerSlotType slotType = containerSlot.getSlotType();
                //Shift the slots by one as the elements include the border of the slot
                SlotType type = SlotType.NORMAL;
                GuiSlot guiSlot = new GuiSlot(type, this, slot.x - 1, slot.y - 1);
                SlotOverlay slotOverlay = containerSlot.getSlotOverlay();
                if (slotOverlay != null) {
                    guiSlot.with(slotOverlay);
                }
                addButton(guiSlot);
            } else {
                addButton(new GuiSlot(SlotType.NORMAL, this, slot.x - 1, slot.y - 1));
            }
        }
    }

    protected ItemStack checkValidity(int slotIndex) {
        return ItemStack.EMPTY;
    }

    @Override
    protected void renderBg(@Nonnull MatrixStack matrix, float partialTick, int mouseX, int mouseY) {
        //Ensure the GL color is white as mods adding an overlay (such as JEI for bookmarks), might have left
        // it in an unexpected state.
        EnchantmentsSeerRenderer.resetColor();
        if (width < 8 || height < 8) {
            EnchantmentsSeer.logger.warn("Gui: {}, was too small to draw the background of. Unable to draw a background for a gui smaller than 8 by 8.", getClass().getSimpleName());
            return;
        }
        GuiUtils.renderBackgroundTexture(matrix, BASE_BACKGROUND, 4, 4, leftPos, topPos, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public FontRenderer getFont() {
        return font;
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        // shift back a whole lot so we can stack more windows
        RenderSystem.translated(0, 0, -500);
        matrix.pushPose();
        renderBackground(matrix);
        //Apply our matrix stack to the render system and pass an unmodified one to the super method
        // Vanilla still renders the items into the GUI using render system transformations so this
        // is required to not have tooltips of GuiElements rendering behind the items
        super.render(matrix, mouseX, mouseY, partialTicks);
        matrix.popPose();
        RenderSystem.translated(0, 0, 500);
    }

    @Override
    public void renderItemTooltip(MatrixStack matrix, @Nonnull ItemStack stack, int xAxis, int yAxis) {
        renderTooltip(matrix, stack, xAxis, yAxis);
    }

    @Override
    public void renderItemTooltipWithExtra(MatrixStack matrix, @Nonnull ItemStack stack, int xAxis, int yAxis, List<ITextComponent> toAppend) {
        if (toAppend.isEmpty()) {
            renderItemTooltip(matrix, stack, xAxis, yAxis);
        } else {
            FontRenderer font = stack.getItem().getFontRenderer(stack);
            net.minecraftforge.fml.client.gui.GuiUtils.preItemToolTip(stack);
            List<ITextComponent> tooltip = new ArrayList<>(getTooltipFromItem(stack));
            tooltip.addAll(toAppend);
            renderWrappedToolTip(matrix, tooltip, xAxis, yAxis, (font == null ? this.font : font));
            net.minecraftforge.fml.client.gui.GuiUtils.postItemToolTip();
        }
    }

    @Override
    public ItemRenderer getItemRenderer() {
        return itemRenderer;
    }

    @Override
    public boolean currentlyQuickCrafting() {
        return isQuickCrafting && !quickCraftSlots.isEmpty();
    }

    @Override
    public void addWindow(GuiWindow window) {
        GuiWindow top = windows.isEmpty() ? null : windows.iterator().next();
        if (top != null) {
            top.onFocusLost();
        }
        windows.add(window);
        window.onFocused();
    }

    @Nullable
    @Override
    public GuiWindow getWindowHovering(double mouseX, double mouseY) {
        return windows.stream().filter(w -> w.isMouseOver(mouseX, mouseY)).findFirst().orElse(null);
    }

    public Collection<GuiWindow> getWindows() {
        return windows;
    }

    public LRU<GuiWindow>.LRUIterator getWindowsDescendingIterator() {
        return windows.descendingIterator();
    }
}
