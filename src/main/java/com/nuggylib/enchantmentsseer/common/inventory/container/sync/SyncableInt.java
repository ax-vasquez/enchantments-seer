package com.nuggylib.enchantmentsseer.common.inventory.container.sync;

import com.nuggylib.enchantmentsseer.common.network.to_client.container.property.IntPropertyData;

import java.util.function.IntConsumer;
import java.util.function.IntSupplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * Slightly modified version of {@link net.minecraft.util.IntReferenceHolder}
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/sync/SyncableInt.java"
 */
public abstract class SyncableInt implements ISyncableData {

    private int lastKnownValue;

    public abstract int get();

    public abstract void set(int value);

    @Override
    public DirtyType isDirty() {
        int oldValue = get();
        boolean dirty = oldValue != this.lastKnownValue;
        this.lastKnownValue = oldValue;
        return DirtyType.get(dirty);
    }

    @Override
    public IntPropertyData getPropertyData(short property, DirtyType dirtyType) {
        return new IntPropertyData(property, get());
    }

    public static SyncableInt create(int[] intArray, int idx) {
        return new SyncableInt() {
            @Override
            public int get() {
                return intArray[idx];
            }

            @Override
            public void set(int value) {
                intArray[idx] = value;
            }
        };
    }

    public static SyncableInt create(IntSupplier getter, IntConsumer setter) {
        return new SyncableInt() {

            @Override
            public int get() {
                return getter.getAsInt();
            }

            @Override
            public void set(int value) {
                setter.accept(value);
            }
        };
    }
}
