package com.nuggylib.enchantmentsseer.common.capabilities.holder.slot;

import com.nuggylib.enchantmentsseer.api.RelativeSide;
import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/holder/slot/InventorySlotHelper.java"
 */
public class InventorySlotHelper {

    private final IInventorySlotHolder slotHolder;
    private boolean built;

    private InventorySlotHelper(IInventorySlotHolder slotHolder) {
        this.slotHolder = slotHolder;
    }

    public static InventorySlotHelper forSide(Supplier<Direction> facingSupplier) {
        return forSide(facingSupplier, null, null);
    }

    public static InventorySlotHelper forSide(Supplier<Direction> facingSupplier, @Nullable Predicate<RelativeSide> insertPredicate,
                                              @Nullable Predicate<RelativeSide> extractPredicate) {
        return new InventorySlotHelper(new InventorySlotHolder(facingSupplier, insertPredicate, extractPredicate));
    }

    public void addSlot(@Nonnull IInventorySlot slot) {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
        if (slotHolder instanceof InventorySlotHolder) {
            ((InventorySlotHolder) slotHolder).addSlot(slot);
        } else {
            throw new IllegalArgumentException("Holder does not know how to add slots");
        }
    }

    public void addSlot(@Nonnull IInventorySlot slot, RelativeSide... sides) {
        if (built) {
            throw new IllegalStateException("Builder has already built.");
        }
        if (slotHolder instanceof InventorySlotHolder) {
            ((InventorySlotHolder) slotHolder).addSlot(slot, sides);
        } else {
            throw new IllegalArgumentException("Holder does not know how to add slots on specific sides");
        }
    }

    public IInventorySlotHolder build() {
        built = true;
        return slotHolder;
    }
}
