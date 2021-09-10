package com.nuggylib.enchantmentsseer.client.gui;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nuggylib.enchantmentsseer.common.inventory.container.SeersEnchantingTableContainer;
import net.minecraft.client.gui.screen.EnchantmentScreen;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
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

import java.util.List;
import java.util.Random;

/**
 * Our version of the base {@link EnchantmentScreen}
 */
public class SeersEnchantingTableScreen extends ContainerScreen<SeersEnchantingTableContainer> {

    private static final ResourceLocation ENCHANTING_TABLE_LOCATION = new ResourceLocation("minecraft:textures/gui/container/enchanting_table.png");
    private static final ResourceLocation ENCHANTING_BOOK_LOCATION = new ResourceLocation("minecraft:textures/entity/enchanting_table_book.png");
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

    public SeersEnchantingTableScreen(SeersEnchantingTableContainer container, PlayerInventory inv, ITextComponent title) {
        super(container, inv, title);
    }

    public void tick() {
        super.tick();
        this.tickBook();
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        // TODO: Modify this logic - the vanilla code assumes there will only be three elements to populate - our
        //  GUI will be different (we won't know how many there will be until an enchant item is added)
        //int i = (this.width - this.imageWidth) / 2;
        //int j = (this.height - this.imageHeight) / 2;

        //for(int k = 0; k < 3; ++k) {
        //    double d0 = mouseX - (double)(i + 60);
        //    double d1 = mouseY - (double)(j + 14 + 19 * k);
        //    if (d0 >= 0.0D && d1 >= 0.0D && d0 < 108.0D && d1 < 19.0D && this.menu.clickMenuButton(this.minecraft.player, k)) {
        //        this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, k);
        //        return true;
        //    }
        //}
        return super.mouseClicked(mouseX, mouseY, button);
    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialTick, int mouseX, int mouseY) {
        RenderHelper.setupForFlatItems();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(ENCHANTING_TABLE_LOCATION);
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;
        this.blit(matrix, i, j, 0, 0, this.imageWidth, this.imageHeight);
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        int k = (int)this.minecraft.getWindow().getGuiScale();
        RenderSystem.viewport((this.width - 320) / 2 * k, (this.height - 240) / 2 * k, 320 * k, 240 * k);
        RenderSystem.translatef(-0.34F, 0.23F, 0.0F);
        RenderSystem.multMatrix(Matrix4f.perspective(90.0D, 1.3333334F, 9.0F, 80.0F));
        RenderSystem.matrixMode(5888);
        matrix.pushPose();
        MatrixStack.Entry matrixstack$entry = matrix.last();
        matrixstack$entry.pose().setIdentity();
        matrixstack$entry.normal().setIdentity();
        matrix.translate(0.0D, (double)3.3F, 1984.0D);
        matrix.scale(5.0F, 5.0F, 5.0F);
        matrix.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrix.mulPose(Vector3f.XP.rotationDegrees(20.0F));
        float f1 = MathHelper.lerp(partialTick, this.oOpen, this.open);
        matrix.translate((double)((1.0F - f1) * 0.2F), (double)((1.0F - f1) * 0.1F), (double)((1.0F - f1) * 0.25F));
        float f2 = -(1.0F - f1) * 90.0F - 90.0F;
        matrix.mulPose(Vector3f.YP.rotationDegrees(f2));
        matrix.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        float f3 = MathHelper.lerp(partialTick, this.oFlip, this.flip) + 0.25F;
        float f4 = MathHelper.lerp(partialTick, this.oFlip, this.flip) + 0.75F;
        f3 = (f3 - (float)MathHelper.fastFloor((double)f3)) * 1.6F - 0.3F;
        f4 = (f4 - (float)MathHelper.fastFloor((double)f4)) * 1.6F - 0.3F;
        if (f3 < 0.0F) {
            f3 = 0.0F;
        }

        if (f4 < 0.0F) {
            f4 = 0.0F;
        }

        if (f3 > 1.0F) {
            f3 = 1.0F;
        }

        if (f4 > 1.0F) {
            f4 = 1.0F;
        }

        RenderSystem.enableRescaleNormal();
        BOOK_MODEL.setupAnim(0.0F, f3, f4, f1);
        IRenderTypeBuffer.Impl irendertypebuffer$impl = IRenderTypeBuffer.immediate(Tessellator.getInstance().getBuilder());
        IVertexBuilder ivertexbuilder = irendertypebuffer$impl.getBuffer(BOOK_MODEL.renderType(ENCHANTING_BOOK_LOCATION));
        BOOK_MODEL.renderToBuffer(matrix, ivertexbuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        irendertypebuffer$impl.endBatch();
        matrix.popPose();
        RenderSystem.matrixMode(5889);
        RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(5888);
        RenderHelper.setupFor3DItems();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        int goldCost = 0;

        for(int i1 = 0; i1 < 3; ++i1) {
            int j1 = i + 60;
            int k1 = j1 + 20;
            this.setBlitOffset(0);
            this.minecraft.getTextureManager().bind(ENCHANTING_TABLE_LOCATION);
            int cost = 0;
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (cost == 0) {
                this.blit(matrix, j1, j + 14 + 19 * i1, 0, 185, 108, 19);
            } else {
                String s = "" + cost;
                int i2 = 86 - this.font.width(s);
                ITextProperties itextproperties = EnchantmentNameParts.getInstance().getRandomName(this.font, i2);
                int j2 = 6839882;
                if (((goldCost < i1 + 1 || this.minecraft.player.experienceLevel < cost) && !this.minecraft.player.abilities.instabuild)) { // Forge: render buttons as disabled when enchantable but enchantability not met on lower levels
                    this.blit(matrix, j1, j + 14 + 19 * i1, 0, 185, 108, 19);
                    this.blit(matrix, j1 + 1, j + 15 + 19 * i1, 16 * i1, 239, 16, 16);
                    this.font.drawWordWrap(itextproperties, k1, j + 16 + 19 * i1, i2, (j2 & 16711422) >> 1);
                    j2 = 4226832;
                } else {
                    int k2 = mouseX - (i + 60);
                    int l2 = mouseY - (j + 14 + 19 * i1);
                    if (k2 >= 0 && l2 >= 0 && k2 < 108 && l2 < 19) {
                        this.blit(matrix, j1, j + 14 + 19 * i1, 0, 204, 108, 19);
                        j2 = 16777088;
                    } else {
                        this.blit(matrix, j1, j + 14 + 19 * i1, 0, 166, 108, 19);
                    }

                    this.blit(matrix, j1 + 1, j + 15 + 19 * i1, 16 * i1, 223, 16, 16);
                    this.font.drawWordWrap(itextproperties, k1, j + 16 + 19 * i1, i2, j2);
                    j2 = 8453920;
                }

                this.font.drawShadow(matrix, s, (float)(k1 + 86 - this.font.width(s)), (float)(j + 16 + 19 * i1 + 7), j2);
            }
        }

    }

    @Override
    public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        partialTicks = this.minecraft.getFrameTime();
        this.renderBackground(matrix);
        super.render(matrix, mouseX, mouseY, partialTicks);
        this.renderTooltip(matrix, mouseX, mouseY);
    }

    /**
     * Book flip animation logic for the book displayed in the GUI
     */
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
        boolean displayingEnchantments = false;

        // TODO: Modify this logic so that it sets displayingEnchantments to true when there are enchantments to
        //  display in the GUI. Vanilla logic does by checking not only if there are enchantments, but also if any of
        //  the listed enchantments has a non-zero cost
//        for(int i = 0; i < 3; ++i) {
//            if ((this.menu).costs[i] != 0) {
//                displayingEnchantments = true;
//            }
//        }

        if (displayingEnchantments) {
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
