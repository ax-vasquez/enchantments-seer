package com.nuggylib.enchantmentsseer.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.render.text.EnumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nullable;
import java.util.Arrays;

/**
 * Registers render-related stuff
 *
 * This class is loosely-inspired by the Mekanism class, `MekanismRenderer`.
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/render/MekanismRenderer.java"
 */
@Mod.EventBusSubscriber(modid = EnchantmentsSeer.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EnchantmentsSeerRenderer {

    // TODO: See if we actually need this (or if we currently don't, but it would be better if we did)
    @SubscribeEvent
    public static void onModelBake(ModelBakeEvent event) {

    }

    /**
     * Use this event to add sprites to the appropriate atlas texture.
     *
     * @param event
     */
    @SubscribeEvent
    public static void onStitch(TextureStitchEvent.Pre event) {
        // Prevent stitching of atlas sprites to anything other than AtlasTexture.LOCATION_BLOCKS (the same thing vanilla MC stitches sprites to for animated blocks)
        if (!event.getMap().location().equals(AtlasTexture.LOCATION_BLOCKS)) {
            return;
        }
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

    /**
     * Binds a texture to the Minecraft texture manager
     *
     * @param texture           The resource location of the texture to bind
     */
    public static void bindTexture(ResourceLocation texture) {
        Minecraft.getInstance().textureManager.bind(texture);
    }

    /**
     * Resets the color for the render system
     *
     * TODO: ...what does this mean, though? (aside from the obvious "resetting" of colors - we need to find out exactly how this works and why it's necessary)
     */
    public static void resetColor() {
        RenderSystem.color4f(1, 1, 1, 1);
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

    public static int getColorARGB(EnumColor color, float alpha) {
        return getColorARGB(color.getRgbCode()[0], color.getRgbCode()[1], color.getRgbCode()[2], alpha);
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
