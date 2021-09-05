package com.nuggylib.enchantmentsseer.common;

import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Inspired by Mekanism
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/CommonPlayerTickHandler.java"
 */
public class CommonPlayerTickHandler {

    public static boolean isOnGroundOrSleeping(PlayerEntity player) {
        if (player.isSleeping()) {
            return true;
        }
        int x = MathHelper.floor(player.getX());
        int y = MathHelper.floor(player.getY() - 0.01);
        int z = MathHelper.floor(player.getZ());
        BlockPos pos = new BlockPos(x, y, z);
        BlockState s = player.level.getBlockState(pos);
        VoxelShape shape = s.getShape(player.level, pos);
        if (shape.isEmpty()) {
            return false;
        }
        AxisAlignedBB playerBox = player.getBoundingBox();
        return !s.isAir(player.level, pos) && playerBox.move(0, -0.01, 0).intersects(shape.bounds().move(pos));
    }

    @SubscribeEvent
    public void onTick(TickEvent.PlayerTickEvent event) {
        // TODO: See if we need anything here
    }

    @SubscribeEvent
    public void onEntityAttacked(LivingAttackEvent event) {
        // TODO: See if we want to add some logic here later
    }

    @SubscribeEvent
    public void onLivingHurt(LivingHurtEvent event) {
        // TODO: See if we want to add some logic here later
    }

    @SubscribeEvent
    public void onLivingJump(LivingEvent.LivingJumpEvent event) {
        // TODO: We can probably just remove this - I can't think of any reason we would need to modify this logic
    }
}
