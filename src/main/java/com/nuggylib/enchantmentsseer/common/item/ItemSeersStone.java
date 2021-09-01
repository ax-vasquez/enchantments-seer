package com.nuggylib.enchantmentsseer.common.item;

import net.minecraft.item.*;

public class ItemSeersStone extends SimpleFoiledItem {

    public ItemSeersStone(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON).stacksTo(1).setNoRepair());
    }

    @Override
    public boolean isFoil(ItemStack itemStack) {
        return true;
    }
}
