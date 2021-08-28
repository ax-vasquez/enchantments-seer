package com.nuggylib.enchantmentsseer.api.inventory;

import com.nuggylib.enchantmentsseer.api.Action;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Direction;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Inspired by the Mekanism codebase
 *
 * The main difference in our class is that we don't care about the concept of "sides". Block GUIs are simply rendered
 * when you right-click an applicable block.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/inventory/IMekanismInventory.java"
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface IEnchantmentsSeerInventory extends IItemHandlerModifiable {

    /**
     * Used to check if an instance of {@link IEnchantmentsSeerInventory} actually has an inventory.
     *
     * @return True if we are actually an inventory.
     *
     * @apiNote If for some reason you are comparing to {@link IEnchantmentsSeerInventory} without having gotten the object via the item handler capability, then you must call
     * this method to make sure that it really is an inventory. As most mekanism tiles have this class in their hierarchy.
     * @implNote If this returns false the capability should not be exposed AND methods should turn reasonable defaults for not doing anything.
     */
    default boolean hasInventory() {
        return true;
    }

    /**
     * Returns the list of IInventorySlots that this inventory exposes on the given side.
     *
     * @param side The side we are interacting with the handler from (null for internal).
     *
     * @return The list of all IInventorySlots that this {@link IEnchantmentsSeerInventory} contains for the given side. If there are no slots for the side or {@link
     * #hasInventory()} is false then it returns an empty list.
     *
     * @implNote When side is null (an internal request), this method <em>MUST</em> return all slots in the inventory. This will be used by the container generating code
     * to add all the proper slots that are needed. Additionally, if {@link #hasInventory()} is false, this <em>MUST</em> return an empty list.
     */
    List<IInventorySlot> getInventorySlots(@Nullable Direction side);

    /**
     * Returns the {@link IInventorySlot} that has the given index from the list of slots on the given side.
     *
     * @param slot The index of the slot to retrieve.
     *
     * @return The {@link IInventorySlot} that has the given index from the list of slots on the given side.
     */
    @Nullable
    default IInventorySlot getInventorySlot(int slot) {
        List<IInventorySlot> slots = getInventorySlots(null);
        return slot >= 0 && slot < slots.size() ? slots.get(slot) : null;
    }

    default Direction getInventorySideFor() {
        return null;
    }

    default void setStackInSlot(int slot, ItemStack stack, @Nullable Direction side) {
        IInventorySlot inventorySlot = getInventorySlot(slot);
        if (inventorySlot != null) {
            inventorySlot.setStack(stack);
        }
    }

    @Override
    default void setStackInSlot(int slot, ItemStack stack) {
        setStackInSlot(slot, stack, getInventorySideFor());
    }

    default int getSlots(@Nullable Direction side) {
        return 0;
    }

    @Override
    default int getSlots() {
        return getSlots(getInventorySideFor());
    }

    default ItemStack getStackInSlot(int slot) {
        IInventorySlot inventorySlot = getInventorySlot(slot);
        return inventorySlot == null ? ItemStack.EMPTY : inventorySlot.getStack();
    }

    /**
     * A sided variant of {@link IItemHandler#insertItem(int, ItemStack, boolean)}, docs copied for convenience.
     *
     * <p>
     * Inserts an {@link ItemStack} into the given slot and return the remainder. The {@link ItemStack} <em>should not</em> be modified in this function!
     * </p>
     * Note: This behaviour is subtly different from {@link net.minecraftforge.fluids.capability.IFluidHandler#fill(net.minecraftforge.fluids.FluidStack,
     * net.minecraftforge.fluids.capability.IFluidHandler.FluidAction)}
     *
     * @param slot   Slot to insert into.
     * @param stack  {@link ItemStack} to insert. This must not be modified by the item handler.
     * @param side   The side we are interacting with the handler from (null for internal).
     * @param action The action to perform, either {@link Action#EXECUTE} or {@link Action#SIMULATE}
     *
     * @return The remaining {@link ItemStack} that was not inserted (if the entire stack is accepted, then return an empty {@link ItemStack}). May be the same as the
     * input {@link ItemStack} if unchanged, otherwise a new {@link ItemStack}. The returned ItemStack can be safely modified after
     *
     * @implNote The {@link ItemStack} <em>should not</em> be modified in this function!
     */
    default ItemStack insertItem(int slot, ItemStack stack, @Nullable Direction side, Action action) {
        return ItemStack.EMPTY; // TODO: Update this to be something meaningful (or update the code to more-closely emulate Mekanism codebase)
    };

    default ItemStack insertItem(int slot, ItemStack stack, boolean simulate) {
        return insertItem(slot, stack, getInventorySideFor(), Action.get(!simulate));
    }

    /**
     * A sided variant of {@link IItemHandler#extractItem(int, int, boolean)}, docs copied for convenience.
     *
     * Extracts an {@link ItemStack} from the given slot.
     * <p>
     * The returned value must be empty if nothing is extracted, otherwise its stack size must be less than or equal to {@code amount} and {@link
     * ItemStack#getMaxStackSize()}.
     * </p>
     *
     * @param slot   Slot to extract from.
     * @param amount Amount to extract (may be greater than the current stack's max limit)
     * @param side   The side we are interacting with the handler from (null for internal).
     * @param action The action to perform, either {@link Action#EXECUTE} or {@link Action#SIMULATE}
     *
     * @return {@link ItemStack} extracted from the slot, must be empty if nothing can be extracted. The returned {@link ItemStack} can be safely modified after, so item
     * handlers should return a new or copied stack.
     *
     * @implNote The returned {@link ItemStack} can be safely modified after, so a new or copied stack should be returned.
     */
    default ItemStack extractItem(int slot, int amount, @Nullable Direction side, Action action) {
        return ItemStack.EMPTY; // TODO: Update this to be something meaningful (or update the code to more-closely emulate Mekanism codebase)
    };

    @Override
    default ItemStack extractItem(int slot, int amount, boolean simulate) {
        return extractItem(slot, amount, getInventorySideFor(), Action.get(!simulate));
    }

    /**
     * A sided variant of {@link IItemHandler#getSlotLimit(int)}, docs copied for convenience.
     *
     * Retrieves the maximum stack size allowed to exist in the given slot.
     *
     * @param slot Slot to query.
     * @param side The side we are interacting with the handler from (null for internal).
     *
     * @return The maximum stack size allowed in the slot.
     */
    default int getSlotLimit(int slot, @Nullable Direction side) {
        return 64; // TODO: Update this to be something meaningful (or update the code to more-closely emulate Mekanism codebase)
    };

    @Override
    default int getSlotLimit(int slot) {
        return getSlotLimit(slot, getInventorySideFor());
    }

    /**
     * A sided variant of {@link IItemHandler#isItemValid(int, ItemStack)}, docs copied for convenience.
     *
     * <p>
     * This function re-implements the vanilla function {@link IInventory#canPlaceItem(int, ItemStack)}. It should be used instead of simulated insertions in cases where
     * the contents and state of the inventory are irrelevant, mainly for the purpose of automation and logic (for instance, testing if a minecart can wait to deposit its
     * items into a full inventory, or if the items in the minecart can never be placed into the inventory and should move on).
     * </p>
     * <ul>
     * <li>isItemValid is false when insertion of the item is never valid.</li>
     * <li>When isItemValid is true, no assumptions can be made and insertion must be simulated case-by-case.</li>
     * <li>The actual items in the inventory, its fullness, or any other state are <strong>not</strong> considered by isItemValid.</li>
     * </ul>
     *
     * @param slot  Slot to query for validity
     * @param stack Stack to test with for validity
     * @param side  The side we are interacting with the handler from (null for internal).
     *
     * @return true if the slot can accept the {@link ItemStack}, not considering the current state of the inventory. false if the slot can never insert the {@link
     * ItemStack} in any situation.
     */
    default boolean isItemValid(int slot, ItemStack stack, @Nullable Direction side) {
        return true; // TODO: Update this to be something meaningful (or update the code to more-closely emulate Mekanism codebase)
    };

    @Override
    default boolean isItemValid(int slot, ItemStack stack) {
        return isItemValid(slot, stack, getInventorySideFor());
    }

}

