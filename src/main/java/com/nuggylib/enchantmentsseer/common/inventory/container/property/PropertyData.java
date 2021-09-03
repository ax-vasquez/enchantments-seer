package com.nuggylib.enchantmentsseer.common.inventory.container.property;

import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import net.minecraft.network.PacketBuffer;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_client/container/property/PropertyData.java"
 */
public abstract class PropertyData {

    private final PropertyType type;
    private final short property;

    protected PropertyData(PropertyType type, short property) {
        this.type = type;
        this.property = property;
    }

    public PropertyType getType() {
        return type;
    }

    public short getProperty() {
        return property;
    }

    public abstract void handleWindowProperty(EnchantmentsSeerContainer container);

    public void writeToPacket(PacketBuffer buffer) {
        buffer.writeEnum(type);
        buffer.writeShort(property);
    }

    public static PropertyData fromBuffer(PacketBuffer buffer) {
        PropertyType type = buffer.readEnum(PropertyType.class);
        short property = buffer.readShort();
        return type.createData(property, buffer);
    }
}

