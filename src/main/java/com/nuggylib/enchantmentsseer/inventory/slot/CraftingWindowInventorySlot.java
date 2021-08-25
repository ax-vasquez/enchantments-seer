package com.nuggylib.enchantmentsseer.inventory.slot;

import com.nuggylib.enchantmentsseer.api.AutomationType;
import com.nuggylib.enchantmentsseer.api.IContentsListener;
import com.nuggylib.enchantmentsseer.api.annotations.FieldsAreNonnullByDefault;
import com.nuggylib.enchantmentsseer.api.annotations.NonNull;
import com.nuggylib.enchantmentsseer.common.content.qio.QIOCraftingWindow;
import com.nuggylib.enchantmentsseer.inventory.container.slot.VirtualInventoryContainerSlot;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BiPredicate;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/slot/CraftingWindowInventorySlot.java"
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CraftingWindowInventorySlot extends BasicInventorySlot {

    public static CraftingWindowInventorySlot input(QIOCraftingWindow window) {
        return new CraftingWindowInventorySlot(notExternal, alwaysTrueBi, window, window);
    }

    protected final QIOCraftingWindow craftingWindow;

    protected CraftingWindowInventorySlot(BiPredicate<@NonNull ItemStack, @NonNull AutomationType> canExtract,
                                          BiPredicate<@NonNull ItemStack, @NonNull AutomationType> canInsert, QIOCraftingWindow craftingWindow, @Nullable IContentsListener listener) {
        super(canExtract, canInsert, alwaysTrue, listener, 0, 0);
        this.craftingWindow = craftingWindow;
    }

    @Nonnull
    @Override
    public VirtualInventoryContainerSlot createContainerSlot() {
        return new VirtualInventoryContainerSlot(this, craftingWindow.getWindowData(), getSlotOverlay(), this::setStackUnchecked);
    }
}
