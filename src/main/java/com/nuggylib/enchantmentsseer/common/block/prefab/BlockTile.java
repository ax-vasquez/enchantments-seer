package com.nuggylib.enchantmentsseer.common.block.prefab;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.block.IHasTileEntity;
import com.nuggylib.enchantmentsseer.common.block.attribute.AttributeGui;
import com.nuggylib.enchantmentsseer.common.content.BlockTypeTile;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import java.util.Random;
import java.util.function.UnaryOperator;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <TILE>
 * @param <TYPE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/prefab/BlockTile.java"
 */
public class BlockTile<TILE extends TileEntityEnchantmentsSeer, TYPE extends BlockTypeTile<TILE>> extends BlockBase<TYPE> implements IHasTileEntity<TILE> {

    public BlockTile(TYPE type) {
        this(type, UnaryOperator.identity());
    }

    public BlockTile(TYPE type, UnaryOperator<AbstractBlock.Properties> propertiesModifier) {
        this(type, propertiesModifier.apply(AbstractBlock.Properties.of(Material.METAL).strength(3.5F, 16).requiresCorrectToolForDrops()));
        //TODO - 10.1: Figure out what the resistance should be (it used to be different in 1.12)
    }

    public BlockTile(TYPE type, AbstractBlock.Properties properties) {
        super(type, properties);
    }

    @Override
    public TileEntityType<TILE> getTileType() {
        return type.getTileType();
    }

    @Nonnull
    @Override
    @Deprecated
    public ActionResultType use(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull PlayerEntity player, @Nonnull Hand hand,
                                @Nonnull BlockRayTraceResult hit) {
        TileEntityEnchantmentsSeer tile = WorldUtils.getTileEntity(TileEntityEnchantmentsSeer.class, world, pos);
        return type.has(AttributeGui.class) ? tile.openGui(player) : ActionResultType.PASS;
    }

    @Override
    public void animateTick(@Nonnull BlockState state, @Nonnull World world, @Nonnull BlockPos pos, @Nonnull Random random) {

    }

    public static class BlockTileModel<TILE extends TileEntityEnchantmentsSeer, BLOCK extends BlockTypeTile<TILE>> extends BlockTile<TILE, BLOCK> {

        public BlockTileModel(BLOCK type) {
            super(type);
        }

        public BlockTileModel(BLOCK type, AbstractBlock.Properties properties) {
            super(type, properties);
        }
    }
}
