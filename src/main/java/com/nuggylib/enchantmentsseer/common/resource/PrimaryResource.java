package com.nuggylib.enchantmentsseer.common.resource;

import net.minecraft.item.Item;
import net.minecraft.tags.ITag;
import net.minecraftforge.common.Tags;

import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/resource/PrimaryResource.java"
 */
public enum PrimaryResource implements IResource {
    IRON("iron", 0xFFAF8E77, Tags.Items.ORES_IRON),
    GOLD("gold", 0xFFF2CD67, Tags.Items.ORES_GOLD);

    private final String name;
    private final int tint;
    private final Supplier<ITag<Item>> oreTag;
    private final boolean isVanilla;
    private final BlockResourceInfo resourceBlockInfo;

    PrimaryResource(String name, int tint, ITag<Item> oreTag) {
        this(name, tint, () -> oreTag, true, null);
    }

    PrimaryResource(String name, int tint, Supplier<ITag<Item>> oreTag, BlockResourceInfo resourceBlockInfo) {
        this(name, tint, oreTag, false, resourceBlockInfo);
    }

    PrimaryResource(String name, int tint, Supplier<ITag<Item>> oreTag, boolean isVanilla, BlockResourceInfo resourceBlockInfo) {
        this.name = name;
        this.tint = tint;
        this.oreTag = oreTag;
        this.isVanilla = isVanilla;
        this.resourceBlockInfo = resourceBlockInfo;
    }

    //TODO: remove
    public String getName() {
        return name;
    }

    @Override
    public String getRegistrySuffix() {
        return name;
    }

    public int getTint() {
        return tint;
    }

    public ITag<Item> getOreTag() {
        return oreTag.get();
    }

    public boolean has(ResourceType type) {
        return (!isVanilla || type != ResourceType.INGOT && type != ResourceType.NUGGET);
    }

    public boolean isVanilla() {
        return isVanilla;
    }

    public BlockResourceInfo getResourceBlockInfo() {
        return resourceBlockInfo;
    }
}
