package com.nuggylib.enchantmentsseer.common.item.block;

import com.nuggylib.enchantmentsseer.common.block.prefab.BlockTile;
import com.nuggylib.enchantmentsseer.common.item.interfaces.IItemSustainedInventory;
import com.nuggylib.enchantmentsseer.common.registration.impl.ItemDeferredRegister;

public class ItemBlockSeersEnchantingTable extends ItemBlockEnchantmentsSeer implements IItemSustainedInventory {

    public ItemBlockSeersEnchantingTable(BlockTile<?, ?> block) {
        super(block, ItemDeferredRegister.getMekBaseProperties().stacksTo(1));
    }

}
