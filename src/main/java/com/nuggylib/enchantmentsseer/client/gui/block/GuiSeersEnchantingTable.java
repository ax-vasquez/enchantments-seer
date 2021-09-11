package com.nuggylib.enchantmentsseer.client.gui.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.gui.GuiEnchantmentsSeerTile;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.GuiSlot;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.SlotType;
import com.nuggylib.enchantmentsseer.common.inventory.container.SeersEnchantingTableContainer;
import com.nuggylib.enchantmentsseer.common.tile.SeersEnchantingTableTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

public class GuiSeersEnchantingTable extends GuiEnchantmentsSeerTile<SeersEnchantingTableTileEntity, SeersEnchantingTableContainer> {

    public GuiSeersEnchantingTable(SeersEnchantingTableContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addButton(new GuiSlot(SlotType.INPUT, this, 145, 20));
    }

    @Override
    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        drawString(matrix, inventory.getDisplayName(), inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(matrix, mouseX, mouseY);
    }

}
