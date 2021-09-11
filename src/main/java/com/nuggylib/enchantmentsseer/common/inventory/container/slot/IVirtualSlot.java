package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.IntSupplier;

// TODO: See about moving this class to the client package since it only seems to be used for GUI-related things
/**
 * Interface representing an abstract container slot
 */
public interface IVirtualSlot {

    int getActualX();

    int getActualY();

    void updatePosition(IntSupplier xPositionSupplier, IntSupplier yPositionSupplier);

    void updateRenderInfo(@Nonnull ItemStack stackToRender, boolean shouldDrawOverlay, @Nullable String tooltipOverride);

    @Nonnull
    ItemStack getStackToRender();

    boolean shouldDrawOverlay();

    @Nullable
    String getTooltipOverride();

    Slot getSlot();

}
