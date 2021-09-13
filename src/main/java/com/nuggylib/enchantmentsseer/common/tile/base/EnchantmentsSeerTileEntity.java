package com.nuggylib.enchantmentsseer.common.tile.base;

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
public class EnchantmentsSeerTileEntity extends TileEntity implements ITickableTileEntity, IItemHandler {

    protected final List<IInventorySlot> inventory;

    public EnchantmentsSeerTileEntity(TileEntityType<?> type) {
        super(type);
        inventory = new ArrayList<>();
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

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        IInventorySlot inventorySlot = getInventorySlot(slot);
        return inventorySlot == null ? ItemStack.EMPTY : inventorySlot.getStack();
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
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

    List<IInventorySlot> getInventorySlots() {
        return this.inventory;
    }
}
