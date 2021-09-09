package com.nuggylib.enchantmentsseer.common.inventory.container;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.property.PropertyData;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.*;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.ISyncableData;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.ISyncableData.DirtyType;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.SyncableInt;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.SyncableItemStack;
import com.nuggylib.enchantmentsseer.common.network.to_client.container.PacketUpdateContainer;
import com.nuggylib.enchantmentsseer.common.registration.impl.ContainerTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.util.StackUtils;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerListener;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IntReferenceHolder;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;

/**
 * Inspired by the Mekanism codebase
 *
 * This is our mod-specific container.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/MekanismContainer.java"
 */
public abstract class EnchantmentsSeerContainer extends Container {

    public static final int BASE_Y_OFFSET = 84;
    public static final int TRANSPORTER_CONFIG_WINDOW = 0;
    public static final int SIDE_CONFIG_WINDOW = 1;
    public static final int UPGRADE_WINDOW = 2;
    protected final List<MainInventorySlot> mainInventorySlots = new ArrayList<>();
    protected final List<HotBarSlot> hotBarSlots = new ArrayList<>();

    protected final PlayerInventory inv;
    protected final List<InventoryContainerSlot> inventoryContainerSlots = new ArrayList<>();
    private final List<ISyncableData> trackedData = new ArrayList<>();
    /**
     * Keeps track of which window the player has open. Only used on the client, so doesn't need to keep track of other players.
     *
     * @apiNote Don't set this directly use the {@link #setSelectedWindow(SelectedWindowData)} instead, this is just protected so that the QIO item viewer container can
     * copy it directly to the new container.
     */
    @Nullable
    protected SelectedWindowData selectedWindow;
    /**
     * Only used on the server
     */
    private Map<UUID, SelectedWindowData> selectedWindows;
    private final Map<Object, List<ISyncableData>> specificTrackedData = new Object2ObjectOpenHashMap<>();

    protected EnchantmentsSeerContainer(ContainerTypeRegistryObject<?> type, int id, PlayerInventory inv) {
        super(type.getContainerType(), id);
        this.inv = inv;
        if (!this.inv.player.level.isClientSide) {
            //Only keep track of uuid based selected grids on the server (we use a size of one as for the most part containers are actually 1:1)
            selectedWindows = new HashMap<>(1);
        }
    }

    public void startTracking(Object key, ISpecificContainerTracker tracker) {
        List<ISyncableData> list = tracker.getSpecificSyncableData();
        list.forEach(this::track);
        specificTrackedData.put(key, list);
    }

    public void stopTracking(Object key) {
        List<ISyncableData> list = specificTrackedData.get(key);
        if (list != null) {
            list.forEach(trackedData::remove);
            specificTrackedData.remove(key);
        }
    }

    @Nonnull
    @Override
    protected Slot addSlot(@Nonnull Slot slot) {
        //Manually handle the code that is in super.addSlot so that we do not end up adding extra elements to
        // inventoryItemStacks as we handle the tracking/sync changing via the below track call. This way we are
        // able to minimize the amount of overhead that we end up with due to keeping track of the stack in SyncableItemStack
        slot.index = slots.size();
        slots.add(slot);
        if (slot instanceof InventoryContainerSlot) {
            inventoryContainerSlots.add((InventoryContainerSlot) slot);
        }
        return slot;
    }

    /**
     * Adds slots and opens, must be called at end of extending classes constructors
     */
    protected void addSlotsAndOpen() {
        addSlots();
        addInventorySlots(inv);
        openInventory(inv);
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        //Is this the proper default
        //TODO - 10.1: Re-evaluate this and maybe add in some distance based checks??
        return true;
    }

    @Override
    public boolean canTakeItemForPickAll(@Nonnull ItemStack stack, @Nonnull Slot slot) {
        if (slot instanceof IInsertableSlot) {
            IInsertableSlot insertableSlot = (IInsertableSlot) slot;
            if (!insertableSlot.canMergeWith(stack)) {
                return false;
            }
            SelectedWindowData selectedWindow = inv.player.level.isClientSide ? getSelectedWindow() : getSelectedWindow(inv.player.getUUID());
            return insertableSlot.exists(selectedWindow) && super.canTakeItemForPickAll(stack, slot);
        }
        return super.canTakeItemForPickAll(stack, slot);
    }

    @Override
    public void removed(@Nonnull PlayerEntity player) {
        super.removed(player);
        closeInventory(player);
    }

    protected void closeInventory(@Nonnull PlayerEntity player) {
        if (!player.level.isClientSide()) {
            clearSelectedWindow(player.getUUID());
        }
    }

    protected void openInventory(@Nonnull PlayerInventory inv) {
    }

    protected int getInventoryYOffset() {
        return BASE_Y_OFFSET;
    }

    protected int getInventoryXOffset() {
        return 8;
    }

    protected void addInventorySlots(@Nonnull PlayerInventory inv) {
        int yOffset = getInventoryYOffset();
        int xOffset = getInventoryXOffset();
        for (int slotY = 0; slotY < 3; slotY++) {
            for (int slotX = 0; slotX < 9; slotX++) {
                addSlot(new MainInventorySlot(inv, PlayerInventory.getSelectionSize() + slotX + slotY * 9, xOffset + slotX * 18, yOffset + slotY * 18));
            }
        }
        yOffset += 58;
        for (int slotX = 0; slotX < PlayerInventory.getSelectionSize(); slotX++) {
            addSlot(createHotBarSlot(inv, slotX, xOffset + slotX * 18, yOffset));
        }
    }

    protected HotBarSlot createHotBarSlot(@Nonnull PlayerInventory inv, int index, int x, int y) {
        return new HotBarSlot(inv, index, x, y);
    }

    protected void addSlots() {
    }

    public List<InventoryContainerSlot> getInventoryContainerSlots() {
        return Collections.unmodifiableList(inventoryContainerSlots);
    }

    public List<MainInventorySlot> getMainInventorySlots() {
        return Collections.unmodifiableList(mainInventorySlots);
    }

    public List<HotBarSlot> getHotBarSlots() {
        return Collections.unmodifiableList(hotBarSlots);
    }

    /**
     * @param slots          Slots to insert into
     * @param stack          Stack to insert (do not modify).
     * @param ignoreEmpty    {@code true} to ignore/skip empty slots.
     * @param selectedWindow Selected window, or null if there is no window selected. This mostly only really matters in relation to VirtualInventoryContainerSlots
     *
     * @return Remainder
     */
    public static <SLOT extends Slot & IInsertableSlot> ItemStack insertItem(List<SLOT> slots, @Nonnull ItemStack stack, boolean ignoreEmpty,
                                                                             @Nullable SelectedWindowData selectedWindow) {
        return insertItem(slots, stack, ignoreEmpty, selectedWindow, Action.EXECUTE);
    }

    /**
     * @param slots          Slots to insert into
     * @param stack          Stack to insert (do not modify).
     * @param ignoreEmpty    {@code true} to ignore/skip empty slots.
     * @param selectedWindow Selected window, or null if there is no window selected. This mostly only really matters in relation to VirtualInventoryContainerSlots
     *
     * @return Remainder
     */
    @Nonnull
    public static <SLOT extends Slot & IInsertableSlot> ItemStack insertItem(List<SLOT> slots, @Nonnull ItemStack stack, boolean ignoreEmpty,
                                                                             @Nullable SelectedWindowData selectedWindow, Action action) {
        if (stack.isEmpty()) {
            //Skip doing anything if the stack is already empty.
            // Makes it easier to chain calls, rather than having to check if the stack is empty after our previous call
            return stack;
        }
        for (SLOT slot : slots) {
            if (ignoreEmpty && !slot.hasItem()) {
                //Skip checking empty stacks if we want to ignore them
                continue;
            } else if (!slot.exists(selectedWindow)) {
                // or if the slot doesn't "exist" for the current window configuration
                continue;
            }
            stack = slot.insertItem(stack, action);
            if (stack.isEmpty()) {
                break;
            }
        }
        return stack;
    }

    /**
     * N.B.
     *
     * This method needs to be overridden because, without it, the default implementation (in {@link Container})
     * attempts to access values in the lastSlots field, which has no contents in our implementation. Without
     * overriding this method, you'd encounter "Index: 0, Size: 0" errors when attempting to open container-linked
     * GUIs.
     */
    @Override
    public void broadcastChanges() {
        // TODO: See if we should do more here other than simply overriding the super class' method
        if (!containerListeners.isEmpty()) {
            //Only check tracked data for changes if we actually have any listeners
            List<PropertyData> dirtyData = new ArrayList<>();
            for (short i = 0; i < trackedData.size(); i++) {
                ISyncableData data = trackedData.get(i);
                DirtyType dirtyType = data.isDirty();
                if (dirtyType != DirtyType.CLEAN) {
                    dirtyData.add(data.getPropertyData(i, dirtyType));
                }
            }
            if (!dirtyData.isEmpty()) {
                sendChange(new PacketUpdateContainer((short) containerId, dirtyData));
            }
        }
    }

    private <MSG> void sendChange(MSG packet) {
        for (IContainerListener listener : containerListeners) {
            if (listener instanceof ServerPlayerEntity) {
                EnchantmentsSeer.packetHandler.sendTo(packet, (ServerPlayerEntity) listener);
            }
        }
    }

    //Start container sync management
    public void track(ISyncableData data) {
        trackedData.add(data);
    }

    @Override
    public void addSlotListener(@Nonnull IContainerListener listener) {
        boolean alreadyHas = containerListeners.contains(listener);
        super.addSlotListener(listener);
        if (!alreadyHas && listener instanceof ServerPlayerEntity) {
            //Send all contents to the listener when it first gets added
            List<PropertyData> dirtyData = new ArrayList<>();
            for (short i = 0; i < trackedData.size(); i++) {
                dirtyData.add(trackedData.get(i).getPropertyData(i, DirtyType.DIRTY));
            }
            if (!dirtyData.isEmpty()) {
                EnchantmentsSeer.packetHandler.sendTo(new PacketUpdateContainer((short) containerId, dirtyData), (ServerPlayerEntity) listener);
            }
        }
    }

    @Nonnull
    protected ItemStack transferSuccess(@Nonnull Slot currentSlot, @Nonnull PlayerEntity player, @Nonnull ItemStack slotStack, @Nonnull ItemStack stackToInsert) {
        int difference = slotStack.getCount() - stackToInsert.getCount();
        currentSlot.remove(difference);
        ItemStack newStack = StackUtils.size(slotStack, difference);
        currentSlot.onTake(player, newStack);
        return newStack;
    }

    /**
     * @apiNote Only call on client
     */
    @Nullable
    public SelectedWindowData getSelectedWindow() {
        return selectedWindow;
    }

    /**
     * @apiNote Only call on server
     */
    @Nullable
    public SelectedWindowData getSelectedWindow(UUID player) {
        return selectedWindows.get(player);
    }

    /**
     * @apiNote Only call on client
     */
    public void setSelectedWindow(@Nullable SelectedWindowData selectedWindow) {
        if (!Objects.equals(this.selectedWindow, selectedWindow)) {
            this.selectedWindow = selectedWindow;
        }
    }

    /**
     * @apiNote Only call on server
     */
    public void setSelectedWindow(UUID player, @Nullable SelectedWindowData selectedWindow) {
        if (selectedWindow == null) {
            clearSelectedWindow(player);
        } else {
            selectedWindows.put(player, selectedWindow);
        }
    }

    /**
     * @apiNote Only call on server
     */
    private void clearSelectedWindow(UUID player) {
        selectedWindows.remove(player);
    }

    public void handleWindowProperty(short property, @Nonnull ItemStack value) {
        ISyncableData data = trackedData.get(property);
        if (data instanceof SyncableItemStack) {
            ((SyncableItemStack) data).set(value);
        }
    }

    public void handleWindowProperty(short property, int value) {
        ISyncableData data = trackedData.get(property);
        if (data instanceof SyncableInt) {
            ((SyncableInt) data).set(value);
        } else if (data instanceof SyncableItemStack) {
            ((SyncableItemStack) data).set(value);
        }
    }

    /**
     * {@inheritDoc}
     *
     * @return The contents in this slot AFTER transferring items away.
     */
    @Nonnull
    @Override
    public ItemStack quickMoveStack(@Nonnull PlayerEntity player, int slotID) {
        EnchantmentsSeer.logger.info("Quick move stack");
        Slot currentSlot = slots.get(slotID);
        if (currentSlot == null || !currentSlot.hasItem()) {
            return ItemStack.EMPTY;
        }
        SelectedWindowData selectedWindow = player.level.isClientSide ? getSelectedWindow() : getSelectedWindow(player.getUUID());
        if (currentSlot instanceof IInsertableSlot && !((IInsertableSlot) currentSlot).exists(selectedWindow)) {
            return ItemStack.EMPTY;
        }
        ItemStack slotStack = currentSlot.getItem();
        ItemStack stackToInsert = slotStack;
        if (currentSlot instanceof InventoryContainerSlot) {
            //Insert into stacks that already contain an item in the order hot bar -> main inventory
            stackToInsert = insertItem(hotBarSlots, stackToInsert, true, selectedWindow);
            stackToInsert = insertItem(mainInventorySlots, stackToInsert, true, selectedWindow);
            //If we still have any left then input into the empty stacks in the order of main inventory -> hot bar
            // Note: Even though we are doing the main inventory, we still need to do both, ignoring empty then not instead of
            // just directly inserting into the main inventory, in case there are empty slots before the one we can stack with
            stackToInsert = insertItem(hotBarSlots, stackToInsert, false, selectedWindow);
            stackToInsert = insertItem(mainInventorySlots, stackToInsert, false, selectedWindow);
        } else {
            //We are in the main inventory or the hot bar
            //Start by trying to insert it into the tile's inventory slots, first attempting to stack with other items
            stackToInsert = insertItem(inventoryContainerSlots, stackToInsert, true, selectedWindow);
            if (slotStack.getCount() == stackToInsert.getCount()) {
                //Then as long as if we still have the same number of items (failed to insert), try to insert it into the tile's inventory slots allowing for empty items
                stackToInsert = insertItem(inventoryContainerSlots, stackToInsert, false, selectedWindow);
                if (slotStack.getCount() == stackToInsert.getCount()) {
                    //Else if we failed to do that also, try transferring to armor inventory, main inventory or the hot bar, depending which one we currently are in
                    if (currentSlot instanceof MainInventorySlot) {
                        stackToInsert = insertItem(hotBarSlots, stackToInsert, true, selectedWindow);
                        stackToInsert = insertItem(hotBarSlots, stackToInsert, false, selectedWindow);
                    } else if (currentSlot instanceof HotBarSlot) {
                        stackToInsert = insertItem(mainInventorySlots, stackToInsert, true, selectedWindow);
                        stackToInsert = insertItem(mainInventorySlots, stackToInsert, false, selectedWindow);
                    } else {
                        //TODO: Should we add a warning message so we can find out if we ever end up here. (Given we should never end up here anyways)
                    }
                }
            }
        }
        if (stackToInsert.getCount() == slotStack.getCount()) {
            //If nothing changed then return that fact
            return ItemStack.EMPTY;
        }
        //Otherwise decrease the stack by the amount we inserted, and return it as a new stack for what is now in the slot
        return transferSuccess(currentSlot, player, slotStack, stackToInsert);
    }

    public interface ISpecificContainerTracker {

        List<ISyncableData> getSpecificSyncableData();
    }
}
