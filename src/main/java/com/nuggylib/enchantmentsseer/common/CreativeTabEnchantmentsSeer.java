package com.nuggylib.enchantmentsseer.common;

import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerItems;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;

public class CreativeTabEnchantmentsSeer extends ItemGroup {

    public CreativeTabEnchantmentsSeer() {
        super(EnchantmentsSeer.MOD_ID);
    }

    @Nonnull
    @Override
    public ItemStack makeIcon() {
        ItemStack stack = new ItemStack(EnchantmentsSeerItems.SEERS_MANUSCRIPT.get());
        return stack;
    }
}
