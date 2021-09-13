package com.nuggylib.enchantmentsseer.client.gui.block;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.gui.GuiEnchantmentsSeerTile;
import com.nuggylib.enchantmentsseer.common.inventory.container.SeersEnchantingTableContainer;
import com.nuggylib.enchantmentsseer.common.tile.SeersEnchantingTableTileEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;

import javax.annotation.Nonnull;

/**
 * The Mekanism-inspired GUI class for the Seer's Enchanting Table
 *
 * This class should be far simpler than any of the vanilla Minecraft {@link Screen} class because all "lower-level"
 * architecture is abstracted out through the various parent classes and interfaces.
 *
 * <b>The only elements that should be added in this class are non-slot elements.</b> The reason why you can't add slots
 * in this class is because they won't be connected to the underlying {@link Container}. Confusingly, you may actually be
 * able to add slots in this class and have it appear as-expected in the GUI. However, such slots will exist outside the
 * scope of the corresponding Container and will therefore bypass any required logic applied to slots through the Container
 * class.
 */
public class GuiSeersEnchantingTable extends GuiEnchantmentsSeerTile<SeersEnchantingTableTileEntity, SeersEnchantingTableContainer> {

    public GuiSeersEnchantingTable(SeersEnchantingTableContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
        dynamicSlots = true;
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
    }

    @Override
    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        drawString(matrix, inventory.getDisplayName(), inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(matrix, mouseX, mouseY);
    }

}
