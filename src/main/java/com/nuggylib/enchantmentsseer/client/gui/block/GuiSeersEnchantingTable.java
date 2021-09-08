package com.nuggylib.enchantmentsseer.client.gui.block;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nuggylib.enchantmentsseer.client.gui.GuiEnchantmentsSeerTile;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.GuiSlot;
import com.nuggylib.enchantmentsseer.client.gui.element.slot.SlotType;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.EnchantmentsSeerTileContainer;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.SeersEnchantingTableContainer;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnchantmentNameParts;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.util.text.*;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Random;

/**
 * The Seers Enchanting Table GUI class
 *
 * This class emulates the structure of Mekanism GUI classes.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/machine/GuiFuelwoodHeater.java"
 */
public class GuiSeersEnchantingTable extends GuiEnchantmentsSeerTile<TileEntitySeersEnchantmentTable, SeersEnchantingTableContainer> {

    private static final ResourceLocation ENCHANTING_BOOK_LOCATION = new ResourceLocation("enchantments-seer:textures/entity/enchanting_table_book.png");
    private static final BookModel BOOK_MODEL = new BookModel();
    private final Random random = new Random();
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.EMPTY;

    public GuiSeersEnchantingTable(SeersEnchantingTableContainer container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
        dynamicSlots = true;
    }

    @Override
    public void tick() {
        super.tick();
        this.tickBook();
    }

    @Override
    protected void addGuiElements() {
        super.addGuiElements();
    }

    @Override
    protected void drawForegroundText(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        renderTitleText(matrix);
        drawString(matrix, inventory.getDisplayName(), inventoryLabelX, inventoryLabelY, titleTextColor());
        super.drawForegroundText(matrix, mouseX, mouseY);
    }

    // TODO: Figure out how to get the book animation rendering, as well as the enchantment rows
    protected void renderBg(MatrixStack matrix, float partialTick, int mouseX, int mouseY) {
        super.renderBg(matrix, partialTick, mouseX, mouseY);
    }

    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        super.render(matrix, mouseX, mouseY, partialTicks);
    }

    public void tickBook() {
        ItemStack itemstack = this.menu.getSlot(0).getItem();
        if (!ItemStack.matches(itemstack, this.last)) {
            this.last = itemstack;

            do {
                this.flipT += (float)(this.random.nextInt(4) - this.random.nextInt(4));
            } while(this.flip <= this.flipT + 1.0F && this.flip >= this.flipT - 1.0F);
        }

        ++this.time;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;



        if (flag) {
            this.open += 0.2F;
        } else {
            this.open -= 0.2F;
        }

        this.open = MathHelper.clamp(this.open, 0.0F, 1.0F);
        float f1 = (this.flipT - this.flip) * 0.4F;
        float f = 0.2F;
        f1 = MathHelper.clamp(f1, -0.2F, 0.2F);
        this.flipA += (f1 - this.flipA) * 0.9F;
        this.flip += this.flipA;
    }

}
