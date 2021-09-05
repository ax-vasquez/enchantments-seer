package com.nuggylib.enchantmentsseer.api.recipes.inputs;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.api.annotations.NonNull;
import com.nuggylib.enchantmentsseer.api.inventory.IInventorySlot;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Objects;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/recipes/inputs/InputHelper.java"
 */
@ParametersAreNonnullByDefault
public class InputHelper {

    private InputHelper() {
    }

    /**
     * Wrap an inventory slot into an {@link IInputHandler}.
     *
     * @param slot Slot to wrap.
     */
    public static IInputHandler<@NonNull ItemStack> getInputHandler(IInventorySlot slot) {
        Objects.requireNonNull(slot, "Slot cannot be null.");
        return new IInputHandler<@NonNull ItemStack>() {

            @Nonnull
            @Override
            public ItemStack getInput() {
                return slot.getStack();
            }

            @Nonnull
            @Override
            public ItemStack getRecipeInput(InputIngredient<@NonNull ItemStack> recipeIngredient) {
                ItemStack input = getInput();
                if (input.isEmpty()) {
                    //All recipes currently require that we have an input. If we don't then return that we failed
                    return ItemStack.EMPTY;
                }
                return recipeIngredient.getMatchingInstance(input);
            }

            @Override
            public void use(@Nonnull ItemStack recipeInput, int operations) {
                if (operations == 0) {
                    //Just exit if we are somehow here at zero operations
                    return;
                }
                if (!recipeInput.isEmpty()) {
                    int amount = recipeInput.getCount() * operations;
                    logMismatchedStackSize(slot.shrinkStack(amount, Action.EXECUTE), amount);
                }
            }

            @Override
            public int operationsCanSupport(@Nonnull ItemStack recipeInput, int currentMax, int usageMultiplier) {
                if (currentMax <= 0 || usageMultiplier <= 0) {
                    //Short circuit that if we already can't perform any operations or don't want to use any, just return
                    return currentMax;
                }
                if (recipeInput.isEmpty()) {
                    //If the input is empty that means there is no ingredient that matches
                    return 0;
                }
                //TODO: Simulate?
                return Math.min(getInput().getCount() / (recipeInput.getCount() * usageMultiplier), currentMax);
            }
        };
    }

    private static void logMismatchedStackSize(long actual, long expected) {
        if (expected != actual) {
            EnchantmentsSeer.logger.error("Stack size changed by a different amount ({}) than requested ({}).", actual, expected, new Exception());
        }
    }
}
