package com.nuggylib.enchantmentsseer.block;

import com.nuggylib.enchantmentsseer.inventory.container.SeersEnchantingTableContainer;
import com.nuggylib.enchantmentsseer.tileentity.SeersEnchantingTableTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.pathfinding.PathType;
import net.minecraft.tileentity.EnchantingTableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.INameable;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import javax.annotation.Nullable;
import java.util.Random;

public class SeersEnchantingTableBlock extends Block {
    protected static final VoxelShape SHAPE = Block.box(0.0D, 0.0D, 0.0D, 16.0D, 12.0D, 16.0D);

    public SeersEnchantingTableBlock(Properties p_i48446_1_) {
        super(p_i48446_1_);
    }

    @Override
    public boolean triggerEvent(BlockState p_189539_1_, World p_189539_2_, BlockPos p_189539_3_, int p_189539_4_, int p_189539_5_) {
        return super.triggerEvent(p_189539_1_, p_189539_2_, p_189539_3_, p_189539_4_, p_189539_5_);
    }

    /**
     *
     *
     * @see "https://mcforge.readthedocs.io/en/latest/blocks/interaction/#use"
     */
    @Override
    public ActionResultType use(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            player.openMenu(state.getMenuProvider(worldIn, pos));
            return ActionResultType.CONSUME;
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void animateTick(BlockState p_180655_1_, World p_180655_2_, BlockPos p_180655_3_, Random p_180655_4_) {
        super.animateTick(p_180655_1_, p_180655_2_, p_180655_3_, p_180655_4_);

        for(int i = -2; i <= 2; ++i) {
            for(int j = -2; j <= 2; ++j) {
                if (i > -2 && i < 2 && j == -1) {
                    j = 2;
                }

                if (p_180655_4_.nextInt(16) == 0) {
                    for(int k = 0; k <= 1; ++k) {
                        BlockPos blockpos = p_180655_3_.offset(i, k, j);
                        if (p_180655_2_.getBlockState(blockpos).is(Blocks.BOOKSHELF)) {
                            if (!p_180655_2_.isEmptyBlock(p_180655_3_.offset(i / 2, 0, j / 2))) {
                                break;
                            }

                            p_180655_2_.addParticle(ParticleTypes.ENCHANT, (double)p_180655_3_.getX() + 0.5D, (double)p_180655_3_.getY() + 2.0D, (double)p_180655_3_.getZ() + 0.5D, (double)((float)i + p_180655_4_.nextFloat()) - 0.5D, (double)((float)k - p_180655_4_.nextFloat() - 1.0F), (double)((float)j + p_180655_4_.nextFloat()) - 0.5D);
                        }
                    }
                }
            }
        }

    }

    /**
     * N.B.
     *
     * Although this overrides a deprecated method, it **does** have an effect. This method is apparently used internally
     * to determine the shape of the block in the game world. It uses the locally-defined VoxelShape, `SHAPE`.
     */
    @Override
    public VoxelShape getShape(BlockState p_220053_1_, IBlockReader p_220053_2_, BlockPos p_220053_3_, ISelectionContext p_220053_4_) {
        return SHAPE;
    }

    /**
     * N.B.
     *
     * Without this method returning BlockRenderType.MODEL, it seems that the RenderType will default to invisible. If
     * you comment this method out, you will see that the outline of the model is invisible.
     */
    @Override
    public BlockRenderType getRenderShape(BlockState p_149645_1_) {
        return BlockRenderType.MODEL;
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SeersEnchantingTableTileEntity();
    }

    @Override
    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState blockState, World worldIn, BlockPos blockPos) {
        TileEntity tileentity = worldIn.getBlockEntity(blockPos);
        if (tileentity instanceof SeersEnchantingTableTileEntity) {
            ITextComponent itextcomponent = ((INameable)tileentity).getDisplayName();
            return new SimpleNamedContainerProvider((containerId, playerInventory, p_220147_4_) -> new SeersEnchantingTableContainer(containerId, playerInventory, IWorldPosCallable.create(worldIn, blockPos)), itextcomponent);
        } else {
            LOGGER.info(String.format("Unexpected TileEntity: %s", tileentity));
            return null;
        }
    }

    public void setPlacedBy(World p_180633_1_, BlockPos p_180633_2_, BlockState p_180633_3_, LivingEntity p_180633_4_, ItemStack p_180633_5_) {
        if (p_180633_5_.hasCustomHoverName()) {
            TileEntity tileentity = p_180633_1_.getBlockEntity(p_180633_2_);
            if (tileentity instanceof EnchantingTableTileEntity) {
                ((EnchantingTableTileEntity)tileentity).setCustomName(p_180633_5_.getHoverName());
            }
        }

    }

    public boolean isPathfindable(BlockState p_196266_1_, IBlockReader p_196266_2_, BlockPos p_196266_3_, PathType p_196266_4_) {
        return false;
    }
}
