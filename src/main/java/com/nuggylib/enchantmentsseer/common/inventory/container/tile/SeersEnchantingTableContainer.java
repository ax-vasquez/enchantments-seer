package com.nuggylib.enchantmentsseer.common.inventory.container.tile;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerContainerTypes;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * The container class for the Seer's Enchanting table
 */
public class SeersEnchantingTableContainer extends EnchantmentsSeerTileContainer<TileEntitySeersEnchantmentTable> {

    private final IInventory enchantSlots = new Inventory(2) {
        public void setChanged() {
            super.setChanged();
            SeersEnchantingTableContainer.this.slotsChanged(this);
        }
    };

    private final IWorldPosCallable access;
    // TODO: Figure out how we will handle our version of the enchantClue, levelClue and costs

    public SeersEnchantingTableContainer(int id, PlayerInventory inv, TileEntitySeersEnchantmentTable tile, IWorldPosCallable access) {
        super(EnchantmentsSeerContainerTypes.SEERS_ENCHANTING_TABLE, id, inv, tile);
        this.access = access;
        // TODO: See what data slots we will need to add (or what they even do)
    }

    public SeersEnchantingTableContainer(int id, PlayerInventory inv, PacketBuffer buf) {
        this(id, inv, getTileFromBuf(buf, TileEntitySeersEnchantmentTable.class), IWorldPosCallable.NULL);
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
        return enchantmentList.stream().map(enchantment -> new EnchantmentData(enchantment, 1)).collect(Collectors.toList());
    }

    public void slotsChanged(IInventory inventory) {
        EnchantmentsSeer.logger.info("Slots changed!");
        if (inventory == this.enchantSlots) {
            ItemStack itemToEnchant = inventory.getItem(0);
            if (!itemToEnchant.isEmpty() && itemToEnchant.isEnchantable()) {
                this.access.execute((p_217002_2_, p_217002_3_) -> {

                    List<EnchantmentData> list = this.getEnchantmentList(itemToEnchant);
                    if (list != null && !list.isEmpty()) {
                        for (EnchantmentData data: list) {
                            EnchantmentsSeer.logger.info(String.format("Enchantment: %s", data.enchantment.getFullname(1)));
                        }
                    }

                    this.broadcastChanges();
                });
            }
        }

    }

}
