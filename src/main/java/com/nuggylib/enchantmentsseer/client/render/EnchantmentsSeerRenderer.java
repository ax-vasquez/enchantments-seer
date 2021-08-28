package com.nuggylib.enchantmentsseer.client.render;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.client.render.text.EnumColor;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.AtlasTexture;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ModelBakeEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.Mod;
import org.lwjgl.opengl.GL11;

import javax.annotation.Nonnull;

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

}
