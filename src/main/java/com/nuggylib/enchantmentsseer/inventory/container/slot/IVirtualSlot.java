package com.nuggylib.enchantmentsseer.inventory.container.slot;

import java.util.function.IntSupplier;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/IVirtualSlot.java"
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
