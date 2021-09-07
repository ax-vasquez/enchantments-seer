package com.nuggylib.enchantmentsseer.common.inventory.container.tile;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.EnchantmentsSeerInventory;
import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import com.nuggylib.enchantmentsseer.common.inventory.slot.EnchantmentsSeerSlot;
import com.nuggylib.enchantmentsseer.common.registration.impl.ContainerTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.util.WorldUtils;
import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.client.Minecraft;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.container.Slot;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraft.stats.Stats;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.IntReferenceHolder;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Inspired by Mekanism code
 *
 * @param <TILE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/tile/MekanismTileContainer.java"
 */
public class EnchantmentsSeerTileContainer<TILE extends TileEntityEnchantmentsSeer> extends EnchantmentsSeerContainer {

    private final IInventory enchantSlots = new EnchantmentsSeerInventory(2) {
        public void setChanged() {
            super.setChanged();
            EnchantmentsSeerTileContainer.this.slotsChanged(this);
        }
    };

    private final IWorldPosCallable access;
    public final int[] costs = new int[3];
    public final int[] enchantClue = new int[]{-1, -1, -1};
    public final int[] levelClue = new int[]{-1, -1, -1};

    @Nonnull
    protected final TILE tile;

    public EnchantmentsSeerTileContainer(ContainerTypeRegistryObject<?> type, int id, PlayerInventory inv, @Nonnull TILE tile) {
        this(type, id, inv, tile, IWorldPosCallable.NULL);
    }

    public EnchantmentsSeerTileContainer(ContainerTypeRegistryObject<?> type, int id, PlayerInventory inv, @Nonnull TILE tile, IWorldPosCallable access) {
        super(type, id, inv);
        this.tile = tile;
        this.access = access;
        addSlotsAndOpen();
    }

    public TILE getTileEntity() {
        return tile;
    }

    @Override
    protected void openInventory(@Nonnull PlayerInventory inv) {
        super.openInventory(inv);
        tile.open(inv.player);
    }

    @Override
    protected void closeInventory(@Nonnull PlayerEntity player) {
        super.closeInventory(player);
        tile.close(player);
    }

    /**
     * Determines the power level of the Seer's Enchanting Table
     *
     * The power level is basically the count of the number of bookshelves close to the table.
     *
     * Note that Vanilla logic hard-caps the power level to 15; whenever their internal logic gets them a value
     * greater than 15, they simply set it to 15.
     *
     * @return      The power level
     */
    private float getPower(World world, BlockPos pos) {
        return world.getBlockState(pos).getEnchantPowerBonus(world, pos);
    }

    @Override
    public void slotsChanged(@NotNull IInventory enchantingTableInventory) {
        if (enchantingTableInventory == this.enchantSlots) {
            ItemStack itemStackToEnchant = enchantingTableInventory.getItem(0);
            if (!itemStackToEnchant.isEmpty() && itemStackToEnchant.isEnchantable()) {
                this.access.execute((worldIn, blockPos) -> {
                    int power = 0;

                    // TODO: See what the Vanilla logic's purpose was
                    for(int k = -1; k <= 1; ++k) {
                        for(int l = -1; l <= 1; ++l) {
                            if ((k != 0 || l != 0) && worldIn.isEmptyBlock(blockPos.offset(l, 0, k)) && worldIn.isEmptyBlock(blockPos.offset(l, 1, k))) {
                                power += getPower(worldIn, blockPos.offset(l * 2, 0, k * 2));
                                power += getPower(worldIn, blockPos.offset(l * 2, 1, k * 2));

                                if (l != 0 && k != 0) {
                                    power += getPower(worldIn, blockPos.offset(l * 2, 0, k));
                                    power += getPower(worldIn, blockPos.offset(l * 2, 1, k));
                                    power += getPower(worldIn, blockPos.offset(l, 0, k * 2));
                                    power += getPower(worldIn, blockPos.offset(l, 1, k * 2));
                                }
                            }
                        }
                    }

                    for(int enchantmentRowNum = 0; enchantmentRowNum < 3; ++enchantmentRowNum) {
                        this.enchantClue[enchantmentRowNum] = -1;
                        if (this.costs[enchantmentRowNum] > 0) {
                            List<EnchantmentData> list = this.getEnchantmentList(itemStackToEnchant);
                            if (list != null && !list.isEmpty()) {
                                // TODO: The old code would grab a random item from the list 3 times - we need to modify this to render ALL enchantments
                                EnchantmentData enchantmentdata = list.get(enchantmentRowNum);
                                this.enchantClue[enchantmentRowNum] = Registry.ENCHANTMENT.getId(enchantmentdata.enchantment);
                                this.levelClue[enchantmentRowNum] = enchantmentdata.level;
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

    /**
     * Gets the full list of enchantments (by their {@link EnchantmentData} representation) for the given item stack
     *
     * @param itemStackToEnchant            The item to potentially enchant
     * @return                              The list of compatible enchantments for the given item
     */
    private List<EnchantmentData> getEnchantmentList(ItemStack itemStackToEnchant) {
        List<Enchantment> enchantmentList = new ArrayList<>(EnchantmentHelper.getEnchantments(itemStackToEnchant).keySet());
        // TODO: See if defaulting to 1 is okay - we can use 1 as the default slider selection for each enchantment row
        List<EnchantmentData> enchantmentDataList = enchantmentList.stream().map(enchantment -> new EnchantmentData(enchantment, 1)).collect(Collectors.toList());
        return enchantmentDataList;
    }

    @Override
    public boolean stillValid(@Nonnull PlayerEntity player) {
        //prevent Containers from remaining valid after the chunk has unloaded;
        return tile.hasGui() && !tile.isRemoved() && WorldUtils.isBlockLoaded(tile.getLevel(), tile.getBlockPos());
    }

    @Override
    protected void addSlots() {
        super.addSlots();
        this.addDataSlot(IntReferenceHolder.shared(this.enchantClue, 0));
        this.addDataSlot(IntReferenceHolder.shared(this.enchantClue, 1));
        this.addDataSlot(IntReferenceHolder.shared(this.enchantClue, 2));
        this.addDataSlot(IntReferenceHolder.shared(this.levelClue, 0));
        this.addDataSlot(IntReferenceHolder.shared(this.levelClue, 1));
        this.addDataSlot(IntReferenceHolder.shared(this.levelClue, 2));
        // If you need to add more slots, add them to the corresponding TileEntity class
        if (tile.hasInventory()) {
            //Get all the inventory slots the tile has
            List<IInventorySlot> inventorySlots = tile.getInventorySlots(null);
            for (IInventorySlot inventorySlot : inventorySlots) {
                EnchantmentsSeer.logger.info("Adding inventory slot");
                Slot containerSlot = inventorySlot.createContainerSlot();
                if (containerSlot != null) {
                    addSlot(containerSlot);
                }
            }
        }
    }

    /**
     *
     * @param player            The player clicking the button
     * @param cost              Cost amount will be somewhat contextual - when recharging, it's the number of Seer's Stones needed to recharge.
     *                              When enchanting, the cost is always 1, because the enchanting process will consume the book in the process.
     * @return                  TODO - clarify the intent of the return value here - it's going to be true or false, but what exactly does that mean?
     */
    public boolean clickMenuButton(@NotNull PlayerEntity player, int cost) {
        ItemStack enchantItemStack = this.enchantSlots.getItem(0);
        ItemStack reagentItemStack = this.enchantSlots.getItem(1);
        int i = cost + 1;
        if ((reagentItemStack.isEmpty() || reagentItemStack.getCount() < i) && !player.abilities.instabuild) {
            return false;
        } else if (this.costs[cost] <= 0 || enchantItemStack.isEmpty() || (player.experienceLevel < i || player.experienceLevel < this.costs[cost]) && !player.abilities.instabuild) {
            return false;
        } else {
            this.access.execute((worldIn, blockPos) -> {
                ItemStack enchantItemStackCopy = enchantItemStack;
                List<EnchantmentData> list = this.getEnchantmentList(enchantItemStack);
                if (!list.isEmpty()) {
                    player.onEnchantmentPerformed(enchantItemStack, i);
                    boolean flag = enchantItemStack.getItem() == Items.BOOK;
                    if (flag) {
                        enchantItemStackCopy = new ItemStack(Items.ENCHANTED_BOOK);
                        CompoundNBT compoundnbt = enchantItemStack.getTag();
                        if (compoundnbt != null) {
                            enchantItemStackCopy.setTag(compoundnbt.copy());
                        }

                        this.enchantSlots.setItem(0, enchantItemStackCopy);
                    }

                    for(int j = 0; j < list.size(); ++j) {
                        EnchantmentData enchantmentdata = list.get(j);
                        if (flag) {
                            EnchantedBookItem.addEnchantment(enchantItemStackCopy, enchantmentdata);
                        } else {
                            enchantItemStackCopy.enchant(enchantmentdata.enchantment, enchantmentdata.level);
                        }
                    }

                    if (!player.abilities.instabuild) {
                        reagentItemStack.shrink(i);
                        if (reagentItemStack.isEmpty()) {
                            this.enchantSlots.setItem(1, ItemStack.EMPTY);
                        }
                    }

                    player.awardStat(Stats.ENCHANT_ITEM);
                    if (player instanceof ServerPlayerEntity) {
                        CriteriaTriggers.ENCHANTED_ITEM.trigger((ServerPlayerEntity)player, enchantItemStackCopy, i);
                    }

                    this.enchantSlots.setChanged();
                    this.slotsChanged(this.enchantSlots);
                    worldIn.playSound((PlayerEntity)null, blockPos, SoundEvents.ENCHANTMENT_TABLE_USE, SoundCategory.BLOCKS, 1.0F, worldIn.random.nextFloat() * 0.1F + 0.9F);
                }

            });
            return true;
        }
    }

    @Nonnull
    public static <TILE extends TileEntity> TILE getTileFromBuf(PacketBuffer buf, Class<TILE> type) {
        if (buf == null) {
            throw new IllegalArgumentException("Null packet buffer");
        }
        return DistExecutor.unsafeCallWhenOn(Dist.CLIENT, () -> () -> {
            BlockPos pos = buf.readBlockPos();
            TILE tile = WorldUtils.getTileEntity(type, Minecraft.getInstance().level, pos);
            if (tile == null) {
                throw new IllegalStateException("Client could not locate tile at " + pos + " for tile container. "
                        + "This is likely caused by a mod breaking client side tile lookup");
            }
            return tile;
        });
    }
}
