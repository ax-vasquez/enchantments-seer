package com.nuggylib.enchantmentsseer.common.lib.frequency;

import com.nuggylib.enchantmentsseer.inventory.IEnchantmentsSeerInventory;
import com.nuggylib.enchantmentsseer.inventory.IInventorySlot;
import com.nuggylib.enchantmentsseer.inventory.slot.EntangloporterInventorySlot;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/content/entangloporter/InventoryFrequency.java"
 */
public class InventoryFrequency extends Frequency implements IEnchantmentsSeerInventory {

    private IInventorySlot storedItem;

    private List<IInventorySlot> inventorySlots;

    /**
     * @param uuid Should only be null if we have incomplete data that we are loading
     */
    public InventoryFrequency(String n, @Nullable UUID uuid) {
        super(FrequencyType.INVENTORY, n, uuid);
        presetVariables();
    }

    public InventoryFrequency() {
        super(FrequencyType.INVENTORY);
    }

    private void presetVariables() {
        inventorySlots = Collections.singletonList(storedItem = EntangloporterInventorySlot.create(this));
    }

    @Nonnull
    @Override
    public List<IInventorySlot> getInventorySlots(@Nullable Direction side) {
        return inventorySlots;
    }

    @Override
    public void onContentsChanged() {
    }
}
