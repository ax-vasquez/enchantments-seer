package com.nuggylib.enchantmentsseer.common.capabilities.holder.slot;

import com.nuggylib.enchantmentsseer.api.RelativeSide;
import com.nuggylib.enchantmentsseer.common.capabilities.holder.BasicHolder;
import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/holder/slot/InventorySlotHolder.java"
 */
public class InventorySlotHolder extends BasicHolder<IInventorySlot> implements IInventorySlotHolder {

    @Nullable
    private final Predicate<RelativeSide> insertPredicate;
    @Nullable
    private final Predicate<RelativeSide> extractPredicate;

    InventorySlotHolder(Supplier<Direction> facingSupplier, @Nullable Predicate<RelativeSide> insertPredicate, @Nullable Predicate<RelativeSide> extractPredicate) {
        super(facingSupplier);
        this.insertPredicate = insertPredicate;
        this.extractPredicate = extractPredicate;
    }

    void addSlot(@Nonnull IInventorySlot slot, RelativeSide... sides) {
        addSlotInternal(slot, sides);
    }

    @Nonnull
    @Override
    public List<IInventorySlot> getInventorySlots(@Nullable Direction direction) {
        return getSlots(direction);
    }

    @Override
    public boolean canInsert(@Nullable Direction direction) {
        //If the insert predicate is null then we can insert from any side, don't bother looking up our facing
        return direction != null && (insertPredicate == null || insertPredicate.test(RelativeSide.fromDirections(facingSupplier.get(), direction)));
    }

    @Override
    public boolean canExtract(@Nullable Direction direction) {
        //If the extract predicate is null then we can extract from any side, don't bother looking up our facing
        return direction != null && (extractPredicate == null || extractPredicate.test(RelativeSide.fromDirections(facingSupplier.get(), direction)));
    }
}
