package com.nuggylib.enchantmentsseer.common.block;

import com.nuggylib.enchantmentsseer.common.tile.SeersEnchantingTableTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

/**
 * Basically a modified version of {@link EnchantingTableBlock}
 */
public class SeersEnchantingTableBlock extends ContainerBlock {

    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public SeersEnchantingTableBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean useShapeForLightOcclusion(BlockState state) {
        return true;
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader reader, BlockPos blockPos, ISelectionContext context) {
        return SHAPE;
    }

    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader reader) {
        return new SeersEnchantingTableTileEntity();
    }

    @Override
    public BlockRenderType getRenderShape(BlockState blockState) {
        return BlockRenderType.MODEL;
    }

    @Override
    public void setPlacedBy(World worldIn, BlockPos blockPos, BlockState blockState, LivingEntity livingEntity, ItemStack itemStack) {
        if (itemStack.hasCustomHoverName()) {
            TileEntity tileentity = worldIn.getBlockEntity(blockPos);
            if (tileentity instanceof EnchantingTableTileEntity) {
                ((EnchantingTableTileEntity)tileentity).setCustomName(itemStack.getHoverName());
            }
        }

    }

    @Override
    public boolean isPathfindable(BlockState blockState, IBlockReader reader, BlockPos blockPos, PathType pathType) {
        return false;
    }

}
