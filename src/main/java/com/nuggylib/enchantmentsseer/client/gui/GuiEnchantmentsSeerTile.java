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
public abstract class GuiEnchantmentsSeerTile<TILE extends TileEntityEnchantmentsSeer, CONTAINER extends EnchantmentsSeerTileContainer<TILE>> extends AbstractGui<CONTAINER> {

    protected final TILE tile;

    protected GuiEnchantmentsSeerTile(CONTAINER container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        EnchantmentsSeer.LOGGER.info("GuiEnchantmentsSeer#GuiEnchantmentsSeerTile (constructor)");
        tile = container.getTileEntity();
    }

    public TILE getTileEntity() {
        return tile;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
        EnchantmentsSeer.LOGGER.info("GuiEnchantmentsSeerTile#addGuiElements");
        addGenericTabs();
    }

    protected void addGenericTabs() {
        EnchantmentsSeer.LOGGER.info("GuiEnchantmentsSeerTile#addGenericTabs called");
    }

    public void renderTitleText(MatrixStack matrix) {
        EnchantmentsSeer.LOGGER.info("GuiEnchantmentsSeerTile#renderTitleText called");
        drawTitleText(matrix, tile.getName(), titleLabelY);
    }

    @Override
    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        super.drawForegroundText(matrix, mouseX, mouseY);
        EnchantmentsSeer.LOGGER.info("GuiEnchantmentsSeerTile#drawForegroundText called");
    }
}
