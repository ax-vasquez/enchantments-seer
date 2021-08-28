package com.nuggylib.enchantmentsseer.common.tile.block;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.IInventorySlotHolder;
import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.InventorySlotHelper;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.InputInventorySlot;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerBlocks;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import net.minecraft.block.BlockState;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import java.util.Random;

/**
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/machine/TileEntityFuelwoodHeater.java"
 */
public class TileEntitySeersEnchantmentTable extends TileEntityEnchantmentsSeer {

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

    private InputInventorySlot itemSlot;
//    private InputInventorySlot reagentSlot;

    public TileEntitySeersEnchantmentTable() {
        super(EnchantmentsSeerBlocks.SEERS_ENCHANTING_TABLE);
    }

    @Nonnull
    @Override
    protected IInventorySlotHolder getInitialInventory() {
        InventorySlotHelper builder = InventorySlotHelper.forSide(this::getDirection);
        builder.addSlot(itemSlot = InputInventorySlot.at(null, 15, 29));
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

}
