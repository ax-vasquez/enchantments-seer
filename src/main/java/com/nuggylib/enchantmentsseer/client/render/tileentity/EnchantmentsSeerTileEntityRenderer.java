package com.nuggylib.enchantmentsseer.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer.Model3D;
import com.nuggylib.enchantmentsseer.client.render.ModelRenderer;
import com.nuggylib.enchantmentsseer.client.render.RenderResizableCuboid.FaceDisplay;
import com.nuggylib.enchantmentsseer.client.render.data.RenderData;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.profiler.IProfiler;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.vector.Vector3d;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.function.BooleanSupplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <TILE>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/render/tileentity/MekanismTileEntityRenderer.java"
 */
@ParametersAreNonnullByDefault
public abstract class EnchantmentsSeerTileEntityRenderer<TILE extends TileEntity> extends TileEntityRenderer<TILE> {

    protected EnchantmentsSeerTileEntityRenderer(TileEntityRendererDispatcher renderer) {
        super(renderer);
    }

    @Override
    public void render(TILE tile, float partialTick, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight) {
        if (tile.getLevel() != null) {
            IProfiler profiler = tile.getLevel().getProfiler();
            profiler.push(getProfilerSection());
            render(tile, partialTick, matrix, renderer, light, overlayLight, profiler);
            profiler.pop();
        }
    }

    protected abstract void render(TILE tile, float partialTick, MatrixStack matrix, IRenderTypeBuffer renderer, int light, int overlayLight, IProfiler profiler);

    protected abstract String getProfilerSection();

    protected boolean isInsideBounds(double minX, double minY, double minZ, double maxX, double maxY, double maxZ) {
        Vector3d projectedView = renderer.camera.getPosition();
        return minX <= projectedView.x && projectedView.x <= maxX &&
                minY <= projectedView.y && projectedView.y <= maxY &&
                minZ <= projectedView.z && projectedView.z <= maxZ;
    }

    protected FaceDisplay getFaceDisplay(RenderData data, Model3D model) {
        return isInsideBounds(data.location.getX(), data.location.getY(), data.location.getZ(),
                data.location.getX() + data.length, data.location.getY() + ModelRenderer.getActualHeight(model), data.location.getZ() + data.width)
                ? FaceDisplay.BACK : FaceDisplay.FRONT;
    }

    protected BooleanSupplier isInsideMultiblock(RenderData data) {
        //Use the full multiblock's render data unlike getFaceDisplay which gets the current height
        return () -> isInsideBounds(data.location.getX(), data.location.getY(), data.location.getZ(),
                data.location.getX() + data.length, data.location.getY() + data.height, data.location.getZ() + data.width);
    }
}
