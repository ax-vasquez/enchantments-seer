package com.nuggylib.enchantmentsseer.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.render.lib.ColorAtlas;
import com.nuggylib.enchantmentsseer.common.lib.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.fluid.Fluid;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.client.model.obj.OBJModel;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * The parent renderer class
 *
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/render/MekanismRenderer.java"
 */
@Mod.EventBusSubscriber(modid = EnchantmentsSeer.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentsSeerRenderer {

    //TODO: Replace various usages of this with the getter for calculating glow light, at least if we end up making it only
    // effect block light for the glow rather than having it actually become full light
    public static final int FULL_LIGHT = 0xF000F0;
    public static final int FULL_SKY_LIGHT = LightTexture.pack(0, 15);

    public static OBJModel contentsModel;
    public static IBakedModel liquifierBlade;
    public static TextureAtlasSprite energyIcon;
    public static TextureAtlasSprite heatIcon;
    public static TextureAtlasSprite whiteIcon;
    public static TextureAtlasSprite teleporterPortal;
    public static TextureAtlasSprite redstoneTorch;
    public static TextureAtlasSprite redstonePulse;

    /**
     * Get a fluid texture when a stack does not exist.
     *
     * @param fluid the fluid to get
     * @param type  Still or Flowing
     *
     * @return the sprite, or missing sprite if not found
     */
    public static TextureAtlasSprite getBaseFluidTexture(@Nonnull Fluid fluid, @Nonnull FluidType type) {
        ResourceLocation spriteLocation;
        if (type == FluidType.STILL) {
            spriteLocation = fluid.getAttributes().getStillTexture();
        } else {
            spriteLocation = fluid.getAttributes().getFlowingTexture();
        }

        return getSprite(spriteLocation);
    }

    public static TextureAtlasSprite getSprite(ResourceLocation spriteLocation) {
        return Minecraft.getInstance().getTextureAtlas(AtlasTexture.LOCATION_BLOCKS).apply(spriteLocation);
    }

    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getInstance().textureManager.bind(texture);
    }

    //Color
    public static void resetColor() {
        RenderSystem.color4f(1, 1, 1, 1);
    }

    public static float getRed(int color) {
        return (color >> 16 & 0xFF) / 255.0F;
    }

    public static float getGreen(int color) {
        return (color >> 8 & 0xFF) / 255.0F;
    }

    public static float getBlue(int color) {
        return (color & 0xFF) / 255.0F;
    }

    public static float getAlpha(int color) {
        return (color >> 24 & 0xFF) / 255.0F;
    }

    public static void color(int color) {
        RenderSystem.color4f(getRed(color), getGreen(color), getBlue(color), getAlpha(color));
    }

    public static void color(ColorAtlas.ColorRegistryObject colorRO) {
        color(colorRO.get());
    }

    public static void color(Color color) {
        RenderSystem.color4f(color.rf(), color.gf(), color.bf(), color.af());
    }

    public static void color(@Nonnull FluidStack fluid) {
        if (!fluid.isEmpty()) {
            color(fluid.getFluid().getAttributes().getColor(fluid));
        }
    }

    public static void color(@Nullable EnumColor color) {
        color(color, 1.0F);
    }

    public static void color(@Nullable EnumColor color, float alpha) {
        color(color, alpha, 1.0F);
    }

    public static void color(@Nullable EnumColor color, float alpha, float multiplier) {
        if (color != null) {
            RenderSystem.color4f(color.getColor(0) * multiplier, color.getColor(1) * multiplier, color.getColor(2) * multiplier, alpha);
        }
    }

    public static int getColorARGB(EnumColor color, float alpha) {
        return getColorARGB(color.getRgbCode()[0], color.getRgbCode()[1], color.getRgbCode()[2], alpha);
    }

    public static int getColorARGB(@Nonnull FluidStack fluidStack) {
        return fluidStack.getFluid().getAttributes().getColor(fluidStack);
    }

    public static int getColorARGB(float red, float green, float blue, float alpha) {
        return getColorARGB((int) (255 * red), (int) (255 * green), (int) (255 * blue), alpha);
    }

    public static int getColorARGB(int red, int green, int blue, float alpha) {
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 1) {
            alpha = 1;
        }
        int argb = (int) (255 * alpha) << 24;
        argb |= red << 16;
        argb |= green << 8;
        argb |= blue;
        return argb;
    }

    public static int calculateGlowLight(int combinedLight, @Nonnull FluidStack fluid) {
        return fluid.isEmpty() ? combinedLight : calculateGlowLight(combinedLight, fluid.getFluid().getAttributes().getLuminosity(fluid));
    }

    public static int calculateGlowLight(int combinedLight, int glow) {
        //Only factor the glow into the block light portion
        return (combinedLight & 0xFFFF0000) | Math.max(Math.min(glow, 15) << 4, combinedLight & 0xFFFF);
    }

    public static void renderColorOverlay(MatrixStack matrix, int x, int y, int width, int height, int color) {
        float r = getRed(color);
        float g = getGreen(color);
        float b = getBlue(color);
        float a = getAlpha(color);
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        RenderSystem.disableAlphaTest();
        RenderSystem.defaultBlendFunc();
        RenderSystem.shadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferbuilder = tessellator.getBuilder();
        bufferbuilder.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        Matrix4f matrix4f = matrix.last().pose();
        bufferbuilder.vertex(matrix4f, width, y, 0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, x, y, 0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, x, height, 0).color(r, g, b, a).endVertex();
        bufferbuilder.vertex(matrix4f, width, height, 0).color(r, g, b, a).endVertex();
        tessellator.end();
        RenderSystem.shadeModel(GL11.GL_FLAT);
        RenderSystem.disableBlend();
        RenderSystem.enableAlphaTest();
        RenderSystem.enableTexture();
    }

    public static float getPartialTick() {
        return Minecraft.getInstance().getFrameTime();
    }

    public static void rotate(MatrixStack matrix, Direction facing, float north, float south, float west, float east) {
        switch (facing) {
            case NORTH:
                matrix.mulPose(Vector3f.YP.rotationDegrees(north));
                break;
            case SOUTH:
                matrix.mulPose(Vector3f.YP.rotationDegrees(south));
                break;
            case WEST:
                matrix.mulPose(Vector3f.YP.rotationDegrees(west));
                break;
            case EAST:
                matrix.mulPose(Vector3f.YP.rotationDegrees(east));
                break;
        }
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        if (!event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
            return;
        }

        // TODO: Stitch our animated textures and stuff
        // event.addSprite(Mekanism.rl("block/overlay/overlay_white"));
    }

    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Post event) {
        AtlasTexture map = event.getMap();
        if (!map.location().equals(AtlasTexture.LOCATION_BLOCKS)) {
            return;
        }

        redstoneTorch = map.getSprite(new ResourceLocation("minecraft:block/redstone_torch"));

    }

    public enum FluidType {
        STILL,
        FLOWING
    }

    // TODO: Find a way to use this for our enchanting table (will likely take some rework)
    public static class Model3D {

        public float minX, minY, minZ;
        public float maxX, maxY, maxZ;

        private final SpriteInfo[] textures = new SpriteInfo[6];
        private final boolean[] renderSides = new boolean[]{true, true, true, true, true, true};

        public void setSideRender(Direction side, boolean value) {
            renderSides[side.ordinal()] = value;
        }

        public Model3D copy() {
            Model3D copy = new Model3D();
            System.arraycopy(textures, 0, copy.textures, 0, textures.length);
            System.arraycopy(renderSides, 0, copy.renderSides, 0, renderSides.length);
            copy.minX = minX;
            copy.minY = minY;
            copy.minZ = minZ;
            copy.maxX = maxX;
            copy.maxY = maxY;
            copy.maxZ = maxZ;
            return copy;
        }

        @Nullable
        public SpriteInfo getSpriteToRender(Direction side) {
            int ordinal = side.ordinal();
            if (renderSides[ordinal]) {
                return textures[ordinal];
            }
            return null;
        }

        public void setTexture(Direction side, SpriteInfo spriteInfo) {
            textures[side.ordinal()] = spriteInfo;
        }

        public void setTexture(TextureAtlasSprite tex) {
            setTexture(tex, 16);
        }

        public void setTexture(TextureAtlasSprite tex, int size) {
            Arrays.fill(textures, new SpriteInfo(tex, size));
        }

        public void setTextures(SpriteInfo down, SpriteInfo up, SpriteInfo north, SpriteInfo south, SpriteInfo west, SpriteInfo east) {
            textures[0] = down;
            textures[1] = up;
            textures[2] = north;
            textures[3] = south;
            textures[4] = west;
            textures[5] = east;
        }

        public static final class SpriteInfo {

            public final TextureAtlasSprite sprite;
            public final int size;

            public SpriteInfo(TextureAtlasSprite sprite, int size) {
                this.sprite = sprite;
                this.size = size;
            }
        }
    }
}
