package com.nuggylib.enchantmentsseer.common.inventory.container.sync;

import com.nuggylib.enchantmentsseer.api.annotations.NonNull;
import com.nuggylib.enchantmentsseer.common.inventory.container.property.PropertyData;
import com.nuggylib.enchantmentsseer.common.network.to_client.container.property.IntPropertyData;
import com.nuggylib.enchantmentsseer.common.network.to_client.container.property.ItemStackPropertyData;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * Version of {@link net.minecraft.util.IntReferenceHolder} for handling item stacks
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/sync/SyncableItemStack.java"
 */
public class SyncableItemStack implements ISyncableData {

    public static SyncableItemStack create(Supplier<@NonNull ItemStack> getter, Consumer<@NonNull ItemStack> setter) {
        return new SyncableItemStack(getter, setter);
    }

    private final Supplier<@NonNull ItemStack> getter;
    private final Consumer<@NonNull ItemStack> setter;
    @Nonnull
    private ItemStack lastKnownValue = ItemStack.EMPTY;

    private SyncableItemStack(Supplier<@NonNull ItemStack> getter, Consumer<@NonNull ItemStack> setter) {
        this.getter = getter;
        this.setter = setter;
    }

    @Nonnull
    public ItemStack get() {
        return getter.get();
    }

    public void set(@Nonnull ItemStack value) {
        setter.accept(value);
    }

    public void set(int amount) {
        ItemStack stack = get();
        if (!stack.isEmpty()) {
            //Double check it is not empty
            stack.setCount(amount);
        }
    }

    @Override
    public DirtyType isDirty() {
        ItemStack value = get();
        if (value.isEmpty() && this.lastKnownValue.isEmpty()) {
            //If they are both empty, we don't need to update anything they are identical
            // Note: isItemEqual returns false if one is empty, even if the other may also be empty
            return DirtyType.CLEAN;
        }
        //TODO: Should same item be replaced with ItemHandlerHelper#canItemStacksStack so that we take cap NBT into account?
        // Cap NBT isn't synced so in a sense it doesn't matter, though maybe it will at some point?
        boolean sameItem = value.sameItem(this.lastKnownValue) && ItemStack.tagMatches(value, this.lastKnownValue);
        if (!sameItem || value.getCount() != this.lastKnownValue.getCount()) {
            //Make sure to copy it in case our item stack object is the same object so would be getting modified
            // only do so though if it is dirty, as we don't need to spam object creation
            this.lastKnownValue = value.copy();
            return sameItem ? DirtyType.SIZE : DirtyType.DIRTY;
        }
        return DirtyType.CLEAN;
    }

    @Override
    public PropertyData getPropertyData(short property, DirtyType dirtyType) {
        if (dirtyType == DirtyType.SIZE) {
            //If only the size changed, don't bother re-syncing the type
            return new IntPropertyData(property, get().getCount());
        }
        return new ItemStackPropertyData(property, get());
    }
}
