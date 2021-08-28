package com.nuggylib.enchantmentsseer.client.render.text;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.nuggylib.enchantmentsseer.client.render.EnchantmentsSeerRenderer;
import com.nuggylib.enchantmentsseer.client.render.SpecialColors;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.util.text.ITextComponent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

public interface IFancyFontRenderer {

    int getXSize();

    FontRenderer getFont();

    default int titleTextColor() {
        return SpecialColors.TEXT_TITLE.argb();
    }

    default int headingTextColor() {
        return SpecialColors.TEXT_HEADING.argb();
    }

    default int subheadingTextColor() {
        return SpecialColors.TEXT_SUBHEADING.argb();
    }

    default int screenTextColor() {
        return SpecialColors.TEXT_SCREEN.argb();
    }

    default int drawString(MatrixStack matrix, ITextComponent component, int x, int y, int color) {
        return getFont().draw(matrix, component, x, y, color);
    }

    default int getStringWidth(ITextComponent component) {
        return getFont().width(component);
    }

    default void drawCenteredText(MatrixStack matrix, ITextComponent component, float x, float y, int color) {
        drawCenteredText(matrix, component, x, 0, y, color);
    }

    default void drawCenteredText(MatrixStack matrix, ITextComponent component, float xStart, float areaWidth, float y, int color) {
        int textWidth = getStringWidth(component);
        float centerX = xStart + (areaWidth / 2F) - (textWidth / 2F);
        drawTextExact(matrix, component, centerX, y, color);
    }

    default void drawTitleText(MatrixStack matrix, ITextComponent text, float y) {
        drawCenteredTextScaledBound(matrix, text, getXSize() - 8, y, titleTextColor());
    }

    default void drawScaledCenteredText(MatrixStack matrix, ITextComponent text, float left, float y, int color, float scale) {
        int textWidth = getStringWidth(text);
        float centerX = left - (textWidth / 2F) * scale;
        drawTextWithScale(matrix, text, centerX, y, color, scale);
    }

    default void drawCenteredTextScaledBound(MatrixStack matrix, ITextComponent text, float maxLength, float y, int color) {
        drawCenteredTextScaledBound(matrix, text, maxLength, 0, y, color);
    }

    default void drawCenteredTextScaledBound(MatrixStack matrix, ITextComponent text, float maxLength, float x, float y, int color) {
        float scale = Math.min(1, maxLength / getStringWidth(text));
        drawScaledCenteredText(matrix, text, x + getXSize() / 2F, y, color, scale);
    }

    default void drawTextExact(MatrixStack matrix, ITextComponent text, float x, float y, int color) {
        matrix.pushPose();
        matrix.translate(x, y, 0);
        drawString(matrix, text, 0, 0, color);
        matrix.popPose();
    }

    default float getNeededScale(ITextComponent text, float maxLength) {
        int length = getStringWidth(text);
        return length <= maxLength ? 1 : maxLength / length;
    }

    default void drawTextScaledBound(MatrixStack matrix, String text, float x, float y, int color, float maxLength) {
        drawTextScaledBound(matrix, TextComponentUtil.getString(text), x, y, color, maxLength);
    }

    default void drawTextScaledBound(MatrixStack matrix, ITextComponent component, float x, float y, int color, float maxLength) {
        int length = getStringWidth(component);

        if (length <= maxLength) {
            drawTextExact(matrix, component, x, y, color);
        } else {
            drawTextWithScale(matrix, component, x, y, color, maxLength / length);
        }
        //Make sure the color does not leak from having drawn the string
        EnchantmentsSeerRenderer.resetColor();
    }

    default void drawScaledTextScaledBound(MatrixStack matrix, ITextComponent text, float x, float y, int color, float maxX, float textScale) {
        float width = getStringWidth(text) * textScale;
        float scale = Math.min(1, maxX / width) * textScale;
        drawTextWithScale(matrix, text, x, y, color, scale);
    }

    default void drawTextWithScale(MatrixStack matrix, ITextComponent text, float x, float y, int color, float scale) {
        prepTextScale(matrix, m -> drawString(m, text, 0, 0, color), x, y, scale);
    }

    default void prepTextScale(MatrixStack matrix, Consumer<MatrixStack> runnable, float x, float y, float scale) {
        float yAdd = 4 - (scale * 8) / 2F;
        matrix.pushPose();
        matrix.translate(x, y + yAdd, 0);
        matrix.scale(scale, scale, scale);
        runnable.accept(matrix);
        matrix.popPose();
        EnchantmentsSeerRenderer.resetColor();
    }

    default int drawWrappedTextWithScale(MatrixStack matrix, ITextComponent text, float x, float y, int color, float maxLength, float scale) {
        return new WrappedTextRenderer(this).renderWithScale(matrix, text.getString(), x, y, color, maxLength, scale);
    }

    default void drawWrappedCenteredText(MatrixStack matrix, ITextComponent text, float x, float y, int color, float maxLength) {
        new WrappedTextRenderer(this).renderCentered(matrix, text.getString(), x, y, color, maxLength);
    }

    // efficient tool to draw word-by-word wrapped text based on a horizontal bound. looks intimidating but runs in O(n)
    class WrappedTextRenderer {

        private final IFancyFontRenderer font;
        private final List<Pair<ITextComponent, Float>> linesToDraw = new ArrayList<>();
        private StringBuilder lineBuilder = new StringBuilder(), wordBuilder = new StringBuilder();
        private float lineLength = 0, wordLength = 0;
        private final float SPACE_LENGTH;

        WrappedTextRenderer(IFancyFontRenderer font) {
            this.font = font;
            SPACE_LENGTH = font.getFont().width(" ");
        }

        void renderCentered(MatrixStack matrix, String text, float x, float y, int color, float maxLength) {
            calculateLines(text, maxLength);
            float startY = y;
            for (Pair<ITextComponent, Float> p : linesToDraw) {
                font.drawTextExact(matrix, p.getLeft(), x - p.getRight() / 2, startY, color);
                startY += 9;
            }
        }

        int renderWithScale(MatrixStack matrix, String text, float x, float y, int color, float maxLength, float scale) {
            //Divide by scale for calculating actual max length so that when the text is scaled it has the proper total space available
            calculateLines(text, maxLength / scale);
            font.prepTextScale(matrix, m -> {
                int startY = 0;
                for (Pair<ITextComponent, Float> p : linesToDraw) {
                    font.drawString(m, p.getLeft(), 0, startY, color);
                    startY += 9;
                }
            }, x, y, scale);
            return linesToDraw.size();
        }

        void calculateLines(String text, float maxLength) {
            for (char c : text.toCharArray()) {
                if (c == ' ') {
                    addWord(maxLength);
                    continue;
                }
                wordBuilder.append(c);
                wordLength += font.getFont().width(Character.toString(c));
            }
            if (wordBuilder.length() > 0) {
                addWord(maxLength);
            }
            if (lineBuilder.length() > 0) {
                linesToDraw.add(Pair.of(TextComponentUtil.getString(lineBuilder.toString()), lineLength));
            }
        }

        void addWord(float maxLength) {
            // ignore spacing if this is the first word of the line
            float spacingLength = lineBuilder.length() == 0 ? 0 : SPACE_LENGTH;
            if (lineLength + spacingLength + wordLength > maxLength) {
                linesToDraw.add(Pair.of(TextComponentUtil.getString(lineBuilder.toString()), lineLength));
                lineBuilder = new StringBuilder(wordBuilder);
                lineLength = wordLength;
            } else {
                if (spacingLength > 0) {
                    lineBuilder.append(" ");
                }
                lineBuilder.append(wordBuilder);
                lineLength += spacingLength + wordLength;
            }
            wordLength = 0;
            wordBuilder = new StringBuilder();
        }
    }
}
