package com.nuggylib.enchantmentsseer.common.item;

import net.minecraft.item.ItemStack;
import net.minecraft.item.SimpleFoiledItem;
import org.jetbrains.annotations.NotNull;

public class ItemSeersEnchantedPage extends SimpleFoiledItem {

    public ItemSeersEnchantedPage(Properties properties) {
        super(properties);
    }

    @Override
    public boolean isFoil(@NotNull ItemStack itemStack) {
        return true;
    }

}