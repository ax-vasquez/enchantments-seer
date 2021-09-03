package com.nuggylib.enchantmentsseer.client.gui.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nuggylib.enchantmentsseer.client.gui.GuiEnchantmentsSeerTile;
import com.nuggylib.enchantmentsseer.client.gui.element.GuiInnerScreen;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.EnchantmentsSeerTileContainer;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.*;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Random;

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
        EnchantmentsSeer.LOGGER.info(String.format("Creating Seer's Enchanting Table GUI with inventory: %s", inventory));
    }

    @Override
    protected void addGuiElements() {
        EnchantmentsSeer.LOGGER.info("GuiSeersEnchantingTable#addGuiElements");
        super.addGuiElements();
        EnchantmentsSeer.LOGGER.info("Adding GUI elements for Seer's Enchanting Table");
        addButton(new GuiInnerScreen(this, 48, 23, 80, 28, () -> Arrays.asList(
                EnchantmentsSeerLang.ENCHANT_ITEM.translate(),
                EnchantmentsSeerLang.ENCHANT_REAGENT.translate()
        )));
    }

    @Override
    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        EnchantmentsSeer.LOGGER.info("Drawing foreground for Seer's Enchanting Table");
        renderTitleText(matrix);
        drawString(matrix, inventory.getDisplayName(), inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(matrix, mouseX, mouseY);
    }

}
