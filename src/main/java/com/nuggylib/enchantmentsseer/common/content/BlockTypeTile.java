package com.nuggylib.enchantmentsseer.common.content;

import com.nuggylib.enchantmentsseer.client.render.text.ILangEntry;
import com.nuggylib.enchantmentsseer.common.block.attribute.AttributeGui;
import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import com.nuggylib.enchantmentsseer.common.registration.impl.ContainerTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.registration.impl.TileEntityTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import net.minecraft.tileentity.TileEntityType;

import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <TILE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/content/blocktype/BlockTypeTile.java"
 */
public class BlockTypeTile<TILE extends TileEntityEnchantmentsSeer> extends BlockType {

    private final Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar;

    public BlockTypeTile(Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
        super(description);
        this.tileEntityRegistrar = tileEntityRegistrar;
    }

    public TileEntityType<TILE> getTileType() {
        return tileEntityRegistrar.get().getTileEntityType();
    }

    public static class BlockTileBuilder<BLOCK extends BlockTypeTile<TILE>, TILE extends TileEntityEnchantmentsSeer, T extends BlockTileBuilder<BLOCK, TILE, T>>
            extends BlockTypeBuilder<BLOCK, T> {

        protected BlockTileBuilder(BLOCK holder) {
            super(holder);
        }

        public static <TILE extends TileEntityEnchantmentsSeer> BlockTileBuilder<BlockTypeTile<TILE>, TILE, ?> createBlock(
                Supplier<TileEntityTypeRegistryObject<TILE>> tileEntityRegistrar, ILangEntry description) {
            return new BlockTileBuilder<>(new BlockTypeTile<>(tileEntityRegistrar, description));
        }

        public T withGui(Supplier<ContainerTypeRegistryObject<? extends EnchantmentsSeerContainer>> containerRegistrar) {
            return with(new AttributeGui(containerRegistrar));
        }
    }
}