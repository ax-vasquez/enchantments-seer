package com.nuggylib.enchantmentsseer.common.tile;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerItemHandler;
import com.nuggylib.enchantmentsseer.common.inventory.container.SeersEnchantingTableContainer;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerTileEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.tileentity.ITickableTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.INameable;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import net.minecraft.tileentity.EnchantingTableTileEntity;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Our version of the {@link EnchantingTableTileEntity}
 *
 * @see "https://mcforge.readthedocs.io/en/1.16.x/datastorage/capabilities/#forge-provided-capabilities"
 */
public class SeersEnchantingTableTileEntity extends TileEntity implements INameable, ITickableTileEntity {

    @CapabilityInject(IItemHandler.class)
    static Capability<IItemHandler> ITEM_HANDLER_CAPABILITY = null;

    LazyOptional<IItemHandler> inventoryHandlerLazyOptional;

    private final IItemHandler inventory = new ItemStackHandler(2) {
        @Override
        protected void onContentsChanged(int slot) {
            EnchantmentsSeer.logger.info(String.format("Contents changed for slot: %s", slot));
            super.onContentsChanged(slot);
            setChanged();
        }

        @NotNull
        @Override
        public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
            EnchantmentsSeer.logger.info(String.format("Inserting item in slot: %s", slot));
            return super.insertItem(slot, stack, simulate);
        }

        @NotNull
        @Override
        public ItemStack extractItem(int slot, int amount, boolean simulate) {
            EnchantmentsSeer.logger.info(String.format("Extracting item from slot: %s", slot));
            return super.extractItem(slot, amount, simulate);
        }
    };

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
        // TODO: The item handler doesn't appear to be getting used yet - there seems to be more we need
        inventoryHandlerLazyOptional = LazyOptional.of(() -> inventory);
    }

    @Override
    public <T> LazyOptional<T> getCapability(Capability<T> cap, Direction side) {
        if (cap == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
            return inventoryHandlerLazyOptional.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        inventoryHandlerLazyOptional.invalidate();
    }

    public IItemHandler getInventory() {
        return inventory;
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
