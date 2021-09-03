package com.nuggylib.enchantmentsseer.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import com.nuggylib.enchantmentsseer.common.base.ProfilerConstants;
import com.nuggylib.enchantmentsseer.common.tile.block.TileEntitySeersEnchantmentTable;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.model.BookModel;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.profiler.IProfiler;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector3f;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/render/tileentity/RenderNutritionalLiquifier.java"
 */
@ParametersAreNonnullByDefault
public class RenderSeersEnchantingTable extends EnchantmentsSeerTileEntityRenderer<TileEntitySeersEnchantmentTable> {
    public static final RenderMaterial BOOK_LOCATION = new RenderMaterial(AtlasTexture.LOCATION_BLOCKS, new ResourceLocation("enchantments-seer:textures/entity/enchanting_table_book"));
    private final BookModel bookModel = new BookModel();

    public RenderSeersEnchantingTable(TileEntityRendererDispatcher renderDispatcherIn) {
        super(renderDispatcherIn);
    }

    @Override
    public void render(TileEntitySeersEnchantmentTable tile, float partialTick, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight,
                       IProfiler profiler) {
        matrix.pushPose();
        matrix.translate(0.5D, 0.75D, 0.5D);
        float f = (float)tile.time + partialTick;
        matrix.translate(0.0D, (double)(0.1F + MathHelper.sin(f * 0.1F) * 0.01F), 0.0D);

        float f1;
        for(f1 = tile.rot - tile.oRot; f1 >= (float)Math.PI; f1 -= ((float)Math.PI * 2F)) {
        }

        while(f1 < -(float)Math.PI) {
            f1 += ((float)Math.PI * 2F);
        }

        float f2 = tile.oRot + f1 * partialTick;
        matrix.mulPose(Vector3f.YP.rotation(-f2));
        matrix.mulPose(Vector3f.ZP.rotationDegrees(80.0F));
        float f3 = MathHelper.lerp(partialTick, tile.oFlip, tile.flip);
        float f4 = MathHelper.frac(f3 + 0.25F) * 1.6F - 0.3F;
        float f5 = MathHelper.frac(f3 + 0.75F) * 1.6F - 0.3F;
        float f6 = MathHelper.lerp(partialTick, tile.oOpen, tile.open);
        this.bookModel.setupAnim(f, MathHelper.clamp(f4, 0.0F, 1.0F), MathHelper.clamp(f5, 0.0F, 1.0F), f6);
        IVertexBuilder ivertexbuilder = BOOK_LOCATION.buffer(renderer, RenderType::entitySolid);
        this.bookModel.render(matrix, ivertexbuilder, light, overlayLight, 1.0F, 1.0F, 1.0F, 1.0F);
        matrix.popPose();
    }
    @Override
    protected String getProfilerSection() {
        return ProfilerConstants.SEERS_ENCHANTING_TABLE;
    }


}
