package com.nuggylib.enchantmentsseer.client;

import com.nuggylib.enchantmentsseer.client.util.TextComponentUtil;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeerLang;
import com.nuggylib.enchantmentsseer.common.lib.collection.IIncrementalEnum;
import com.nuggylib.enchantmentsseer.common.util.MathUtils;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.item.DyeColor;
import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.Tags;

import javax.annotation.Nonnull;

public enum EnumColor implements IIncrementalEnum<EnumColor> {
    BLACK("\u00a70", EnchantmentsSeerLang.COLOR_BLACK, "Black", "black", new int[]{64, 64, 64}, DyeColor.BLACK),
    DARK_BLUE("\u00a71", EnchantmentsSeerLang.COLOR_DARK_BLUE, "Blue", "blue", new int[]{54, 107, 208}, DyeColor.BLUE),
    DARK_GREEN("\u00a72", EnchantmentsSeerLang.COLOR_DARK_GREEN, "Green", "green", new int[]{89, 193, 95}, DyeColor.GREEN),
    DARK_AQUA("\u00a73", EnchantmentsSeerLang.COLOR_DARK_AQUA, "Cyan", "cyan", new int[]{0, 243, 208}, DyeColor.CYAN),
    DARK_RED("\u00a74", EnchantmentsSeerLang.COLOR_DARK_RED, "Dark Red", "dark_red", new int[]{201, 7, 31}, MaterialColor.NETHER, Tags.Items.DYES_RED, false),
    PURPLE("\u00a75", EnchantmentsSeerLang.COLOR_PURPLE, "Purple", "purple", new int[]{164, 96, 217}, DyeColor.PURPLE),
    ORANGE("\u00a76", EnchantmentsSeerLang.COLOR_ORANGE, "Orange", "orange", new int[]{255, 161, 96}, DyeColor.ORANGE),
    GRAY("\u00a77", EnchantmentsSeerLang.COLOR_GRAY, "Light Gray", "light_gray", new int[]{207, 207, 207}, DyeColor.LIGHT_GRAY),
    DARK_GRAY("\u00a78", EnchantmentsSeerLang.COLOR_DARK_GRAY, "Gray", "gray", new int[]{122, 122, 122}, DyeColor.GRAY),
    INDIGO("\u00a79", EnchantmentsSeerLang.COLOR_INDIGO, "Light Blue", "light_blue", new int[]{85, 158, 255}, DyeColor.LIGHT_BLUE),
    BRIGHT_GREEN("\u00a7a", EnchantmentsSeerLang.COLOR_BRIGHT_GREEN, "Lime", "lime", new int[]{117, 255, 137}, DyeColor.LIME),
    AQUA("\u00a7b", EnchantmentsSeerLang.COLOR_AQUA, "Aqua", "aqua", new int[]{48, 255, 249}, MaterialColor.COLOR_LIGHT_BLUE, Tags.Items.DYES_LIGHT_BLUE, false),
    RED("\u00a7c", EnchantmentsSeerLang.COLOR_RED, "Red", "red", new int[]{255, 56, 60}, DyeColor.RED),
    PINK("\u00a7d", EnchantmentsSeerLang.COLOR_PINK, "Magenta", "magenta", new int[]{213, 94, 203}, DyeColor.MAGENTA),
    YELLOW("\u00a7e", EnchantmentsSeerLang.COLOR_YELLOW, "Yellow", "yellow", new int[]{255, 221, 79}, DyeColor.YELLOW),
    WHITE("\u00a7f", EnchantmentsSeerLang.COLOR_WHITE, "White", "white", new int[]{255, 255, 255}, DyeColor.WHITE),
    //Extras for dye-completeness
    BROWN("\u00a76", EnchantmentsSeerLang.COLOR_BROWN, "Brown", "brown", new int[]{161, 118, 73}, DyeColor.BROWN),
    BRIGHT_PINK("\u00a7d", EnchantmentsSeerLang.COLOR_BRIGHT_PINK, "Pink", "pink", new int[]{255, 188, 196}, DyeColor.PINK);

    private static final EnumColor[] COLORS = values();
    /**
     * The color code that will be displayed
     */
    public final String code;

    private int[] rgbCode;
    private Color color;
    private final EnchantmentsSeerLang langEntry;
    private final String englishName;
    private final String registryPrefix;
    //TODO - 1.17: Potentially make getDyeTag nullable, and just use that for seeing if we have a corresponding dye
    // Alternatively maybe we want to just have it keep track of a nullable dye color and we can get the tag from there
    private final boolean hasCorrespondingDye;
    private final MaterialColor mapColor;
    private final ITag<Item> dyeTag;

    EnumColor(String s, EnchantmentsSeerLang langEntry, String englishName, String registryPrefix, int[] rgbCode, DyeColor dyeColor) {
        this(s, langEntry, englishName, registryPrefix, rgbCode, dyeColor.getMaterialColor(), dyeColor.getTag(), true);
    }

    EnumColor(String code, EnchantmentsSeerLang langEntry, String englishName, String registryPrefix, int[] rgbCode, MaterialColor mapColor, ITag<Item> dyeTag,
              boolean hasCorrespondingDye) {
        this.code = code;
        this.langEntry = langEntry;
        this.englishName = englishName;
        this.hasCorrespondingDye = hasCorrespondingDye;
        this.registryPrefix = registryPrefix;
        setColorFromAtlas(rgbCode);
        this.mapColor = mapColor;
        this.dyeTag = dyeTag;
    }

    /**
     * Gets the prefix to use in registry names for this color.
     */
    public String getRegistryPrefix() {
        return registryPrefix;
    }

    /**
     * Gets the English name of this color.
     */
    public String getEnglishName() {
        return englishName;
    }

    /**
     * Gets the material or map color that most closely corresponds to this color.
     */
    public MaterialColor getMapColor() {
        return mapColor;
    }

    /**
     * Gets the item tag that corresponds to the dye this color corresponds to.
     */
    public ITag<Item> getDyeTag() {
        //TODO - 1.17: Make this nullable and replace hasDyeName with just null checking this
        return dyeTag;
    }

    @Deprecated
    public boolean hasDyeName() {
        return hasCorrespondingDye;
    }

    /**
     * Gets the name of this color with it's color prefix code.
     *
     * @return the color's name and color prefix
     */
    public ITextComponent getColoredName() {
        return TextComponentUtil.build(this, getName());
    }

    /**
     * Gets the name of this color without coloring the returned result
     *
     * @return the color's name
     */
    public IFormattableTextComponent getName() {
        return langEntry.translate();
    }

    /**
     * @apiNote For use by the data generators.
     */
    public EnchantmentsSeerLang getLangEntry() {
        return langEntry;
    }

    /**
     * Gets the 0-1 of this color's RGB value by dividing by 255 (used for OpenGL coloring).
     *
     * @param index - R:0, G:1, B:2
     *
     * @return the color value
     */
    public float getColor(int index) {
        return rgbCode[index] / 255F;
    }

    /**
     * Gets the corresponding text color for this color.
     */
    public Color getColor() {
        return color;
    }

    @Override
    public String toString() {
        return code;
    }

    /**
     * Gets a color by index.
     *
     * @param index Index of the color.
     */
    public static EnumColor byIndexStatic(int index) {
        return MathUtils.getByIndexMod(COLORS, index);
    }

    @Nonnull
    @Override
    public EnumColor byIndex(int index) {
        return byIndexStatic(index);
    }

    /**
     * Sets the internal color representation of this color from the color atlas.
     *
     * @param color Color data.
     *
     * @apiNote This method is mostly for <strong>INTERNAL</strong> usage.
     */
    public void setColorFromAtlas(int[] color) {
        rgbCode = color;
        this.color = Color.fromRgb(rgbCode[0] << 16 | rgbCode[1] << 8 | rgbCode[2]);
    }

    /**
     * Gets the red, green and blue color value, as an integer(range: 0 - 255).
     *
     * @return the color values.
     *
     * @apiNote Modifying the returned array will result in this color object changing the color it represents, and should not be done.
     */
    public int[] getRgbCode() {
        return rgbCode;
    }

    /**
     * Gets the red, green and blue color value, as a float(range: 0 - 1).
     *
     * @return the color values.
     */
    public float[] getRgbCodeFloat() {
        return new float[]{getColor(0), getColor(1), getColor(2)};
    }
}
