package com.nuggylib.enchantmentsseer.common.inventory.container.tile;

import com.nuggylib.enchantmentsseer.common.registries.EnchantmentsSeerContainerTypes;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.IWorldPosCallable;
import org.jetbrains.annotations.NotNull;

/**
 * The container class for the Seer's Enchanting table
 */
public class SeersEnchantingTableContainer extends EnchantmentsSeerTileContainer<TileEntitySeersEnchantmentTable> {

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

    @Override
    protected void addInventorySlots(@NotNull PlayerInventory inv) {
        super.addInventorySlots(inv);
    }

    @Override
    protected void addSlots() {
        super.addSlots();
    }
}
