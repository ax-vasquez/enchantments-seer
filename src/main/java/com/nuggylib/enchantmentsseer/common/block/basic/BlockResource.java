package com.nuggylib.enchantmentsseer.common.block.basic;

import com.nuggylib.enchantmentsseer.common.block.BlockEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.resource.BlockResourceInfo;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.PushReaction;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraftforge.common.ToolType;

import javax.annotation.Nonnull;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/BlockMekanism.java"
 */
public class BlockResource extends BlockEnchantmentsSeer {

    @Nonnull
    private final BlockResourceInfo resource;

    //TODO: Isn't as "generic"? So make it be from one BlockType thing?
    public BlockResource(@Nonnull BlockResourceInfo resource) {
        super(AbstractBlock.Properties.of(Material.METAL).strength(resource.getHardness(), resource.getResistance())
                .lightLevel(state -> resource.getLightValue()).requiresCorrectToolForDrops().harvestTool(ToolType.PICKAXE).harvestLevel(resource.getHarvestLevel()));
        this.resource = resource;
    }

    @Nonnull
    public BlockResourceInfo getResourceInfo() {
        return resource;
    }

    @Nonnull
    @Override
    @Deprecated
    public PushReaction getPistonPushReaction(@Nonnull BlockState state) {
        return resource.getPushReaction();
    }

    @Override
    public boolean isPortalFrame(BlockState state, IBlockReader world, BlockPos pos) {
        return resource.isPortalFrame();
    }
}
