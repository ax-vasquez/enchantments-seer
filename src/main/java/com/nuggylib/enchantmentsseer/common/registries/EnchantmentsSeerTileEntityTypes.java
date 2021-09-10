package com.nuggylib.enchantmentsseer.common.registries;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.tile.SeersEnchantingTableTileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * @see "https://mcforge.readthedocs.io/en/latest/tileentities/tileentity/#creating-a-tileentity"
 */
public class EnchantmentsSeerTileEntityTypes {

    private EnchantmentsSeerTileEntityTypes() {

    }

    public static final DeferredRegister<TileEntityType<?>> TILE_ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.TILE_ENTITIES, EnchantmentsSeer.MOD_ID);

    public static final RegistryObject<TileEntityType<SeersEnchantingTableTileEntity>> SEERS_ENCHANTING_TABLE = TILE_ENTITY_TYPES.register("seers_enchanting_table", () -> TileEntityType.Builder.of(SeersEnchantingTableTileEntity::new, EnchantmentsSeerBlocks.SEERS_ENCHANTING_TABLE.get()).build(null));

}
