package com.nuggylib.enchantmentsseer.common.inventory.container.property;

import com.nuggylib.enchantmentsseer.common.inventory.container.sync.ISyncableData;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.SyncableInt;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.SyncableItemStack;
import com.nuggylib.enchantmentsseer.common.network.to_client.container.property.IntPropertyData;
import com.nuggylib.enchantmentsseer.common.network.to_client.container.property.ItemStackPropertyData;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Supplier;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_client/container/property/PropertyType.java"
 */
public enum PropertyType {
    INT(Integer.TYPE, 0, (getter, setter) -> SyncableInt.create(() -> (int) getter.get(), setter::accept),
            (property, buffer) -> new IntPropertyData(property, buffer.readVarInt())),
    ITEM_STACK(ItemStack.class, ItemStack.EMPTY, (getter, setter) -> SyncableItemStack.create(() -> (ItemStack) getter.get(), setter::accept),
            (property, buffer) -> new ItemStackPropertyData(property, buffer.readItem()));

    private final Class<?> type;
    private final Object defaultValue;
    private final BiFunction<Supplier<Object>, Consumer<Object>, ISyncableData> creatorFunction;
    private final BiFunction<Short, PacketBuffer, PropertyData> dataCreatorFunction;

    private static final PropertyType[] VALUES = values();

    PropertyType(Class<?> type, Object defaultValue, BiFunction<Supplier<Object>, Consumer<Object>, ISyncableData> creatorFunction,
                 BiFunction<Short, PacketBuffer, PropertyData> dataCreatorFunction) {
        this.type = type;
        this.defaultValue = defaultValue;
        this.creatorFunction = creatorFunction;
        this.dataCreatorFunction = dataCreatorFunction;
    }

    public <T> T getDefault() {
        return (T) defaultValue;
    }

    public static PropertyType getFromType(Class<?> type) {
        for (PropertyType propertyType : VALUES) {
            if (type == propertyType.type) {
                return propertyType;
            }
        }

        return null;
    }

    public PropertyData createData(short property, PacketBuffer buffer) {
        return dataCreatorFunction.apply(property, buffer);
    }

    public ISyncableData create(Supplier<Object> supplier, Consumer<Object> consumer) {
        return creatorFunction.apply(supplier, consumer);
    }
}
