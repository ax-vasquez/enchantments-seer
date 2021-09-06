package com.nuggylib.enchantmentsseer.client.gui.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.gui.GuiEnchantmentsSeerTile;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.GuiSlot;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.SlotType;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.EnchantmentsSeerTileContainer;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.*;

import javax.annotation.Nonnull;

/**
 * The Seers Enchanting Table GUI class
 *
 * This class emulates the structure of Mekanism GUI classes.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/machine/GuiFuelwoodHeater.java"
 */
public class GuiSeersEnchantingTable extends GuiEnchantmentsSeerTile<TileEntitySeersEnchantmentTable, EnchantmentsSeerTileContainer<TileEntitySeersEnchantmentTable>> {

    public GuiSeersEnchantingTable(EnchantmentsSeerTileContainer<TileEntitySeersEnchantmentTable> container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addButton(new GuiSlot(SlotType.INPUT, this, 145, 20));
    }

    @Override
    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        renderTitleText(matrix);
        drawString(matrix, inventory.getDisplayName(), inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(matrix, mouseX, mouseY);
    }

}
