package com.nuggylib.enchantmentsseer.common.item;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.*;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.world.World;

import javax.annotation.Nonnull;

public class ItemSeersStone extends Item {

    public ItemSeersStone(Properties properties) {
        super(properties.rarity(Rarity.UNCOMMON).stacksTo(1).setNoRepair());
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack finishUsingItem(@Nonnull ItemStack stack, @Nonnull World world, @Nonnull LivingEntity entityLiving) {
        return stack;
    }

    @Override
    public int getUseDuration(@Nonnull ItemStack stack) {
        return 32;
    }

    @Nonnull
    @Override
    public UseAction getUseAnimation(@Nonnull ItemStack stack) {
        return UseAction.DRINK;
    }

    @Nonnull
    @Override
    public ActionResult<ItemStack> use(@Nonnull World worldIn, PlayerEntity playerIn, @Nonnull Hand handIn) {
        return ActionResult.success(playerIn.getItemInHand(handIn));
    }

}
