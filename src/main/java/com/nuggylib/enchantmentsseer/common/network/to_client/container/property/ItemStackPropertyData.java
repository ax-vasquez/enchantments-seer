package com.nuggylib.enchantmentsseer.common.network.to_client.container.property;

import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import com.nuggylib.enchantmentsseer.common.inventory.container.property.PropertyData;
import com.nuggylib.enchantmentsseer.common.inventory.container.property.PropertyType;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from Mekansim; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/network/to_client/container/property/ItemStackPropertyData.java"
 */
public class ItemStackPropertyData extends PropertyData {

    @Nonnull
    private final ItemStack value;

    public ItemStackPropertyData(short property, @Nonnull ItemStack value) {
        super(PropertyType.ITEM_STACK, property);
        this.value = value;
    }

    @Override
    public void handleWindowProperty(EnchantmentsSeerContainer container) {
        container.handleWindowProperty(getProperty(), value);
    }

    @Override
    public void writeToPacket(PacketBuffer buffer) {
        super.writeToPacket(buffer);
        buffer.writeItem(value);
    }
}
