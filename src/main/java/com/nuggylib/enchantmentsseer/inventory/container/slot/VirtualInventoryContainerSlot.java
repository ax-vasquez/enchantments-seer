package com.nuggylib.enchantmentsseer.inventory.container.slot;

import com.nuggylib.enchantmentsseer.inventory.container.SelectedWindowData;
import com.nuggylib.enchantmentsseer.inventory.slot.BasicInventorySlot;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;
import java.util.function.IntSupplier;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/VirtualInventoryContainerSlot.java"
 */
public class VirtualInventoryContainerSlot extends InventoryContainerSlot implements IVirtualSlot {

    private final SelectedWindowData windowData;
    private IntSupplier xPositionSupplier = () -> x;
    private IntSupplier yPositionSupplier = () -> y;
    private ItemStack stackToRender = ItemStack.EMPTY;
    @Nullable
    private String tooltipOverride;
    private boolean shouldDrawOverlay;

    public VirtualInventoryContainerSlot(BasicInventorySlot slot, SelectedWindowData windowData, @Nullable SlotOverlay slotOverlay, Consumer<ItemStack> uncheckedSetter) {
        super(slot, 0, 0, ContainerSlotType.IGNORED, slotOverlay, uncheckedSetter);
        this.windowData = windowData;
    }

    @Override
    public int getActualX() {
        return xPositionSupplier.getAsInt();
    }

    @Override
    public int getActualY() {
        return yPositionSupplier.getAsInt();
    }

    @Override
    public void updatePosition(IntSupplier xPositionSupplier, IntSupplier yPositionSupplier) {
        this.xPositionSupplier = xPositionSupplier;
        this.yPositionSupplier = yPositionSupplier;
    }

    @Override
    public void updateRenderInfo(@Nonnull ItemStack stackToRender, boolean shouldDrawOverlay, @Nullable String tooltipOverride) {
        this.stackToRender = stackToRender;
        this.shouldDrawOverlay = shouldDrawOverlay;
        this.tooltipOverride = tooltipOverride;
    }

    @Nonnull
    @Override
    public ItemStack getStackToRender() {
        return stackToRender;
    }

    @Override
    public boolean shouldDrawOverlay() {
        return shouldDrawOverlay;
    }

    @Nullable
    @Override
    public String getTooltipOverride() {
        return tooltipOverride;
    }

    @Override
    public Slot getSlot() {
        return this;
    }

    @Override
    public boolean exists(@Nullable SelectedWindowData windowData) {
        return this.windowData.equals(windowData);
    }
}
