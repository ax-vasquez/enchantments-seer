package com.nuggylib.enchantmentsseer.common.lib.transmitter;

import com.nuggylib.enchantmentsseer.client.render.text.IHasTranslationKey;
import com.nuggylib.enchantmentsseer.client.render.text.ILangEntry;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import com.nuggylib.enchantmentsseer.common.content.network.transmitter.Transmitter;
import com.nuggylib.enchantmentsseer.common.tile.transmitter.TileEntityTransmitter;

public enum TransmissionType implements IHasTranslationKey {
    ITEM("InventoryNetwork", "items", EnchantmentsSeerLang.TRANSMISSION_TYPE_ITEM);

    private final String name;
    private final String transmission;
    private final ILangEntry langEntry;

    TransmissionType(String name, String transmission, ILangEntry langEntry) {
        this.name = name;
        this.transmission = transmission;
        this.langEntry = langEntry;
    }

    public String getName() {
        return name;
    }

    public String getTransmission() {
        return transmission;
    }

    public ILangEntry getLangEntry() {
        return langEntry;
    }

    @Override
    public String getTranslationKey() {
        return langEntry.getTranslationKey();
    }

    public boolean checkTransmissionType(Transmitter<?, ?, ?> transmitter) {
        return transmitter.getSupportedTransmissionTypes().contains(this);
    }

    public boolean checkTransmissionType(TileEntityTransmitter transmitter) {
        return checkTransmissionType(transmitter.getTransmitter());
    }
}
