package com.nuggylib.enchantmentsseer.api.recipes.inputs;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * Interface describing handling of an input.
 *
 * @param <INPUT> Type of input handled by this handler.
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/recipes/inputs/IInputHandler.java"
 */
public interface IInputHandler<INPUT> {

    /**
     * Returns the currently stored input.
     *
     * <p>
     * <strong>IMPORTANT:</strong> This input <em>MUST NOT</em> be modified. This method is not for altering an input's contents. Any implementers who
     * are able to detect modification through this method should throw an exception.
     * </p>
     * <p>
     * <strong><em>SERIOUSLY: DO NOT MODIFY THE RETURNED INPUT</em></strong>
     * </p>
     *
     * @return Input stored.
     *
     * @apiNote <strong>IMPORTANT:</strong> Do not modify this value.
     */
    INPUT getInput();

    /**
     * Gets a copy of the recipe's ingredient that matches the stored input.
     *
     * @param recipeIngredient Recipe ingredient.
     *
     * @return Matching instance. The returned value can be safely modified after.
     */
    INPUT getRecipeInput(InputIngredient<INPUT> recipeIngredient);

    /**
     * Adds {@code operations} operations worth of {@code recipeInput} from the input.
     *
     * @param recipeInput Recipe input result.
     * @param operations  Operations to perform.
     */
    void use(INPUT recipeInput, int operations);

    /**
     * Calculates how many operations the input can sustain.
     *
     * @param recipeIngredient Recipe ingredient.
     * @param currentMax       The current maximum number of operations that can happen.
     *
     * @return The number of operations the input can sustain.
     */
    @Deprecated//TODO - 1.17: Remove this
    default int operationsCanSupport(InputIngredient<INPUT> recipeIngredient, int currentMax) {
        return operationsCanSupport(recipeIngredient, currentMax, 1);
    }

    /**
     * Calculates how many operations the input can sustain.
     *
     * @param recipeIngredient Recipe ingredient.
     * @param currentMax       The current maximum number of operations that can happen.
     * @param usageMultiplier  Usage multiplier to multiply the recipeIngredient's amount by per operation.
     *
     * @return The number of operations the input can sustain.
     */
    @Deprecated//TODO - 1.17: Remove this
    default int operationsCanSupport(InputIngredient<INPUT> recipeIngredient, int currentMax, int usageMultiplier) {
        return operationsCanSupport(getRecipeInput(recipeIngredient), currentMax, usageMultiplier);
    }

    /**
     * Calculates how many operations the input can sustain.
     *
     * @param recipeInput Recipe input gotten from {@link #getRecipeInput(InputIngredient)}.
     * @param currentMax  The current maximum number of operations that can happen.
     *
     * @return The number of operations the input can sustain.
     */
    default int operationsCanSupport(INPUT recipeInput, int currentMax) {
        return operationsCanSupport(recipeInput, currentMax, 1);
    }

    /**
     * Calculates how many operations the input can sustain.
     *
     * @param recipeInput     Recipe input gotten from {@link #getRecipeInput(InputIngredient)}.
     * @param currentMax      The current maximum number of operations that can happen.
     * @param usageMultiplier Usage multiplier to multiply the recipeInput's amount by per operation.
     *
     * @return The number of operations the input can sustain.
     */
    int operationsCanSupport(INPUT recipeInput, int currentMax, int usageMultiplier);
}
