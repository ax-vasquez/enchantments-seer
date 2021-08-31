package com.nuggylib.enchantmentsseer.common.capabilities.holder;

import com.nuggylib.enchantmentsseer.api.RelativeSide;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <TYPE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/holder/BasicHolder.java"
 */
public class BasicHolder<TYPE> implements IHolder {

    private final Map<RelativeSide, List<TYPE>> directionalSlots = new EnumMap<>(RelativeSide.class);
    private final List<TYPE> inventorySlots = new ArrayList<>();
    protected final Supplier<Direction> facingSupplier;
    //TODO: Allow declaring that some sides will be the same, so can just be the same list in memory??

    protected BasicHolder(Supplier<Direction> facingSupplier) {
        this.facingSupplier = facingSupplier;
    }

    protected void addSlotInternal(@Nonnull TYPE slot, RelativeSide... sides) {
        inventorySlots.add(slot);
        for (RelativeSide side : sides) {
            directionalSlots.computeIfAbsent(side, k -> new ArrayList<>()).add(slot);
        }
    }

    @Nonnull
    public List<TYPE> getSlots(@Nullable Direction side) {
        if (side == null || directionalSlots.isEmpty()) {
            //If we want the internal OR we have no side specification, give all of our slots
            return inventorySlots;
        }
        List<TYPE> slots = directionalSlots.get(RelativeSide.fromDirections(facingSupplier.get(), side));
        if (slots == null) {
            return Collections.emptyList();
        }
        return slots;
    }
}
