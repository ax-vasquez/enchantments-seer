package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import com.nuggylib.enchantmentsseer.api.IContentsListener;
import com.nuggylib.enchantmentsseer.common.inventory.slot.BasicInventorySlot;
import com.nuggylib.enchantmentsseer.api.annotations.NonNull;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/slot/InputInventorySlot.java"
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class InputInventorySlot extends BasicInventorySlot {

    public static InputInventorySlot at(@Nullable IContentsListener listener, int x, int y) {
        return at(alwaysTrue, listener, x, y);
    }

    public static InputInventorySlot at(Predicate<@NonNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y) {
        return at(alwaysTrue, isItemValid, listener, x, y);
    }

    public static InputInventorySlot at(Predicate<@NonNull ItemStack> insertPredicate, Predicate<@NonNull ItemStack> isItemValid, @Nullable IContentsListener listener,
                                        int x, int y) {
        Objects.requireNonNull(insertPredicate, "Insertion check cannot be null");
        Objects.requireNonNull(isItemValid, "Item validity check cannot be null");
        return new InputInventorySlot(insertPredicate, isItemValid, listener, x, y);
    }

    protected InputInventorySlot(Predicate<@NonNull ItemStack> insertPredicate, Predicate<@NonNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y) {
        super(notExternal, (stack, automationType) -> insertPredicate.test(stack), isItemValid, listener, x, y);
        setSlotType(ContainerSlotType.INPUT);
    }
}
