package com.nuggylib.enchantmentsseer.common.registries;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.item.ItemSeersManuscript;
import com.nuggylib.enchantmentsseer.common.item.ItemSeersStone;
import com.nuggylib.enchantmentsseer.common.registration.impl.ItemDeferredRegister;
import com.nuggylib.enchantmentsseer.common.registration.impl.ItemRegistryObject;
import com.nuggylib.enchantmentsseer.common.resource.IResource;
import com.nuggylib.enchantmentsseer.common.resource.PrimaryResource;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;
import com.nuggylib.enchantmentsseer.common.resource.ResourceType;
import net.minecraft.item.Item;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registries/MekanismItems.java"
 */
public class EnchantmentsSeerItems {

    private EnchantmentsSeerItems() {
    }

    public static final ItemDeferredRegister ITEMS = new ItemDeferredRegister(EnchantmentsSeer.MOD_ID);
    public static final Table<ResourceType, PrimaryResource, ItemRegistryObject<Item>> PROCESSED_RESOURCES = HashBasedTable.create();

    public static final ItemRegistryObject<Item> SEERS_STONE = ITEMS.register("seers_stone", ItemSeersStone::new);
    public static final ItemRegistryObject<Item> SEERS_MANUSCRIPT = ITEMS.register("seers_manuscript", ItemSeersManuscript::new);

    static {
        for (ResourceType type : EnumUtils.RESOURCE_TYPES) {
            for (PrimaryResource resource : EnumUtils.PRIMARY_RESOURCES) {
                if (resource.has(type)) {
                    PROCESSED_RESOURCES.put(type, resource, ITEMS.register(type.getRegistryPrefix() + "_" + resource.getName()));
                }
            }
        }
    }

    private static ItemRegistryObject<Item> registerResource(ResourceType type, IResource resource) {
        return ITEMS.register(type.getRegistryPrefix() + "_" + resource.getRegistrySuffix());
    }
}
