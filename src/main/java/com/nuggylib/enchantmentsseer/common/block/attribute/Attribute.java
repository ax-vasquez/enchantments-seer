package com.nuggylib.enchantmentsseer.common.block.attribute;

import com.google.common.collect.Lists;
import com.nuggylib.enchantmentsseer.common.block.interfaces.ITypeBlock;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;

/**
 * Inspired by the Mekanism codebase
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/attribute/Attribute.java"
 */
public interface Attribute {

    interface TileAttribute<TILE extends TileEntityEnchantmentsSeer> extends Attribute {}

    default void adjustProperties(AbstractBlock.Properties props) {
    }

    static boolean has(Block block, Class<? extends Attribute> type) {
        return block instanceof ITypeBlock && ((ITypeBlock) block).getType().has(type);
    }

    static <T extends Attribute> T get(Block block, Class<T> type) {
        return block instanceof ITypeBlock ? ((ITypeBlock) block).getType().get(type) : null;
    }

    static boolean has(Block block1, Block block2, Class<? extends Attribute> type) {
        return has(block1, type) && has(block2, type);
    }

    static Collection<Attribute> getAll(Block block) {
        return block instanceof ITypeBlock ? ((ITypeBlock) block).getType().getAll() : Lists.newArrayList();
    }

    static <T extends Attribute> void ifHas(Block block, Class<T> type, Consumer<T> run) {
        if (block instanceof ITypeBlock) {
            T attribute = ((ITypeBlock) block).getType().get(type);
            if (attribute != null) {
                run.accept(attribute);
            }
        }
    }

    @Nullable
    static Direction getFacing(BlockState state) {
        AttributeStateFacing attr = get(state.getBlock(), AttributeStateFacing.class);
        return attr == null ? null : attr.getDirection(state);
    }

    @Nullable
    static BlockState setFacing(BlockState state, Direction facing) {
        AttributeStateFacing attr = get(state.getBlock(), AttributeStateFacing.class);
        return attr == null ? null : attr.setDirection(state, facing);
    }

}
