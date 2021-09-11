package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.SeersEnchantingTableContainer;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class EnchantmentsSeerContainerTypes {

    private EnchantmentsSeerContainerTypes() {
    }

    public static final DeferredRegister<ContainerType<?>> CONTAINER_TYPES = DeferredRegister.create(ForgeRegistries.CONTAINERS, EnchantmentsSeer.MOD_ID);

    public static final RegistryObject<ContainerType<SeersEnchantingTableContainer>> SEERS_ENCHANTING_TABLE = CONTAINER_TYPES.register("seers_enchanting_table", () -> new ContainerType<>(SeersEnchantingTableContainer::new));

}
