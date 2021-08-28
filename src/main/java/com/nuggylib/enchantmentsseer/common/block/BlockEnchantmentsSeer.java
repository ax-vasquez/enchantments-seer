package com.nuggylib.enchantmentsseer.common.block;

import com.nuggylib.enchantmentsseer.common.block.states.BlockStateHelper;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.tile.interfaces.ISustainedData;
import com.nuggylib.enchantmentsseer.common.tile.interfaces.ISustainedInventory;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.PushReaction;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootContext;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/BlockMekanism.java"
 */
public class BlockEnchantmentsSeer extends Block {

    protected BlockEnchantmentsSeer(AbstractBlock.Properties properties) {
        super(properties);
        registerDefaultState(BlockStateHelper.getDefaultState(stateDefinition.any()));
    }

    @Nonnull
    @Override
    @Deprecated
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        if (hasTileEntity(state)) {
            //Protect against mods like Quark that allow blocks with TEs to be moved
            //TODO: Eventually it would be nice to go through this and maybe even allow some TEs to be moved if they don't strongly
            // care about the world, but for now it is safer to just block them from being moved
            return PushReaction.BLOCK;
        }
        return super.getPistonPushReaction(state);
    }

    @Nonnull
    @Override
    public ItemStack getPickBlock(@Nonnull BlockState state, RayTraceResult target, @Nonnull IBlockReader world, @Nonnull BlockPos pos, PlayerEntity player) {
        ItemStack itemStack = new ItemStack(this);
        TileEntityEnchantmentsSeer tile = WorldUtils.getTileEntity(TileEntityEnchantmentsSeer.class, world, pos);
        if (tile == null) {
            return itemStack;
        }
        Item item = itemStack.getItem();
        if (item instanceof ISustainedInventory && tile.persistInventory() && tile.getSlots() > 0) {
            ((ISustainedInventory) item).setInventory(tile.getInventory(), itemStack);
        }
        return itemStack;
    }

    @Nonnull
    @Override
    @Deprecated
    public List<ItemStack> getDrops(@Nonnull BlockState state, @Nonnull LootContext.Builder builder) {
        return super.getDrops(state, builder);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return this instanceof IHasTileEntity;
    }

    @Override
    public TileEntity createTileEntity(@Nonnull BlockState state, @Nonnull IBlockReader world) {
        if (this instanceof IHasTileEntity) {
            return ((IHasTileEntity<?>) this).getTileType().create();
        }
        return null;
    }

    @Override
    protected void createBlockStateDefinition(@Nonnull StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        BlockStateHelper.fillBlockStateContainer(this, builder);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(@Nonnull BlockItemUseContext context) {
        return BlockStateHelper.getStateForPlacement(this, super.getStateForPlacement(context), context);
    }

    @Override
    public void setPlacedBy(@Nonnull World world, @Nonnull BlockPos pos, @Nonnull BlockState state, @Nullable LivingEntity placer, @Nonnull ItemStack stack) {
        super.setPlacedBy(world, pos, state, placer, stack);
        TileEntityEnchantmentsSeer tile = WorldUtils.getTileEntity(TileEntityEnchantmentsSeer.class, world, pos);
        if (tile == null) {
            return;
        }

        //Handle item
        Item item = stack.getItem();
        setTileData(world, pos, state, placer, stack, tile);

        if (tile instanceof ISustainedData && stack.hasTag()) {
            ((ISustainedData) tile).readSustainedData(stack);
        }
        if (item instanceof ISustainedInventory && tile.persistInventory()) {
            tile.setInventory(((ISustainedInventory) item).getInventory(stack));
        }
    }

    //Method to override for setting some simple tile specific stuff
    public void setTileData(World world, BlockPos pos, BlockState state, @Nullable LivingEntity placer, ItemStack stack, TileEntityEnchantmentsSeer tile) {
    }

}
