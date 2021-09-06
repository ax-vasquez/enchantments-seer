package com.nuggylib.enchantmentsseer.common.item;

import net.minecraft.item.*;

public class ItemSeersStoneU extends SimpleFoiledItem {

    public ItemSeersStoneU(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON).stacksTo(1).setNoRepair());
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return false;
    }
}
