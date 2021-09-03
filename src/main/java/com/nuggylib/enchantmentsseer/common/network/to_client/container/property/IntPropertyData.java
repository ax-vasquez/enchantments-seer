package com.nuggylib.enchantmentsseer.common.network.to_client.container.property;

import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import com.nuggylib.enchantmentsseer.common.inventory.container.property.PropertyData;
import com.nuggylib.enchantmentsseer.common.inventory.container.property.PropertyType;
import net.minecraft.network.PacketBuffer;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_client/container/property/IntPropertyData.java"
 */
public class IntPropertyData extends PropertyData {

    private final int value;

    public IntPropertyData(short property, int value) {
        super(PropertyType.INT, property);
        this.value = value;
    }

    @Override
    public void handleWindowProperty(EnchantmentsSeerContainer container) {
        container.handleWindowProperty(getProperty(), value);
    }

    @Override
    public void writeToPacket(PacketBuffer buffer) {
        super.writeToPacket(buffer);
        buffer.writeVarInt(value);
    }
}
