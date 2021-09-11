package com.nuggylib.enchantmentsseer.client.gui.element.slot;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.gui.IGuiWrapper;
import com.nuggylib.enchantmentsseer.client.gui.VirtualSlotContainerScreen;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.IVirtualSlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.SlotOverlay;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.VirtualInventoryContainerSlot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class GuiVirtualSlot extends GuiSlot {

    private IVirtualSlot virtualSlot;

    public GuiVirtualSlot(SlotType type, IGuiWrapper gui, int x, int y, VirtualInventoryContainerSlot containerSlot) {
        this(type, gui, x, y);
        if (containerSlot != null) {
            SlotOverlay slotOverlay = containerSlot.getSlotOverlay();
            if (slotOverlay != null) {
                with(slotOverlay);
            }
            updateVirtualSlot(containerSlot);
        }
    }

    public GuiVirtualSlot(SlotType type, IGuiWrapper gui, int x, int y) {
        super(type, gui, x, y);
        //Virtual slots need to render the hovered overlay as they don't let vanilla render it
        setRenderHover(true);
    }

    public boolean isElementForSlot(IVirtualSlot virtualSlot) {
        return this.virtualSlot == virtualSlot;
    }

    public void updateVirtualSlot(@Nonnull IVirtualSlot virtualSlot) {
        this.virtualSlot = virtualSlot;
        this.virtualSlot.updatePosition(() -> relativeX + 1, () -> relativeY + 1);
    }

    @Override
    protected void drawContents(@Nonnull MatrixStack matrix) {
        if (virtualSlot != null) {
            ItemStack stack = virtualSlot.getStackToRender();
            if (!stack.isEmpty()) {
                int xPos = x + 1;
                int yPos = y + 1;
                if (virtualSlot.shouldDrawOverlay()) {
                    fill(matrix, xPos, yPos, xPos + 16, yPos + 16, DEFAULT_HOVER_COLOR);
                }
                gui().renderItemWithOverlay(matrix, stack, xPos, yPos, 1, virtualSlot.getTooltipOverride());
            }
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (mouseX >= x && mouseY >= y && mouseX < x + width && mouseY < y + height) {
            IGuiWrapper gui = gui();
            if (gui instanceof VirtualSlotContainerScreen && virtualSlot != null) {
                //Redirect to a copy of vanilla logic
                return ((VirtualSlotContainerScreen<?>) gui).slotClicked(virtualSlot.getSlot(), button);
            }
        }
        return super.mouseClicked(mouseX, mouseY, button);
    }
}
