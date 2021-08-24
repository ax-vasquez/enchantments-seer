package com.nuggylib.enchantmentsseer.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.SimpleFoiledItem;

public class SeersEnchantedPageItem extends SimpleFoiledItem {

    public SeersEnchantedPageItem(Properties p_i48467_1_) {
        super(p_i48467_1_);
    }

    @Override
    public boolean isFoil(ItemStack p_77636_1_) {
        return true;
    }

}