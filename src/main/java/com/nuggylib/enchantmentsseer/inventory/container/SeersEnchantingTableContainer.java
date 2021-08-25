package com.nuggylib.enchantmentsseer.inventory.container;

import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.stats.Stats;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class SeersEnchantingTableContainer extends Container {
    private static final Logger LOGGER = LogManager.getLogger(SeersEnchantingTableContainer.class);

    private final IInventory enchantSlots = new Inventory(2) {
        public void setChanged() {
            super.setChanged();
            SeersEnchantingTableContainer.this.slotsChanged(this);
        }
    };
    private final IWorldPosCallable access;
    private final Random random = new Random();
    private final IntReferenceHolder enchantmentSeed = IntReferenceHolder.standalone();
    public final int[] costs = new int[3];
    public final int[] enchantClue = new int[]{-1, -1, -1};
    public final int[] levelClue = new int[]{-1, -1, -1};

    public SeersEnchantingTableContainer(int containerId, PlayerInventory playerInventory) {
        this(containerId, playerInventory, IWorldPosCallable.NULL);
    }

    public SeersEnchantingTableContainer(int containerId, PlayerInventory playerInventory, IWorldPosCallable access) {
        super(EnchantmentsSeer.SEERS_ENCHANTING_TABLE_CONTAINER_TYPE.get(), containerId);
        this.access = access;
        // Create the item slot (e.g., the slot for the item to be enchanted)
        this.addSlot(new Slot(this.enchantSlots, 0, 15, 47) {
            public boolean mayPlace(ItemStack inputItemStack) {
                return true;
            }

            public int getMaxStackSize() {
                return 1;
            }
        });
        // Create the reagent slot
        this.addSlot(new Slot(this.enchantSlots, 1, 35, 47) {
            // TODO: Only allow Seer's Enchanting Book, Enchanted Book, or Seer's Stones as input
            public boolean mayPlace(ItemStack inputItemStack) {
                return net.minecraftforge.common.Tags.Items.GEMS_LAPIS.contains(inputItemStack.getItem());
            }
        });

        final int INVENTORY_ROWS = 3;
        final int INVENTORY_COLUMNS = 9;
        // Create the slots to display the player's internal inventory
        for(int i = 0; i < INVENTORY_ROWS; ++i) {
            for(int j = 0; j < INVENTORY_COLUMNS; ++j) {
                this.addSlot(new Slot(playerInventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
            }
        }

        final int HOTBAR_SLOTS = 9;
        // Create the slots to display the player's hot-bar inventory
        for(int k = 0; k < HOTBAR_SLOTS; ++k) {
            this.addSlot(new Slot(playerInventory, k, 8 + k * 18, 142));
        }

        // TODO: Update the GUI so that row count is dynamic and feed is scrollable
        final int ROW_COUNT = 3;
        this.addDataSlot(this.enchantmentSeed).set(playerInventory.player.getEnchantmentSeed());
        // Adds data slots used as the enchantment list rows
        for (int i = 0; i < ROW_COUNT; ++i) {
            this.addDataSlot(IntReferenceHolder.shared(this.costs, i));
            this.addDataSlot(IntReferenceHolder.shared(this.enchantClue, i));
            this.addDataSlot(IntReferenceHolder.shared(this.levelClue, i));
        }

    }

    private float getPower(net.minecraft.world.World world, net.minecraft.util.math.BlockPos pos) {
        return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
    }

    /**
     * What to do when any of the slots change, meaning:
     * 1. An item to enchant item was added/removed
     * 2. An item was enchanted (and therefore removed)
     * 3. A reagent was added/removed
     * 4. A reagent was consumed
     */
    public void slotsChanged(IInventory inventory) {
        if (inventory == this.enchantSlots) {
            ItemStack itemstack = inventory.getItem(0);
            if (!itemstack.isEmpty() && itemstack.isEnchantable()) {
                this.access.execute((world, blockPos) -> {
                    int power = 0;

                    for(int k = -1; k <= 1; ++k) {
                        for(int l = -1; l <= 1; ++l) {
                            if ((k != 0 || l != 0) && world.isEmptyBlock(blockPos.offset(l, 0, k)) && world.isEmptyBlock(blockPos.offset(l, 1, k))) {
                                power += getPower(world, blockPos.offset(l * 2, 0, k * 2));
                                power += getPower(world, blockPos.offset(l * 2, 1, k * 2));

                                if (l != 0 && k != 0) {
                                    power += getPower(world, blockPos.offset(l * 2, 0, k));
                                    power += getPower(world, blockPos.offset(l * 2, 1, k));
                                    power += getPower(world, blockPos.offset(l, 0, k * 2));
                                    power += getPower(world, blockPos.offset(l, 1, k * 2));
                                }
                            }
                        }
                    }

                    this.random.setSeed((long)this.enchantmentSeed.get());

                    for(int index = 0; index < 3; ++index) {
                        this.costs[index] = EnchantmentHelper.getEnchantmentCost(this.random, index, (int)power, itemstack);
                        this.enchantClue[index] = -1;
                        this.levelClue[index] = -1;
                        if (this.costs[index] < index + 1) {
                            this.costs[index] = 0;
                        }
                        this.costs[index] = net.minecraftforge.event.ForgeEventFactory.onEnchantmentLevelSet(world, blockPos, index, (int)power, itemstack, costs[index]);
                    }

                    for(int index = 0; index < 3; ++index) {
                        if (this.costs[index] > 0) {
                            List<EnchantmentData> list = this.getEnchantmentList(itemstack, this.costs[index]);
                            if (list != null && !list.isEmpty()) {
                                EnchantmentData enchantmentdata = list.get(this.random.nextInt(list.size()));
                                this.enchantClue[index] = Registry.ENCHANTMENT.getId(enchantmentdata.enchantment);
                                this.levelClue[index] = enchantmentdata.level;
                            }
                        }
                    }

                    this.broadcastChanges();
                });
            } else {
                for(int i = 0; i < 3; ++i) {
                    this.costs[i] = 0;
                    this.enchantClue[i] = -1;
                    this.levelClue[i] = -1;
                }
            }
        }

    }

    public boolean clickMenuButton(PlayerEntity player, int index) {
        ItemStack enchantItem = this.enchantSlots.getItem(0);
        ItemStack reagentItem = this.enchantSlots.getItem(1);
        // TODO: The names here are probably wrong - figure out what the names here should really be
        int cost = index + 1;
        if ((reagentItem.isEmpty() || reagentItem.getCount() < cost) && !player.abilities.instabuild) {
            return false;
        } else if (this.costs[index] <= 0 || enchantItem.isEmpty() || (player.experienceLevel < cost || player.experienceLevel < this.costs[index]) && !player.abilities.instabuild) {
            return false;
        } else {
            this.access.execute((worldIn, blockPos) -> {
                ItemStack resultItem = enchantItem;
                List<EnchantmentData> list = this.getEnchantmentList(enchantItem, this.costs[index]);
                if (!list.isEmpty()) {
                    player.onEnchantmentPerformed(enchantItem, cost);
                    boolean isBookItem = enchantItem.getItem() == Items.BOOK;
                    if (isBookItem) {
                        resultItem = new ItemStack(Items.ENCHANTED_BOOK);
                        CompoundNBT compoundnbt = enchantItem.getTag();
                        if (compoundnbt != null) {
                            resultItem.setTag(compoundnbt.copy());
                        }

                        this.enchantSlots.setItem(0, resultItem);
                    }

                    for(int j = 0; j < list.size(); ++j) {
                        EnchantmentData enchantmentdata = list.get(j);
                        if (isBookItem) {
                            EnchantedBookItem.addEnchantment(resultItem, enchantmentdata);
                        } else {
                            resultItem.enchant(enchantmentdata.enchantment, enchantmentdata.level);
                        }
                    }

                    if (!player.abilities.instabuild) {
                        reagentItem.shrink(cost);
                        if (reagentItem.isEmpty()) {
                            this.enchantSlots.setItem(1, ItemStack.EMPTY);
                        }
                    }

                    player.awardStat(Stats.ENCHANT_ITEM);
                    if (player instanceof ServerPlayerEntity) {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, resultItem, cost);
                    }

                    this.enchantSlots.setChanged();
                    this.enchantmentSeed.set(player.getEnchantmentSeed());
                    this.slotsChanged(this.enchantSlots);
                    worldIn.playSound((PlayerEntity)null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
                }

            });
            return true;
        }
    }

    /**
     * Internal method to get the list of enchantments for the given itemStack
     *
     * @param itemStack
     * @param enchantmentCost
     * @return
     */
    private List<EnchantmentData> getEnchantmentList(ItemStack itemStack, int enchantmentCost) {
        List<EnchantmentData> availableEnchantsForItem = EnchantmentHelper.getAvailableEnchantmentResults(enchantmentCost, itemStack, false);
        ArrayList<String> categories = new ArrayList<>();
        for (EnchantmentData data: availableEnchantsForItem) {
            String enchantmentNameAndLevel = data.enchantment.getFullname(data.level).getString();
            List<String> parts = new ArrayList<>(Arrays.asList(enchantmentNameAndLevel.split(" ")));
            parts.remove((parts.size() - 1));
            StringBuilder builder = new StringBuilder();
            for (int i = 0; i < parts.size(); ++i) {
                builder.append(parts.get(i));
                if (i != (parts.size() - 1)) {
                    builder.append(" ");
                }
            }
            String enchantmentName = builder.toString();
            if (!categories.contains(enchantmentName)) {
                categories.add(enchantmentName);
            }
        }
        LOGGER.info(String.format("Enchantment categories for item: %s", categories));
        return availableEnchantsForItem;
    }

    @OnlyIn(Dist.CLIENT)
    public int getGoldCount() {
        ItemStack itemstack = this.enchantSlots.getItem(1);
        return itemstack.isEmpty() ? 0 : itemstack.getCount();
    }

    @OnlyIn(Dist.CLIENT)
    public int getEnchantmentSeed() {
        return this.enchantmentSeed.get();
    }

    public void removed(PlayerEntity p_75134_1_) {
        super.removed(p_75134_1_);
        this.access.execute((p_217004_2_, p_217004_3_) -> {
            this.clearContainer(p_75134_1_, p_75134_1_.level, this.enchantSlots);
        });
    }

    public boolean stillValid(PlayerEntity p_75145_1_) {
        return stillValid(this.access, p_75145_1_, EnchantmentsSeer.SEERS_ENCHANTING_TABLE_BLOCK.get());
    }

    public ItemStack quickMoveStack(PlayerEntity p_82846_1_, int slotIndex) {
        ItemStack itemStackCopy = ItemStack.EMPTY;
        Slot slot = this.slots.get(slotIndex);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack = slot.getItem();
            itemStackCopy = itemStack.copy();
            if (slotIndex == 0) {
                if (!this.moveItemStackTo(itemStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (slotIndex == 1) {
                if (!this.moveItemStackTo(itemStack, 2, 38, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (itemStack.getItem() == Items.LAPIS_LAZULI) {
                if (!this.moveItemStackTo(itemStack, 1, 2, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (this.slots.get(0).hasItem() || !this.slots.get(0).mayPlace(itemStack)) {
                    return ItemStack.EMPTY;
                }

                ItemStack itemstack2 = itemStack.copy();
                itemstack2.setCount(1);
                itemStack.shrink(1);
                this.slots.get(0).set(itemstack2);
            }

            if (itemStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (itemStack.getCount() == itemStackCopy.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(p_82846_1_, itemStack);
        }

        return itemStackCopy;
    }
}
