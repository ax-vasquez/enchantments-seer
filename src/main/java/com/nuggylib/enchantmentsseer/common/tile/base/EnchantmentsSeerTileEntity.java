package com.nuggylib.enchantmentsseer.common.tile.base;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.IInventorySlotHolder;
import com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager.ItemHandlerManager;
import com.nuggylib.enchantmentsseer.common.inventory.IInventorySlot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.world.World;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Inspired by Mekansim
 *
 * This is a more "Forge-ready" version of the {@link TileEntity} class and adds core functionality such as:
 * <ol>
 *     <li>Handle "tick" logic</li>
 *     <li>General inventory logic (e.g., getting/setting {@link ItemStack}s)</li>
 * </ol>
 */
public abstract class EnchantmentsSeerTileEntity extends TileEntity implements ITickableTileEntity, IItemHandler {

    protected final List<IInventorySlot> inventory;

    //Variables for handling ITileContainer
    protected final ItemHandlerManager itemHandlerManager;

    public EnchantmentsSeerTileEntity(TileEntityType<?> type) {
        super(type);
        inventory = new ArrayList<>();
        itemHandlerManager = new ItemHandlerManager(getInitialInventory(), this);
    }

    @Override
    public void tick() {
        if (!isRemote()) {
            // TODO: client-side tick logic
        } else {
            // TODO: server-side tick logic
        }
    }

    /**
     * Gets the <b>count</b> of the total number of slots (not the actual contents of the slots)
     *
     * @return  The total number of slots in the {@link TileEntity}'s inventory
     */
    @Override
    public int getSlots() {
        return this.inventory.size();
    }

    /**
     * Gets the {@link ItemStack} of the item in the given slot index in the {@link TileEntity}'s inventory.
     *
     * If there is nothing in the given slot, an empty ItemStack is returned.
     *
     * @param slot          The index of the slot to get
     * @return              The ItemStack of the item in the slot, or an empty ItemStack if the slot is unoccupied
     */
    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        IInventorySlot inventorySlot = getInventorySlot(slot);
        return inventorySlot == null ? ItemStack.EMPTY : inventorySlot.getStack();
    }

    // TODO: Clarify if this is actively called (e.g., "to do an insert"), or if it gets invoked in response to an item
    //  being inserted (e.g., an "item was inserted" event)
    /**
     * Insert an item into the given slot
     *
     * @param slot                  The slot in which an ItemStack is being inserted
     * @param stack                 The ItemStack being inserted into the target slot
     * @param simulate              <i>Not used in our implementation</i>
     * @return                      The ItemStack
     */
    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        IInventorySlot inventorySlot = getInventorySlot(slot);
        if (inventorySlot == null) {
            return stack;
        }
        return inventorySlot.insertItem(stack);
    }

    /**
     * Extract an item from the given slot
     *
     * @param slot                  The slot to extract items from
     * @param amount                The number of items to extract
     * @param simulate              <i>Not used in our implementation</i>
     * @return                      The ItemStack
     */
    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        IInventorySlot inventorySlot = getInventorySlot(slot);
        if (inventorySlot == null) {
            return ItemStack.EMPTY;
        }
        return inventorySlot.extractItem(amount);
    }

    /**
     * Determines the max item count for the given slot
     *
     * @param slot                  The slot to get the max item count for
     * @return                      The max stack count for the item in the target inventory slot (frequently 64, but can be other values)
     */
    @Override
    public int getSlotLimit(int slot) {
        IInventorySlot inventorySlot = getInventorySlot(slot);
        return inventorySlot == null ? 0 : inventorySlot.getLimit(ItemStack.EMPTY);
    }

    /**
     * Tests if the given ItemStack is valid input for the target slot
     *
     * @param slot                  The slot to check the input against
     * @param stack                 The ItemStack to check for validity
     * @return                      Whether the given ItemStack can be inserted into the target slot (<pre>true</pre>) or not (<pre>false</pre>)
     */
    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        IInventorySlot inventorySlot = getInventorySlot(slot);
        return inventorySlot != null && inventorySlot.isItemValid(stack);
    }

    /**
     * Like getWorld(), but for when you _know_ world won't be null
     *
     * @return The world!
     */
    @Nonnull
    protected World getWorldNN() {
        return Objects.requireNonNull(getLevel(), "getWorldNN called before world set");
    }

    public boolean isRemote() {
        return getWorldNN().isClientSide();
    }

    @Nullable
    protected IInventorySlot getInventorySlot(int slot) {
        List<IInventorySlot> slots = getInventorySlots();
        return slot >= 0 && slot < slots.size() ? slots.get(slot) : null;
    }

    @Nullable
    protected IInventorySlotHolder getInitialInventory() {
        return null;
    }

    List<IInventorySlot> getInventorySlots() {
        return this.inventory;
    }
}
