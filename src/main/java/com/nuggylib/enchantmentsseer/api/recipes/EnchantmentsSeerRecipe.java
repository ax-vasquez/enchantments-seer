package com.nuggylib.enchantmentsseer.api.recipes;


import java.util.Objects;
import javax.annotation.Nonnull;
import com.nuggylib.enchantmentsseer.api.inventory.IgnoredIInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.network.PacketBuffer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Shamelessly copied from Mekanism
 * @see https://github.com/mekanism/Mekanism/blob/849917af38926c817377f51c57d23735cb374ce9/src/api/java/mekanism/api/recipes/MekanismRecipe.java
 */

/**
 * Base class for helping wrap our recipes into IRecipes.
 */
public abstract class EnchantmentsSeerRecipe implements IRecipe<IgnoredIInventory> {

    private final ResourceLocation id;

    /**
     * @param id Recipe name.
     */
    protected EnchantmentsSeerRecipe(ResourceLocation id) {
        this.id = Objects.requireNonNull(id, "Recipe name cannot be null.");
    }

    /**
     * Writes this recipe to a PacketBuffer.
     *
     * @param buffer The buffer to write to.
     */
    public abstract void write(PacketBuffer buffer);

    @Nonnull
    @Override
    public ResourceLocation getId() {
        return id;
    }

    @Override
    public boolean matches(@Nonnull IgnoredIInventory inv, @Nonnull World world) {
        return true;
    }

    @Override
    public boolean isSpecial() {
        //Note: If we make this non dynamic, we can make it show in vanilla's crafting book and also then obey the recipe locking.
        // For now none of that works/makes sense in our concept so don't lock it
        return true;
    }

    @Nonnull
    @Override
    public ItemStack assemble(@Nonnull IgnoredIInventory inv) {
        return ItemStack.EMPTY;
    }

    @Override
    public boolean canCraftInDimensions(int width, int height) {
        return true;
    }

    @Nonnull
    @Override
    public ItemStack getResultItem() {
        return ItemStack.EMPTY;
    }
}