package com.nuggylib.enchantmentsseer.common.inventory.container.sync;

import com.nuggylib.enchantmentsseer.api.annotations.NonNull;
import com.nuggylib.enchantmentsseer.common.network.to_client.container.property.IntPropertyData;
import it.unimi.dsi.fastutil.ints.Int2ObjectFunction;

import javax.annotation.Nonnull;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class SyncableEnum<ENUM extends Enum<ENUM>> implements ISyncableData {

    public static <ENUM extends Enum<ENUM>> SyncableEnum<ENUM> create(Int2ObjectFunction<ENUM> decoder, @Nonnull ENUM defaultValue, Supplier<@NonNull ENUM> getter,
                                                                      Consumer<@NonNull ENUM> setter) {
        return new SyncableEnum<>(decoder, defaultValue, getter, setter);
    }

    private final Int2ObjectFunction<ENUM> decoder;
    private final Supplier<@NonNull ENUM> getter;
    private final Consumer<@NonNull ENUM> setter;
    @Nonnull
    private ENUM lastKnownValue;

    private SyncableEnum(Int2ObjectFunction<ENUM> decoder, @Nonnull ENUM defaultValue, Supplier<@NonNull ENUM> getter, Consumer<@NonNull ENUM> setter) {
        this.decoder = decoder;
        this.lastKnownValue = defaultValue;
        this.getter = getter;
        this.setter = setter;
    }

    @Nonnull
    public ENUM get() {
        return getter.get();
    }

    public void set(int ordinal) {
        set(decoder.apply(ordinal));
    }

    public void set(@Nonnull ENUM value) {
        setter.accept(value);
    }

    @Override
    public DirtyType isDirty() {
        ENUM oldValue = get();
        boolean dirty = oldValue != this.lastKnownValue;
        this.lastKnownValue = oldValue;
        return DirtyType.get(dirty);
    }

    @Override
    public IntPropertyData getPropertyData(short property, DirtyType dirtyType) {
        return new IntPropertyData(property, get().ordinal());
    }
}
