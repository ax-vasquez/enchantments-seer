package com.nuggylib.enchantmentsseer.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.EnchantmentsSeerTileContainer;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;

/**
 * Inspired by code from Mekanism
 *
 * @param <TILE>
 * @param <CONTAINER>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/GuiMekanismTile.java"
 */
public abstract class GuiEnchantmentsSeerTile<TILE extends TileEntityEnchantmentsSeer, CONTAINER extends EnchantmentsSeerTileContainer<TILE>> extends GuiEnchantmentsSeer<CONTAINER> {

    protected final TILE tile;

    protected GuiEnchantmentsSeerTile(CONTAINER container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        tile = container.getTileEntity();
    }

    public TILE getTileEntity() {
        return tile;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        addGenericTabs();
    }

    protected void addGenericTabs() {}

    public void renderTitleText(MatrixStack matrix) {
        EnchantmentsSeer.LOGGER.info(String.format("GuiEnchantmentsSeerTile#renderTitleText: %s", tile.getName()));
        drawTitleText(matrix, tile.getName(), titleLabelY);
    }

    @Override
    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        super.drawForegroundText(matrix, mouseX, mouseY);
    }
}
