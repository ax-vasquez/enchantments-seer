package com.nuggylib.enchantmentsseer.common;

import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/CreativeTabMekanism.java"
 */
public class CreativeTabEnchantmentsSeer extends ItemGroup {

    public CreativeTabEnchantmentsSeer() {
        super(EnchantmentsSeer.MOD_ID);
    }

    @Nonnull
    @Override
    public ItemStack makeIcon() {
        return EnchantmentsSeerItems.SEERS_MANUSCRIPT.getItemStack();
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        //Overwrite the lang key to match the one representing Mekanism
        return EnchantmentsSeerLang.ENCHANTMENTS_SEER.translate();
    }
}
