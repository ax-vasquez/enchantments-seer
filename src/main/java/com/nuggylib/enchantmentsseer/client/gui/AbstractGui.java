package com.nuggylib.enchantmentsseer.client.gui;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.util.ResourceType;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

/**
 * Base GUI class
 *
 * Create subclasses that extend this class for any new GUI you need to create. This includes:
 * 1. Interactive block GUIs (e.g., furnace GUI)
 * 2. Achievement popups
 * 3. Warning/Error popups
 * 4. Anything else that renders as a "modal" type view in-game
 *
 * Method parameter names were inferred by cross-referencing the `EnchantmentScreen` class (from vanilla Minecraft)
 * and the Mekanism code, specifically `GuiMekanism`
 *
 * You may notice that this class doesn't really do much other than override super class methods, and you'd be right -
 * that's literally all this class does. <b>This class is a convenience class, intended to make it easier to use
 * the `ContainerScreen` class by giving the overridden method parameter names something meaningful.</b>
 *
 * @param <CONTAINER>           The corresponding `Container` class for the GUI
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/GuiMekanism.java"
 */
@ParametersAreNonnullByDefault
public abstract class AbstractGui<CONTAINER extends Container> extends ContainerScreen<CONTAINER> {
    public static final ResourceLocation BASE_BACKGROUND = EnchantmentsSeer.getResource(ResourceType.GUI, "base.png");
    public static final ResourceLocation SHADOW = EnchantmentsSeer.getResource(ResourceType.GUI, "shadow.png");
    public static final ResourceLocation BLUR = EnchantmentsSeer.getResource(ResourceType.GUI, "blur.png");

    public AbstractGui(CONTAINER container, PlayerInventory inventory, ITextComponent title) {
        super(container, inventory, title);
    }

    @Override
    protected void renderBg(MatrixStack matrix, float partialTick, int mouseX, int mouseY) {

    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    @Override
    public void render(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {

    }
}
