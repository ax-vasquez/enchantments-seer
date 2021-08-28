package com.nuggylib.enchantmentsseer.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.GuiSlot;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.SlotType;
import com.nuggylib.enchantmentsseer.client.render.text.IFancyFontRenderer;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.ContainerSlotType;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.InventoryContainerSlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.SlotOverlay;
import com.nuggylib.enchantmentsseer.common.util.EnchantmentsSeerUtils.ResourceType;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

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
public abstract class AbstractGui<CONTAINER extends Container> extends ContainerScreen<CONTAINER> implements IGuiWrapper, IFancyFontRenderer {

    public static final ResourceLocation BASE_BACKGROUND = EnchantmentsSeer.getResource(ResourceType.GUI, "base.png");
    public static final ResourceLocation SHADOW = EnchantmentsSeer.getResource(ResourceType.GUI, "shadow.png");
    public static final ResourceLocation BLUR = EnchantmentsSeer.getResource(ResourceType.GUI, "blur.png");

    public static int maxZOffset;

    public AbstractGui(CONTAINER container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialTick, int mouseX, int mouseY) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {

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

}
