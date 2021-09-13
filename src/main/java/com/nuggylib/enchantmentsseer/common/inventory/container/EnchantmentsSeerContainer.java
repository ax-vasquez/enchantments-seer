package com.nuggylib.enchantmentsseer.common.inventory.container;

import com.nuggylib.enchantmentsseer.common.inventory.container.slot.HotBarSlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.InventoryContainerSlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.MainInventorySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraft.inventory.container.Slot;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.entity.Entity;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Container class inspired by Mekanism
 *
 * <h2>When a Container is needed</h2>
 * <b>You only need a Container when you need to render a GUI linked to an {@link Entity} or {@link TileEntity}</b>.
 * The slots created in a Container are responsible for things such as menu button clicks, items/fluids being quick-moved,
 * and various other "menu" (as vanilla Minecraft appears to loosely-refer to it as) items. Check the methods given to
 * you by the {@link Container} class.
 *
 * <h2>When a Container <b>ISN'T</b> needed</h2>
 * Basically, for anything that doesn't have a GUI. For example, the basic dirt block in vanilla Minecraft doesn't have
 * a corresponding {@link Container} class since it has no corresponding GUI.
 *
 * <h2>How to use this class</h2>
 * <ol>
 *     <li>Extend this class</li>
 * </ol>
 */
public class EnchantmentsSeerContainer extends Container {

    public static final int BASE_Y_OFFSET = 84;
    /**
     * The number of rows in the default player inventory; used when creating the main inventory slots
     */
    public static final int MAIN_INVENTORY_ROW_COUNT = 3;
    /**
     * The number of columns in both the default player inventory <b>and</b> hot bar. In the case of the hot bar, we
     * only iterate over the column count since there is only one row.
     */
    public static final int PLAYER_INVENTORY_COL_COUNT = 9;

    /**
     * A reference to the player's inventory held while the player is using the corresponding Block's GUI (e.g., has
     * activated the {@link Entity}/{@link TileEntity} by right-clicking).
     */
    protected final PlayerInventory inv;
    /**
     * A list of slots corresponding to the {@link Entity}/{@link TileEntity} inventory. This is a separate list from
     * the target inventory. In our mod's current architecture, subclasses of the {@link EnchantmentsSeerContainer} class
     * <b>should not</b> directly-add item or fluid slots. Instead, define the inventory slots as you see fit within the
     * {@link Entity}/{@link TileEntity} - from there, the slots should get automatically-added to the container after
     * it reads in the corresponding inventory and creates the slots as-needed.
     *
     * The {@link #addSlot(Slot)} method contains the logic to determine which list a given slot should go to.
     */
    protected final List<InventoryContainerSlot> inventoryContainerSlots = new ArrayList<>();
    /**
     * A list of slots for the player's main inventory. This list should simply reflect the size and contents of the player's
     * inventory.
     */
    protected final List<MainInventorySlot> mainInventorySlots = new ArrayList<>();
    /**
     * A list of slots for the player's hot bar slots. It should simply reflect the size and contents of the player's hot
     * bar.
     */
    protected final List<HotBarSlot> hotBarSlots = new ArrayList<>();

    protected EnchantmentsSeerContainer(ContainerType<?> type, int id, PlayerInventory inv) {
        super(type, id);
        this.inv = inv;
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        //Is this the proper default
        //TODO - 10.1: Re-evaluate this and maybe add in some distance based checks??
        return true;
    }

    /**
     * General-purpose add-slot logic to be used by any of the subclasses.
     *
     * @param slot          The {@link Slot} to add
     * @return              The {@link Slot} originally passed to the method
     */
    @Nonnull
    @Override
    protected Slot addSlot(@Nonnull Slot slot) {
        super.addSlot(slot);
        if (slot instanceof InventoryContainerSlot) {
            inventoryContainerSlots.add((InventoryContainerSlot) slot);
        } else if (slot instanceof MainInventorySlot) {
            mainInventorySlots.add((MainInventorySlot) slot);
        } else if (slot instanceof HotBarSlot) {
            hotBarSlots.add((HotBarSlot) slot);
        }
        return slot;
    }

    protected HotBarSlot createHotBarSlot(@Nonnull PlayerInventory inv, int index, int x, int y) {
        return new HotBarSlot(inv, index, x, y);
    }

    protected void addSlots() {
    }

    protected void openInventory(@Nonnull PlayerInventory inv) {
    }

    /**
     * Helper method to create the Container slots for the "main inventory" (the items that the player has in their
     * inventory) and the hot bar slots.
     *
     * @param inv           The player's inventory
     */
    protected void addInventorySlots(@Nonnull PlayerInventory inv) {
        int yOffset = getInventoryYOffset();
        int xOffset = getInventoryXOffset();
        for (int slotY = 0; slotY < MAIN_INVENTORY_ROW_COUNT; slotY++) {
            for (int slotX = 0; slotX < PLAYER_INVENTORY_COL_COUNT; slotX++) {
                addSlot(new MainInventorySlot(inv, PlayerInventory.getSelectionSize() + slotX + slotY * 9, xOffset + slotX * 18, yOffset + slotY * 18));
            }
        }
        yOffset += 58;
        // TODO: See if there is a reason Mekanism used this particular method to get the count rather than hard-coding
        //  as they did with the main inventory slots
        // Note that PlayerInventory.getSelectionSize() always returns 9
        for (int slotX = 0; slotX < PlayerInventory.getSelectionSize(); slotX++) {
            addSlot(createHotBarSlot(inv, slotX, xOffset + slotX * 18, yOffset));
        }
    }

    // TODO: Find out/document why Mekanism uses this constant as the offset
    protected int getInventoryYOffset() {
        return BASE_Y_OFFSET;
    }

    // TODO: Find out/document why Mekanism uses 8 as the offset
    protected int getInventoryXOffset() {
        return 8;
    }

    /**
     * Adds slots and opens, must be called at end of extending classes constructors
     *
     * TODO: This is clearly an important method - document all of the use cases and how to use it
     */
    protected void addSlotsAndOpen() {
        addSlots();
        addInventorySlots(inv);
        openInventory(inv);
    }

}
