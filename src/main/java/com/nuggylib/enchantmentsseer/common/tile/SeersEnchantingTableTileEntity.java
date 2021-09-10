package com.nuggylib.enchantmentsseer.common.tile;

import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.INameable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import net.minecraft.tileentity.EnchantingTableTileEntity;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Our version of the {@link EnchantingTableTileEntity}
 *
 * @see "https://mcforge.readthedocs.io/en/1.16.x/datastorage/capabilities/#forge-provided-capabilities"
 */
public class SeersEnchantingTableTileEntity extends TileEntity implements IItemHandler, INameable, ITickableTileEntity {
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    public float rot;
    public float oRot;
    public float tRot;
    private static final Random RANDOM = new Random();
    private ITextComponent name;

    public SeersEnchantingTableTileEntity() {
        super(EnchantmentsSeerTileEntityTypes.SEERS_ENCHANTING_TABLE.get());
    }

    // TODO: Vanilla uses save/load presumably to save contents (maybe) - it seems we need to use insertItem/extractItem since we have to go through IItemHandler
//    @Override
//    public CompoundNBT save(CompoundNBT nbtTags) {
//        super.save(nbtTags);
//        if (this.hasCustomName()) {
//            nbtTags.putString("CustomName", ITextComponent.Serializer.toJson(this.name));
//        }
//
//        return nbtTags;
//    }
//
//    @Override
//    public void load(BlockState blockState, CompoundNBT nbtTags) {
//        super.load(blockState, nbtTags);
//        if (nbtTags.contains("CustomName", 8)) {
//            this.name = ITextComponent.Serializer.fromJson(nbtTags.getString("CustomName"));
//        }
//
//    }

    @Override
    public int getSlots() {
        return 0;
    }

    @NotNull
    @Override
    public ItemStack getStackInSlot(int slot) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    @Override
    public void tick() {
        this.oOpen = this.open;
        this.oRot = this.rot;
        PlayerEntity playerentity = this.level.getNearestPlayer((double)this.worldPosition.getX() + 0.5D, (double)this.worldPosition.getY() + 0.5D, (double)this.worldPosition.getZ() + 0.5D, 3.0D, false);
        if (playerentity != null) {
            double d0 = playerentity.getX() - ((double)this.worldPosition.getX() + 0.5D);
            double d1 = playerentity.getZ() - ((double)this.worldPosition.getZ() + 0.5D);
            this.tRot = (float) MathHelper.atan2(d1, d0);
            this.open += 0.1F;
            if (this.open < 0.5F || RANDOM.nextInt(40) == 0) {
                float f1 = this.flipT;

                do {
                    this.flipT += (float)(RANDOM.nextInt(4) - RANDOM.nextInt(4));
                } while(f1 == this.flipT);
            }
        } else {
            this.tRot += 0.02F;
            this.open -= 0.1F;
        }

        while(this.rot >= (float)Math.PI) {
            this.rot -= ((float)Math.PI * 2F);
        }

        while(this.rot < -(float)Math.PI) {
            this.rot += ((float)Math.PI * 2F);
        }

        while(this.tRot >= (float)Math.PI) {
            this.tRot -= ((float)Math.PI * 2F);
        }

        while(this.tRot < -(float)Math.PI) {
            this.tRot += ((float)Math.PI * 2F);
        }

        float f2;
        for(f2 = this.tRot - this.rot; f2 >= (float)Math.PI; f2 -= ((float)Math.PI * 2F)) {
        }

        while(f2 < -(float)Math.PI) {
            f2 += ((float)Math.PI * 2F);
        }

        this.rot += f2 * 0.4F;
        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        ++this.time;
        this.oFlip = this.flip;
        float f = (this.flipT - this.flip) * 0.4F;
        float f3 = 0.2F;
        f = MathHelper.clamp(f, -0.2F, 0.2F);
        this.flipA += (f - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }

    @Override
    public ITextComponent getName() {
        return (ITextComponent)(this.name != null ? this.name : new TranslationTextComponent("container.enchant"));
    }

    @Nullable
    public ITextComponent getCustomName() {
        return this.name;
    }
}
