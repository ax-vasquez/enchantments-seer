package com.nuggylib.enchantmentsseer.client.jei.interfaces;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;

import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/client/jei/interfaces/IJEIGhostTarget.java"
 */
public interface IJEIGhostTarget {

    /**
     * @return {@code null} if it doesn't actually currently support ghost handling
     */
    @Nullable
    IGhostIngredientConsumer getGhostHandler();

    /**
     * Number of pixels on each side that make up the border, and should be ignored when creating the target area.
     */
    default int borderSize() {
        return 0;
    }

    interface IGhostIngredientConsumer extends Consumer<Object> {

        boolean supportsIngredient(Object ingredient);
    }

    interface IGhostItemConsumer extends IGhostIngredientConsumer {

        @Override
        default boolean supportsIngredient(Object ingredient) {
            return ingredient instanceof ItemStack && !((ItemStack) ingredient).isEmpty();
        }
    }

    interface IGhostBlockItemConsumer extends IGhostItemConsumer {

        @Override
        default boolean supportsIngredient(Object ingredient) {
            //Only allow block items
            return IGhostItemConsumer.super.supportsIngredient(ingredient) && ((ItemStack) ingredient).getItem() instanceof BlockItem;
        }
    }
}
