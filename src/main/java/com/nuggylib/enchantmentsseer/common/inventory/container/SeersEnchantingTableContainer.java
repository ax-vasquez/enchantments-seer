package com.nuggylib.enchantmentsseer.common.inventory.container;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerEnchantHelper;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerBlocks;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerContainerTypes;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.EnchantmentContainer;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraftforge.common.Tags;

import java.util.List;

// TODO: Set this up to mimic the vanilla container class
/**
 * Our version of {@link EnchantmentContainer}
 *
 * An implementation of the {@link Container} class is required for any block that needs to store one or more
 * {@link ItemStack}s.
 */
public class SeersEnchantingTableContainer extends Container {

    // TODO: Explore/document this more
    /**
     * Unsure of what this is at the moment - it appears to be responsible for performing operations in the game
     * world on behalf of the corresponding container.
     *
     * From observing how {@link EnchantmentContainer} uses this field, it appears that {@link IWorldPosCallable} provides
     * a means through which you can perform container operations that require their interactions to also be synced
     * to the server side. However, I'm still working to understand this better.
     */
    private final IWorldPosCallable access;

    /**
     * The slots for the container
     *
     * This DOES NOT have anything to do with the GUI slot textures. It simply declares that this container has two
     * inventory slots, enabling it to store two separate {@link ItemStack}s.
     */
    private final IInventory enchantSlots = new Inventory(2) {
        public void setChanged() {
            super.setChanged();
            SeersEnchantingTableContainer.this.slotsChanged(this);
        }
    };

    /**
     * Overloaded constructor
     *
     * This constructor is reponsible for "informing" the GUI where the slots belong on the texture, once rendered.
     */
    public SeersEnchantingTableContainer(int containerId, IWorldPosCallable access, PlayerInventory inv) {
        super(EnchantmentsSeerContainerTypes.SEERS_ENCHANTING_TABLE.get(), containerId);
        this.access = access;

        // Create the enchant item slot
        this.addSlot(new Slot(this.enchantSlots, 0, 15, 47) {
            public boolean mayPlace(ItemStack potentialEnchantItem) {
                return true;
            }

            public int getMaxStackSize() {
                return 1;
            }
        });

        // Create the reagent slot
        this.addSlot(new Slot(this.enchantSlots, 1, 35, 47) {
            public boolean mayPlace(ItemStack potentialReagentItem) {
                return Tags.Items.GEMS_LAPIS.contains(potentialReagentItem.getItem());
            }
        });

        // Create slots for the main inventory
        for(int mainInventoryRow = 0; mainInventoryRow < 3; ++mainInventoryRow) {
            for(int mainInventoryColumn = 0; mainInventoryColumn < 9; ++mainInventoryColumn) {
                this.addSlot(new Slot(inv, mainInventoryColumn + mainInventoryRow * 9 + 9, 8 + mainInventoryColumn * 18, 84 + mainInventoryRow * 18));
            }
        }

        // Create slots for the hotbar
        for(int hotbarColumn = 0; hotbarColumn < 9; ++hotbarColumn) {
            this.addSlot(new Slot(inv, hotbarColumn, 8 + hotbarColumn * 18, 142));
        }
    }

    /**
     * The "primary" constructor
     *
     * This constructor is used when registering the class
     */
    public SeersEnchantingTableContainer(int containerId, PlayerInventory playerInventory) {
        // TODO: Find out what impact IWorldPosCallable.NULL has
        this(containerId, IWorldPosCallable.NULL, playerInventory);
    }

    // TODO: Clarify the docs for this once we know more about it
    /**
     * Determines if <b>something</b> is still valid, but existing documentation doesn't make it very clear.
     *
     * This implementation mimics the functionality for the same method in {@link EnchantmentContainer}
     */
    @Override
    public boolean stillValid(PlayerEntity player) {
        return stillValid(this.access, player, EnchantmentsSeerBlocks.SEERS_ENCHANTING_TABLE.get());
    }

    /**
     * Runs when the contents of the inventory change
     *
     * {@link Container#slotsChanged(IInventory)} is invoked anytime a player modifies the contents of the given container,
     * which makes it a very good place to perform key operations such as obtaining the list of enchantments for the item
     * currently in the first slot.
     */
    @Override
    public void slotsChanged(IInventory inv) {
        if (inv == this.enchantSlots) {
            ItemStack stack = inv.getItem(0);
            if (!stack.isEmpty() && stack.isEnchantable()) {
                // Unlike Vanilla, we don't use any concept of "power level" for enchanting - as a result, our table doesn't need
                // bookshelves around it (since those are directly-used to determine the power level)
                this.access.execute((worldIn, blockPos) -> {
                    List<Enchantment> list = EnchantmentsSeerEnchantHelper.getAvailableEnchantmentResults(stack, false);

                    for (Enchantment enchantment : list) {
                        EnchantmentsSeer.logger.info(String.format("%s", enchantment.getFullname(enchantment.getMaxLevel())));
                    }
                    this.broadcastChanges();
                });
            }
        }

    }

    @Override
    public boolean clickMenuButton(PlayerEntity player, int cost) {
        ItemStack enchantItemStack = this.enchantSlots.getItem(0);
        ItemStack reagentItemStack = this.enchantSlots.getItem(1);
        int reagentCost = cost + 1;
        if ((reagentItemStack.isEmpty() || reagentItemStack.getCount() < reagentCost) && !player.abilities.instabuild) {
            return false;
        } else if (enchantItemStack.isEmpty() || !player.abilities.instabuild) {
            return false;
        } else {
            this.access.execute((worldIn, blockPos) -> {
                ItemStack resultItemStack = enchantItemStack;
                List<Enchantment> list = EnchantmentsSeerEnchantHelper.getAvailableEnchantmentResults(enchantItemStack, false);
                if (!list.isEmpty()) {
                    player.onEnchantmentPerformed(enchantItemStack, reagentCost);
                    boolean isBookItem = enchantItemStack.getItem() == Items.BOOK;
                    if (isBookItem) {
                        resultItemStack = new ItemStack(Items.ENCHANTED_BOOK);
                        CompoundNBT compoundnbt = enchantItemStack.getTag();
                        if (compoundnbt != null) {
                            resultItemStack.setTag(compoundnbt.copy());
                        }

                        this.enchantSlots.setItem(0, resultItemStack);
                    }

                    for(int j = 0; j < list.size(); ++j) {
                        Enchantment enchantmentdata = list.get(j);  // TODO: Use this in the enchantment process (or
                        // Disallow books from being enchanted for the sake of simplicity
                        if (!isBookItem) {
                            // TODO: Modify the logic here so that the USER sets the enchantment level
                            //resultItemStack.enchant(enchantmentdata.enchantment, enchantmentdata.level);
                        }
                    }

                    if (!player.abilities.instabuild) {
                        reagentItemStack.shrink(reagentCost);
                        if (reagentItemStack.isEmpty()) {
                            this.enchantSlots.setItem(1, ItemStack.EMPTY);
                        }
                    }

                    player.awardStat(Stats.ENCHANT_ITEM);
                    if (player instanceof ServerPlayerEntity) {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, resultItemStack, reagentCost);
                    }

                    this.enchantSlots.setChanged();
                    this.slotsChanged(this.enchantSlots);
                    worldIn.playSound((PlayerEntity)null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
                }

            });
            return true;
        }
    }

    @Override
    public void removed(PlayerEntity player) {
        super.removed(player);
        this.access.execute((worldIn, blockPos) -> {
            this.clearContainer(player, player.level, this.enchantSlots);
        });
    }

    /**
     * This method is invoked whenever the player "quick moves" an item into the Seers Enchanting Table.
     *
     * To quick-move an {@link ItemStack}, simply hold the <pre>SHIFT</pre> key and click the item to quick-move.
     */
    @Override
    public ItemStack quickMoveStack(PlayerEntity player, int slotIndex) {
        ItemStack holderItemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStackInSlot = slot.getItem();
            holderItemStack = itemStackInSlot.copy();
            if (slotIndex == 0) {
                if (!this.moveItemStackTo(itemStackInSlot, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex == 1) {
                if (!this.moveItemStackTo(itemStackInSlot, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStackInSlot.getItem() == Items.LAPIS_LAZULI) {
                if (!this.moveItemStackTo(itemStackInSlot, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.slots.get(0).hasItem() || !this.slots.get(0).mayPlace(itemStackInSlot)) {
                    return ItemStack.EMPTY;
                }

                ItemStack extractedItemStack = itemStackInSlot.copy();
                extractedItemStack.setCount(1);
                itemStackInSlot.shrink(1);
                this.slots.get(0).set(extractedItemStack);
            }

            if (itemStackInSlot.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStackInSlot.getCount() == holderItemStack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, itemStackInSlot);
        }

        return holderItemStack;
    }

}
