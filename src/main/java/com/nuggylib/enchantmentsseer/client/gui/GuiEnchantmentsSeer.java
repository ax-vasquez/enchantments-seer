package com.nuggylib.enchantmentsseer.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nuggylib.enchantmentsseer.client.gui.element.GuiElement;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.GuiVirtualSlot;
import com.nuggylib.enchantmentsseer.client.gui.element.window.GuiWindow;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.GuiSlot;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.SlotType;
import com.nuggylib.enchantmentsseer.client.render.text.IFancyFontRenderer;
import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import com.nuggylib.enchantmentsseer.common.inventory.container.SelectedWindowData;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.ContainerSlotType;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.IVirtualSlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.InventoryContainerSlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.SlotOverlay;
import com.nuggylib.enchantmentsseer.common.lib.collection.LRU;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils.ResourceType;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
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
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * Base GUI class
 *
 * Create subclasses that extend this class for any new GUI you need to create. This includes:
 * 1. Interactive block GUIs (e.g., furnace GUI)
 * 2. Achievement popups
 * 3. Warning/Error popups
 * 4. Anything else that renders as a "modal" type view in-game
 *
 * Method parameter names were inferred by cross-referencing the `EnchantmentScreen` class (from vanilla Minecraft)
 * and the Mekanism code, specifically `GuiMekanism`
 *
 * You may notice that this class doesn't really do much other than override super class methods, and you'd be right -
 * that's literally all this class does. <b>This class is a convenience class, intended to make it easier to use
 * the `ContainerScreen` class by giving the overridden method parameter names something meaningful.</b>
 *
 * @param <CONTAINER>           The corresponding `Container` class for the GUI
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/GuiMekanism.java"
 */
@ParametersAreNonnullByDefault
public abstract class GuiEnchantmentsSeer<CONTAINER extends Container> extends VirtualSlotContainerScreen<CONTAINER> implements IGuiWrapper, IFancyFontRenderer {

    public static final ResourceLocation BASE_BACKGROUND = EnchantmentsSeer.getResource(ResourceType.GUI, "base.png");
    public static final ResourceLocation SHADOW = EnchantmentsSeer.getResource(ResourceType.GUI, "shadow.png");
    public static final ResourceLocation BLUR = EnchantmentsSeer.getResource(ResourceType.GUI, "blur.png");

    // TODO: It appears we need to add windows to the LRU for the corresponding GUI; I checked the logs and it seems the children field is never populated
    protected final LRU<GuiWindow> windows = new LRU<>();
    protected final List<GuiElement> focusListeners = new ArrayList<>();
    public boolean switchingToJEI;

    public static int maxZOffset;

    public GuiEnchantmentsSeer(CONTAINER container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
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

    // TODO: Fix the textures - we just need the assets to use when rendering the base GUI
    /**
     * Render the GUI background
     *
     * This is the controlling method for all GUIs' backgrounds rendered in our mod.
     *
     * Note that, without rendering a background in this method, you'll likely encounter `java.lang.IndexOutOfBoundsException: Index: 0, Size: 0`
     */
    @Override
    protected void renderBg(MatrixStack matrix, float partialTick, int mouseX, int mouseY) {
        //Ensure the GL color is white as mods adding an overlay (such as JEI for bookmarks), might have left
        // it in an unexpected state.
        EnchantmentsSeerRenderer.resetColor();
        if (width < 8 || height < 8) {
            EnchantmentsSeer.LOGGER.warn("Gui: {}, was too small to draw the background of. Unable to draw a background for a gui smaller than 8 by 8.", getClass().getSimpleName());
            return;
        }
        GuiUtils.renderBackgroundTexture(matrix, BASE_BACKGROUND, 4, 4, leftPos, topPos, imageWidth, imageHeight, 256, 256);
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    /**
     * Render the GUI
     *
     * This is the controlling method for all GUIs rendered in our mod.
     *
     * Note that, without rendering a background in this method, you'll likely encounter `java.lang.IndexOutOfBoundsException: Index: 0, Size: 0`
     */
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
    public FontRenderer getFont() {
        return font;
    }

    @Override
    public ItemRenderer getItemRenderer() {
        return itemRenderer;
    }

    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
    }

    protected void addSlots() {
        int size = menu.slots.size();
        for (int i = 0; i < size; i++) {
            EnchantmentsSeer.LOGGER.info(String.format("Creating slot index: %s", i));
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
                if (slotType == ContainerSlotType.VALIDITY) {
                    int index = i;
                    guiSlot.validity(() -> checkValidity(index));
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

    /**
     * Called to add gui elements to the GUI. Add elements before calling super if they should be before the slots, and after if they should be after the slots. Most
     * elements can and should be added after the slots.
     */
    protected void addGuiElements() {
        // TODO: See if the check that Mekanism had was necessary (they use a variable that appears to be defaulting to true, according to their comments)
        addSlots();
    }

    @Override
    public void tick() {
        super.tick();
        children.stream().filter(child -> child instanceof GuiElement).map(child -> (GuiElement) child).forEach(GuiElement::tick);
        windows.forEach(GuiWindow::tick);
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

    @Override
    public void removeWindow(GuiWindow window) {
        if (!windows.isEmpty()) {
            GuiWindow top = windows.iterator().next();
            windows.remove(window);
            if (window == top) {
                //If the window was the top window, make it lose focus
                window.onFocusLost();
                //Amd check if a new window is now in focus
                GuiWindow newTop = windows.isEmpty() ? null : windows.iterator().next();
                if (newTop == null) {
                    //If there isn't any because they have all been removed
                    // fire an "event" for any post all windows being closed
                    lastWindowRemoved();
                } else {
                    //Otherwise mark the new window as being focused
                    newTop.onFocused();
                }
                //Update the listener to being the window that is now selected or null if none are
                setFocused(newTop);
            }
        }
    }

    protected void lastWindowRemoved() {
        //Mark that no windows are now selected
        if (menu instanceof EnchantmentsSeerContainer) {
            ((EnchantmentsSeerContainer) menu).setSelectedWindow(null);
        }
    }

    @Override
    public void setSelectedWindow(SelectedWindowData selectedWindow) {
        if (menu instanceof EnchantmentsSeerContainer) {
            ((EnchantmentsSeerContainer) menu).setSelectedWindow(selectedWindow);
        }
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
}
