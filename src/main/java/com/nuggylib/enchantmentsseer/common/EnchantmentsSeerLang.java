package com.nuggylib.enchantmentsseer.common;

import com.nuggylib.enchantmentsseer.client.render.text.ILangEntry;
import net.minecraft.util.Util;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/MekanismLang.java"
 */
public enum EnchantmentsSeerLang implements ILangEntry {
    CLOSE("gui", "close"),
    ENCHANTMENTS_SEER("constants", "mod_name"),
    EMPTY("gui", "empty"),
    SIDE_DATA_NONE("side_data", "none"),
    SIDE_DATA_INPUT("side_data", "input"),
    SIDE_DATA_INPUT_OUTPUT("side_data", "input_output"),
    SIDE_DATA_OUTPUT("side_data", "output"),
    // Descriptions
    DESCRIPTION_SEERS_ENCHANTING_TABLE("description", "seers_enchanting_table"),
    // Seers enchanting table
    ENCHANT_ITEM("enchant_item", "short"),
    ENCHANT_REAGENT("enchant_reagent", "short"),
    // Transmission types
    TRANSMISSION_TYPE_ITEM("transmission", "items"),
    //Connection Type
    CONNECTION_NORMAL("connection", "normal"),
    CONNECTION_PUSH("connection", "push"),
    CONNECTION_PULL("connection", "pull"),
    CONNECTION_NONE("connection", "none"),
    ;

    private final String key;

    EnchantmentsSeerLang(String type, String path) {
        this(Util.makeDescriptionId(type, EnchantmentsSeer.rl(path)));
    }

    EnchantmentsSeerLang(String key) {
        this.key = key;
    }

    @Override
    public String getTranslationKey() {
        return key;
    }
}
