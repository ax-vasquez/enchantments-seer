package com.nuggylib.enchantmentsseer.common.block;

import com.nuggylib.enchantmentsseer.common.inventory.container.SeersEnchantingTableContainer;
import com.nuggylib.enchantmentsseer.common.tile.SeersEnchantingTableTileEntity;
import net.minecraft.block.*;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.inventory.container.SimpleNamedContainerProvider;
import net.minecraft.item.ItemStack;
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

    // Forge docs require this method be implemented for any Block with a TileEntity for version 1.16.5 mods
    // See https://mcforge.readthedocs.io/en/1.16.x/tileentities/tileentity/#attaching-a-tileentity-to-a-block
    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    // Forge docs require this method be implemented for any Block with a TileEntity for version 1.16.5 mods
    // See https://mcforge.readthedocs.io/en/1.16.x/tileentities/tileentity/#attaching-a-tileentity-to-a-block
    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new SeersEnchantingTableTileEntity();
    }

    // TODO: I think we need to remove this as this is what Minecraft vanilla is using
    @Nullable
    @Override
    public TileEntity newBlockEntity(IBlockReader reader) {
        return new SeersEnchantingTableTileEntity();
    }

    @Override
    public ActionResultType use(BlockState blockState, World worldIn, BlockPos blockPos, PlayerEntity player, Hand hand, BlockRayTraceResult rayTraceResult) {
        if (worldIn.isClientSide) {
            return ActionResultType.SUCCESS;
        } else {
            player.openMenu(blockState.getMenuProvider(worldIn, blockPos));
            return ActionResultType.CONSUME;
        }
    }

    @Nullable
    public INamedContainerProvider getMenuProvider(BlockState blockState, World worldIn, BlockPos blockPos) {
        TileEntity tileentity = worldIn.getBlockEntity(blockPos);
        if (tileentity instanceof SeersEnchantingTableTileEntity) {
            ITextComponent itextcomponent = ((INameable)tileentity).getDisplayName();
            return new SimpleNamedContainerProvider((containerId, inv, player) -> {
                return new SeersEnchantingTableContainer(containerId, IWorldPosCallable.create(worldIn, blockPos), inv);
            }, itextcomponent);
        } else {
            return null;
        }
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
