package com.nuggylib.enchantmentsseer.client.gui.element.slot;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.gui.IGuiWrapper;
import com.nuggylib.enchantmentsseer.client.gui.element.GuiTexturedElement;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer;
import com.nuggylib.enchantmentsseer.client.render.text.EnumColor;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.slot.SlotOverlay;
import com.nuggylib.enchantmentsseer.client.jei.interfaces.IJEIGhostTarget;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;
import java.util.function.IntSupplier;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/gui/element/slot/GuiSlot.java"
 */
public class GuiSlot extends GuiTexturedElement implements IJEIGhostTarget {

    private static final int INVALID_SLOT_COLOR = EnchantmentsSeerRenderer.getColorARGB(EnumColor.DARK_RED, 0.8F);
    public static final int DEFAULT_HOVER_COLOR = 0x80FFFFFF;
    private final SlotType slotType;
    private Supplier<ItemStack> validityCheck;
    private Supplier<ItemStack> storedStackSupplier;
    private Supplier<SlotOverlay> overlaySupplier;
    @Nullable
    private BooleanSupplier warningSupplier;
    @Nullable
    private IntSupplier overlayColorSupplier;
    @Nullable
    private SlotOverlay overlay;
    @Nullable
    private IHoverable onHover;
    @Nullable
    private IClickable onClick;
    private boolean renderHover;
    private boolean renderAboveSlots;

    @Nullable
    private IGhostIngredientConsumer ghostHandler;

    public GuiSlot(SlotType type, IGuiWrapper gui, int x, int y) {
        super(type.getTexture(), gui, x, y, type.getWidth(), type.getHeight());
        EnchantmentsSeer.LOGGER.info(String.format("Using texture: %s", type.getTexture()));
        this.slotType = type;
        active = false;
    }

    public GuiSlot validity(Supplier<ItemStack> validityCheck) {
        //TODO - WARNING SYSTEM: Evaluate if any of these validity things should be moved to the warning system
        this.validityCheck = validityCheck;
        return this;
    }

    /**
     * @apiNote For use when there is no validity check and this is a "fake" slot in that the container screen doesn't render the item by default.
     */
    public GuiSlot stored(Supplier<ItemStack> storedStackSupplier) {
        this.storedStackSupplier = storedStackSupplier;
        return this;
    }

    public GuiSlot hover(IHoverable onHover) {
        this.onHover = onHover;
        return this;
    }

    public GuiSlot click(IClickable onClick) {
        this.onClick = onClick;
        return this;
    }

    public GuiSlot with(SlotOverlay overlay) {
        this.overlay = overlay;
        return this;
    }

    public GuiSlot overlayColor(IntSupplier colorSupplier) {
        overlayColorSupplier = colorSupplier;
        return this;
    }

    public GuiSlot with(Supplier<SlotOverlay> overlaySupplier) {
        this.overlaySupplier = overlaySupplier;
        return this;
    }

    public GuiSlot setRenderHover(boolean renderHover) {
        this.renderHover = renderHover;
        return this;
    }

    public GuiSlot setGhostHandler(@Nullable IGhostIngredientConsumer ghostHandler) {
        this.ghostHandler = ghostHandler;
        return this;
    }

    public GuiSlot setRenderAboveSlots() {
        this.renderAboveSlots = true;
        return this;
    }

    @Override
    public void renderButton(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        if (!renderAboveSlots) {
            draw(matrix);
        }
    }

    @Override
    public void drawBackground(@Nonnull MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
        if (renderAboveSlots) {
            draw(matrix);
        }
    }

    private void draw(@Nonnull MatrixStack matrix) {
        minecraft.textureManager.bind(getResource());
        blit(matrix, x, y, 0, 0, width, height, width, height);
        if (overlaySupplier != null) {
            overlay = overlaySupplier.get();
        }
        if (overlay != null) {
            minecraft.textureManager.bind(overlay.getTexture());
            blit(matrix, x, y, 0, 0, overlay.getWidth(), overlay.getHeight(), overlay.getWidth(), overlay.getHeight());
        }
        drawContents(matrix);
    }

    protected void drawContents(@Nonnull MatrixStack matrix) {
        if (validityCheck != null) {
            ItemStack invalid = validityCheck.get();
            if (!invalid.isEmpty()) {
                int xPos = x + 1;
                int yPos = y + 1;
                fill(matrix, xPos, yPos, xPos + 16, yPos + 16, INVALID_SLOT_COLOR);
                EnchantmentsSeerRenderer.resetColor();
                gui().renderItem(matrix, invalid, xPos, yPos);
            }
        } else if (storedStackSupplier != null) {
            ItemStack stored = storedStackSupplier.get();
            if (!stored.isEmpty()) {
                gui().renderItem(matrix, stored, x + 1, y + 1);
            }
        }
    }

    @Override
    public void renderForeground(MatrixStack matrix, int mouseX, int mouseY) {
        if (renderHover && isHovered()) {
            int xPos = relativeX + 1;
            int yPos = relativeY + 1;
            fill(matrix, xPos, yPos, xPos + 16, yPos + 16, DEFAULT_HOVER_COLOR);
            EnchantmentsSeerRenderer.resetColor();
        }
        if (overlayColorSupplier != null) {
            matrix.pushPose();
            matrix.translate(0, 0, 10);
            int xPos = relativeX + 1;
            int yPos = relativeY + 1;
            fill(matrix, xPos, yPos, xPos + 16, yPos + 16, overlayColorSupplier.getAsInt());
            matrix.popPose();
            EnchantmentsSeerRenderer.resetColor();
        }
        if (isHovered()) {
            //TODO: Should it pass it the proper mouseX and mouseY. Probably, though buttons may have to be redone slightly then
            renderToolTip(matrix, mouseX - getGuiLeft(), mouseY - getGuiTop());
        }
    }

    @Override
    public void renderToolTip(@Nonnull MatrixStack matrix, int mouseX, int mouseY) {
        if (onHover != null) {
            onHover.onHover(this, matrix, mouseX, mouseY);
        }
    }

    @Override
    public boolean mouseClicked(double mouseX, double mouseY, int button) {
        if (onClick != null && isValidClickButton(button)) {
            if (mouseX >= x + borderSize() && mouseY >= y + borderSize() && mouseX < x + width - borderSize() && mouseY < y + height - borderSize()) {
                onClick.onClick(this, (int) mouseX, (int) mouseY);
                playDownSound(Minecraft.getInstance().getSoundManager());
                return true;
            }
        }
        return false;
    }

    @Nullable
    @Override
    public IGhostIngredientConsumer getGhostHandler() {
        return ghostHandler;
    }

    @Override
    public int borderSize() {
        return 1;
    }
}
