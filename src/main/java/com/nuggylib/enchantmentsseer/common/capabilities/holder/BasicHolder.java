package com.nuggylib.enchantmentsseer.common.capabilities.holder;

import com.nuggylib.enchantmentsseer.api.RelativeSide;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.InventorySlotHolder;
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

    private final String LOG_CLASS_PREFIX = InventorySlotHolder.class.getSimpleName();

    private final Map<RelativeSide, List<TYPE>> directionalSlots = new EnumMap<>(RelativeSide.class);
    private final List<TYPE> inventorySlots = new ArrayList<>();
    protected final Supplier<Direction> facingSupplier;
    //TODO: Allow declaring that some sides will be the same, so can just be the same list in memory??

    protected BasicHolder(Supplier<Direction> facingSupplier) {
        this.facingSupplier = facingSupplier;
    }

    protected void addSlotInternal(@Nonnull TYPE slot, RelativeSide... sides) {
        EnchantmentsSeer.LOGGER.info(String.format("[%s#%s] - Adding slot: %s", LOG_CLASS_PREFIX, "addSlotInternal", slot.getClass().getSimpleName()));
        inventorySlots.add(slot);
        for (RelativeSide side : sides) {
            directionalSlots.computeIfAbsent(side, k -> new ArrayList<>()).add(slot);
        }
        EnchantmentsSeer.LOGGER.info(String.format("[%s#%s] - Added slot: %s", LOG_CLASS_PREFIX, "addSlotInternal", slot.getClass().getSimpleName()));
    }

    @Nonnull
    public List<TYPE> getSlots(@Nullable Direction side) {
        EnchantmentsSeer.LOGGER.info(String.format("[%s#%s] - Getting slots for side: %s", LOG_CLASS_PREFIX, "getSlots", side));
        if (side == null || directionalSlots.isEmpty()) {
            EnchantmentsSeer.LOGGER.info(String.format("[%s#%s] - Returning slots: %s", LOG_CLASS_PREFIX, "getSlots", inventorySlots));
            //If we want the internal OR we have no side specification, give all of our slots
            return inventorySlots;
        }
        List<TYPE> slots = directionalSlots.get(RelativeSide.fromDirections(facingSupplier.get(), side));
        EnchantmentsSeer.LOGGER.info(String.format("[%s#%s] - Returning slots: %s", LOG_CLASS_PREFIX, "getSlots", slots));
        if (slots == null) {
            return Collections.emptyList();
        }
        return slots;
    }
}
