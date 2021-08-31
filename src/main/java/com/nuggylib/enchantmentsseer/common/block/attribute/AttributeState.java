package com.nuggylib.enchantmentsseer.common.block.attribute;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.state.Property;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.List;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/attribute/AttributeState.java"
 */
public interface AttributeState extends Attribute {

    BlockState copyStateData(BlockState oldState, BlockState newState);

    void fillBlockStateContainer(Block block, List<Property<?>> properties);

    default BlockState getDefaultState(@Nonnull BlockState state) {
        return state;
    }

    @Contract("_, null, _, _, _, _ -> null")
    default BlockState getStateForPlacement(Block block, @Nullable BlockState state, @Nonnull IWorld world, @Nonnull BlockPos pos, @Nullable PlayerEntity player,
                                            @Nonnull Direction face) {
        return state;
    }
}