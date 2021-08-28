package com.nuggylib.enchantmentsseer.common.block.states;

import com.nuggylib.enchantmentsseer.common.block.attribute.Attribute;
import com.nuggylib.enchantmentsseer.common.block.attribute.AttributeState;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.Property;
import net.minecraft.state.StateContainer;
import net.minecraft.util.Direction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IWorld;
import org.jetbrains.annotations.Contract;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/states/BlockStateHelper.java"
 */
public class BlockStateHelper {

    private BlockStateHelper() {
    }

    //Cardboard Box storage
    public static final BooleanProperty storageProperty = BooleanProperty.create("storage");

    public static BlockState getDefaultState(@Nonnull BlockState state) {
        Block block = state.getBlock();
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                state = ((AttributeState) attr).getDefaultState(state);
            }
        }
        return state;
    }

    public static void fillBlockStateContainer(Block block, StateContainer.Builder<Block, BlockState> builder) {
        List<Property<?>> properties = new ArrayList<>();
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                ((AttributeState) attr).fillBlockStateContainer(block, properties);
            }
        }
        if (block instanceof IStateStorage) {
            properties.add(storageProperty);
        }
        if (!properties.isEmpty()) {
            builder.add(properties.toArray(new Property[0]));
        }
    }

    @Contract("_, null, _ -> null")
    public static BlockState getStateForPlacement(Block block, @Nullable BlockState state, BlockItemUseContext context) {
        return getStateForPlacement(block, state, context.getLevel(), context.getClickedPos(), context.getPlayer(), context.getClickedFace());
    }

    @Contract("_, null, _, _, _, _ -> null")
    public static BlockState getStateForPlacement(Block block, @Nullable BlockState state, @Nonnull IWorld world, @Nonnull BlockPos pos, @Nullable PlayerEntity player, @Nonnull Direction face) {
        if (state == null) {
            return null;
        }
        for (Attribute attr : Attribute.getAll(block)) {
            if (attr instanceof AttributeState) {
                state = ((AttributeState) attr).getStateForPlacement(block, state, world, pos, player, face);
            }
        }
        return state;
    }

}
