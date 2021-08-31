package com.nuggylib.enchantmentsseer.common.block.prefab;

import com.nuggylib.enchantmentsseer.common.block.BlockEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.block.interfaces.ITypeBlock;
import com.nuggylib.enchantmentsseer.common.content.BlockType;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.material.Material;

import java.util.function.UnaryOperator;

/**
 * Shamelessly-copied from Mekanism; all credit goes to them
 *
 * @param <TYPE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/prefab/BlockBase.java"
 */
public class BlockBase<TYPE extends BlockType> extends BlockEnchantmentsSeer implements ITypeBlock {

    protected final TYPE type;

    public BlockBase(TYPE type, UnaryOperator<Properties> propertyModifier) {
        this(type, propertyModifier.apply(AbstractBlock.Properties.of(Material.METAL).requiresCorrectToolForDrops()));
    }

    public BlockBase(TYPE type, AbstractBlock.Properties properties) {
        super(hack(type, properties));
        this.type = type;
    }

    // ugly hack but required to have a reference to our block type before setting state info; assumes single-threaded startup
    private static BlockType cacheType;

    private static <TYPE extends BlockType> AbstractBlock.Properties hack(TYPE type, AbstractBlock.Properties props) {
        cacheType = type;
        type.getAll().forEach(a -> a.adjustProperties(props));
        return props;
    }

    @Override
    public BlockType getType() {
        return type == null ? cacheType : type;
    }

    public static class BlockBaseModel<BLOCK extends BlockType> extends BlockBase<BLOCK> {

        public BlockBaseModel(BLOCK blockType, UnaryOperator<AbstractBlock.Properties> propertyModifier) {
            super(blockType, propertyModifier);
        }

        public BlockBaseModel(BLOCK blockType, AbstractBlock.Properties properties) {
            super(blockType, properties);
        }
    }
}
