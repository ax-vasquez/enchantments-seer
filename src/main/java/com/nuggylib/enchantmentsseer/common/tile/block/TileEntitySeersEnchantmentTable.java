package com.nuggylib.enchantmentsseer.common.tile.block;

import com.nuggylib.enchantmentsseer.api.annotations.NonNull;
import com.nuggylib.enchantmentsseer.api.recipes.inputs.IInputHandler;
import com.nuggylib.enchantmentsseer.api.recipes.inputs.InputHelper;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.IInventorySlotHolder;
import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.InventorySlotHelper;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.EnchantInventorySlot;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.InputInventorySlot;
import com.nuggylib.enchantmentsseer.common.lib.transmitter.TransmissionType;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerBlocks;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.tile.component.TileComponentConfig;
import net.minecraft.block.BlockState;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/prefab/TileEntityElectricMachine.java"
 */
public class TileEntitySeersEnchantmentTable extends TileEntityEnchantmentsSeer implements ITickableTileEntity {

    protected final IInputHandler<@NonNull ItemStack> enchantItemHelper;
    protected final IInputHandler<@NonNull ItemStack> reagentItemHelper;
    private EnchantInventorySlot inputSlot;
    private InputInventorySlot reagentSlot;

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

    private double lastEnvironmentLoss;
    // TODO: See if we need to fully-implement this class to get things to a working state

    public TileEntitySeersEnchantmentTable() {
        super(EnchantmentsSeerBlocks.SEERS_ENCHANTING_TABLE);
        // Setup up the tile configuration component
        configComponent = new TileComponentConfig(this, TransmissionType.ITEM);
        // Ensures the slots are defined before trying to use them
        configComponent.setupItemIOConfig(inputSlot, reagentSlot);
        enchantItemHelper = InputHelper.getInputHandler(inputSlot);
        reagentItemHelper = InputHelper.getInputHandler(reagentSlot);
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory() {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(inputSlot = EnchantInventorySlot.at(null, 15, 47));
        builder.addSlot(reagentSlot = InputInventorySlot.at(null, 35, 47));
        return builder.build();
    }

    @Override
    public void load(@Nonnull BlockState state, @Nonnull CompoundNBT nbtTags) {
        super.load(state, nbtTags);
    }

    @Nonnull
    @Override
    public CompoundNBT save(@Nonnull CompoundNBT nbtTags) {
        super.save(nbtTags);
        return nbtTags;
    }

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

    public ITextComponent getName() {
        return (ITextComponent)(this.name != null ? this.name : new TranslationTextComponent("container.enchant"));
    }

    public void setCustomName(@Nullable ITextComponent p_200229_1_) {
        this.name = p_200229_1_;
    }

    @Nullable
    public ITextComponent getCustomName() {
        return this.name;
    }

}
