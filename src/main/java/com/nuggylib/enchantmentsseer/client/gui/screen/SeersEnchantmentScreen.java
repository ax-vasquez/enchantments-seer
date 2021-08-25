package com.nuggylib.enchantmentsseer.client.gui.screen;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nuggylib.enchantmentsseer.inventory.container.SeersEnchantingTableContainer;
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
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;
import java.util.Random;

@OnlyIn(Dist.CLIENT)
public class SeersEnchantmentScreen extends ContainerScreen<SeersEnchantingTableContainer> {
    private static final ResourceLocation ENCHANTING_TABLE_LOCATION = new ResourceLocation("enchantments-seer:textures/gui/container/seers_enchanting_table.png");
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

    /**
     * @param container                 Is used internally by ContainerScreen as the `menu` field
     * @param playerInventory
     * @param textComponent
     */
    public SeersEnchantmentScreen(SeersEnchantingTableContainer container, PlayerInventory playerInventory, ITextComponent textComponent) {
        super(container, playerInventory, textComponent);
    }

    public void tick() {
        super.tick();
        this.tickBook();
    }

    public boolean mouseClicked(double p_231044_1_, double p_231044_3_, int p_231044_5_) {
        int i = (this.width - this.imageWidth) / 2;
        int j = (this.height - this.imageHeight) / 2;

        for(int k = 0; k < 3; ++k) {
            double d0 = p_231044_1_ - (double)(i + 60);
            double d1 = p_231044_3_ - (double)(j + 14 + 19 * k);
            if (d0 >= 0.0D && d1 >= 0.0D && d0 < 108.0D && d1 < 19.0D && this.menu.clickMenuButton(this.minecraft.player, k)) {
                this.minecraft.gameMode.handleInventoryButtonClick((this.menu).containerId, k);
                return true;
            }
        }

        return super.mouseClicked(p_231044_1_, p_231044_3_, p_231044_5_);
    }

    protected void renderBg(MatrixStack matrixStack, float p_230450_2_, int p_230450_3_, int p_230450_4_) {
        RenderHelper.setupForFlatItems();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(ENCHANTING_TABLE_LOCATION);
        int widthMargin = (this.width - this.imageWidth) / 2;
        int heightMargin = (this.height - this.imageHeight) / 2;
        // Draws the gray background
        this.blit(matrixStack, widthMargin, heightMargin, 0, 0, this.imageWidth, this.imageHeight);
        RenderSystem.matrixMode(5889);
        RenderSystem.pushMatrix();
        RenderSystem.loadIdentity();
        int guiScale = (int)this.minecraft.getWindow().getGuiScale();
        RenderSystem.viewport((this.width - 320) / 2 * guiScale, (this.height - 240) / 2 * guiScale, 320 * guiScale, 240 * guiScale);
        RenderSystem.translatef(-0.34F, 0.23F, 0.0F);
        RenderSystem.multMatrix(Matrix4f.perspective(90.0D, 1.3333334F, 9.0F, 80.0F));
        RenderSystem.matrixMode(5888);
        matrixStack.pushPose();
        MatrixStack.Entry matrixstack$entry = matrixStack.last();
        matrixstack$entry.pose().setIdentity();
        matrixstack$entry.normal().setIdentity();
        matrixStack.translate(0.0D, (double)3.3F, 1984.0D);
        float f = 5.0F;
        matrixStack.scale(5.0F, 5.0F, 5.0F);
        matrixStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(20.0F));
        float f1 = MathHelper.lerp(p_230450_2_, this.oOpen, this.open);
        matrixStack.translate((double)((1.0F - f1) * 0.2F), (double)((1.0F - f1) * 0.1F), (double)((1.0F - f1) * 0.25F));
        float f2 = -(1.0F - f1) * 90.0F - 90.0F;
        matrixStack.mulPose(Vector3f.YP.rotationDegrees(f2));
        matrixStack.mulPose(Vector3f.XP.rotationDegrees(180.0F));
        float f3 = MathHelper.lerp(p_230450_2_, this.oFlip, this.flip) + 0.25F;
        float f4 = MathHelper.lerp(p_230450_2_, this.oFlip, this.flip) + 0.75F;
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
        IVertexBuilder bookVertexBuilder = irendertypebuffer$impl.getBuffer(BOOK_MODEL.renderType(ENCHANTING_BOOK_LOCATION));
        BOOK_MODEL.renderToBuffer(matrixStack, bookVertexBuilder, 15728880, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        irendertypebuffer$impl.endBatch();
        matrixStack.popPose();
        RenderSystem.matrixMode(5889);
        RenderSystem.viewport(0, 0, this.minecraft.getWindow().getWidth(), this.minecraft.getWindow().getHeight());
        RenderSystem.popMatrix();
        RenderSystem.matrixMode(5888);
        RenderHelper.setupFor3DItems();
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        EnchantmentNameParts.getInstance().initSeed((long)this.menu.getEnchantmentSeed());
        int goldCount = this.menu.getGoldCount();

        for(int index = 0; index < 3; ++index) {
            int lower = widthMargin + 60;
            int upper = lower + 20;
            this.setBlitOffset(0);
            this.minecraft.getTextureManager().bind(ENCHANTING_TABLE_LOCATION);
            int cost = (this.menu).costs[index];
            RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
            if (cost == 0) {
                this.blit(matrixStack, lower, heightMargin + 14 + 19 * index, 0, 185, 108, 19);
            } else {
                String s = "" + cost;
                int i2 = 86 - this.font.width(s);
                ITextProperties itextproperties = EnchantmentNameParts.getInstance().getRandomName(this.font, i2);
                int j2 = 6839882;
                if ((goldCount < index + 1 || this.minecraft.player.experienceLevel < cost) && !this.minecraft.player.abilities.instabuild) {
                    this.blit(matrixStack, lower, heightMargin + 14 + 19 * index, 0, 185, 108, 19);
                    this.blit(matrixStack, lower + 1, heightMargin + 15 + 19 * index, 16 * index, 239, 16, 16);
                    this.font.drawWordWrap(itextproperties, upper, heightMargin + 16 + 19 * index, i2, (j2 & 16711422) >> 1);
                    j2 = 4226832;
                } else {
                    int k2 = p_230450_3_ - (widthMargin + 60);
                    int l2 = p_230450_4_ - (heightMargin + 14 + 19 * index);
                    if (k2 >= 0 && l2 >= 0 && k2 < 108 && l2 < 19) {
                        this.blit(matrixStack, lower, heightMargin + 14 + 19 * index, 0, 204, 108, 19);
                        j2 = 16777088;
                    } else {
                        this.blit(matrixStack, lower, heightMargin + 14 + 19 * index, 0, 166, 108, 19);
                    }

                    this.blit(matrixStack, lower + 1, heightMargin + 15 + 19 * index, 16 * index, 223, 16, 16);
                    this.font.drawWordWrap(itextproperties, upper, heightMargin + 16 + 19 * index, i2, j2);
                    j2 = 8453920;
                }

                this.font.drawShadow(matrixStack, s, (float)(upper + 86 - this.font.width(s)), (float)(heightMargin + 16 + 19 * index + 7), j2);
            }
        }

    }

    public void render(MatrixStack p_230430_1_, int p_230430_2_, int p_230430_3_, float p_230430_4_) {
        p_230430_4_ = this.minecraft.getFrameTime();
        this.renderBackground(p_230430_1_);
        super.render(p_230430_1_, p_230430_2_, p_230430_3_, p_230430_4_);
        this.renderTooltip(p_230430_1_, p_230430_2_, p_230430_3_);
        boolean flag = this.minecraft.player.abilities.instabuild;
        int i = this.menu.getGoldCount();

        for(int j = 0; j < 3; ++j) {
            int k = (this.menu).costs[j];
            Enchantment enchantment = Enchantment.byId((this.menu).enchantClue[j]);
            int l = (this.menu).levelClue[j];
            int i1 = j + 1;
            if (this.isHovering(60, 14 + 19 * j, 108, 17, (double)p_230430_2_, (double)p_230430_3_) && k > 0 && l >= 0 && enchantment != null) {
                List<ITextComponent> list = Lists.newArrayList();
                list.add((new TranslationTextComponent("container.enchant.clue", enchantment.getFullname(l))).withStyle(TextFormatting.WHITE));
                if (!flag) {
                    list.add(StringTextComponent.EMPTY);
                    if (this.minecraft.player.experienceLevel < k) {
                        list.add((new TranslationTextComponent("container.enchant.level.requirement", (this.menu).costs[j])).withStyle(TextFormatting.RED));
                    } else {
                        IFormattableTextComponent iformattabletextcomponent;
                        if (i1 == 1) {
                            iformattabletextcomponent = new TranslationTextComponent("container.enchant.lapis.one");
                        } else {
                            iformattabletextcomponent = new TranslationTextComponent("container.enchant.lapis.many", i1);
                        }

                        list.add(iformattabletextcomponent.withStyle(i >= i1 ? TextFormatting.GRAY : TextFormatting.RED));
                        IFormattableTextComponent iformattabletextcomponent1;
                        if (i1 == 1) {
                            iformattabletextcomponent1 = new TranslationTextComponent("container.enchant.level.one");
                        } else {
                            iformattabletextcomponent1 = new TranslationTextComponent("container.enchant.level.many", i1);
                        }

                        list.add(iformattabletextcomponent1.withStyle(TextFormatting.GRAY));
                    }
                }

                this.renderComponentTooltip(p_230430_1_, list, p_230430_2_, p_230430_3_);
                break;
            }
        }

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

        for(int i = 0; i < 3; ++i) {
            if ((this.menu).costs[i] != 0) {
                flag = true;
            }
        }

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
