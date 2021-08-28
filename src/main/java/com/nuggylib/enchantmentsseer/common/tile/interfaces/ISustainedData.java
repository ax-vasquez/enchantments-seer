package com.nuggylib.enchantmentsseer.common.tile.interfaces;

import net.minecraft.item.ItemStack;

import java.util.Map;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/interfaces/ISustainedData.java"
 */
public interface ISustainedData {

    void writeSustainedData(ItemStack itemStack);

    void readSustainedData(ItemStack itemStack);

    //Key is tile save string, value is sustained data string
    Map<String, String> getTileDataRemap();
}
