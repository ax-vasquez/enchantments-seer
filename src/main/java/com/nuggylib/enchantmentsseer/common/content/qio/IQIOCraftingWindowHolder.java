package com.nuggylib.enchantmentsseer.common.content.qio;

import com.nuggylib.enchantmentsseer.api.IContentsListener;
import net.minecraft.world.World;

import javax.annotation.Nullable;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/content/qio/IQIOCraftingWindowHolder.java"
 */
public interface IQIOCraftingWindowHolder extends IContentsListener {

    byte MAX_CRAFTING_WINDOWS = 3;

    @Nullable
    World getHolderWorld();

    QIOCraftingWindow[] getCraftingWindows();

    /**
     * @apiNote Only should be used on the server, so it is perfectly safe to always just be returning null when on the client.
     */
    @Nullable
    QIOFrequency getFrequency();
}
