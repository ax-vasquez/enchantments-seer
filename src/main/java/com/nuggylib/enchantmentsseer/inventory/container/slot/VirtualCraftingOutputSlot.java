package com.nuggylib.enchantmentsseer.inventory.container.slot;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.api.AutomationType;
import com.nuggylib.enchantmentsseer.common.content.qio.QIOCraftingWindow;
import com.nuggylib.enchantmentsseer.inventory.slot.BasicInventorySlot;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.function.Consumer;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/slot/VirtualCraftingOutputSlot.java"
 */
public class VirtualCraftingOutputSlot extends VirtualInventoryContainerSlot {

    @Nonnull
    private final QIOCraftingWindow craftingWindow;
    /**
     * @apiNote For use on client side to store if we can craft or not. On the server side we check it directly
     */
    private boolean canCraft;
    private int amountCrafted;

    public VirtualCraftingOutputSlot(BasicInventorySlot slot, @Nullable SlotOverlay slotOverlay, Consumer<ItemStack> uncheckedSetter,
                                     @Nonnull QIOCraftingWindow craftingWindow) {
        super(slot, craftingWindow.getWindowData(), slotOverlay, uncheckedSetter);
        this.craftingWindow = craftingWindow;
    }

    @Override
    public boolean canMergeWith(@Nonnull ItemStack stack) {
        //Don't allow double clicking to pickup stacks from the output slot
        return false;
    }

    @Override
    public boolean mayPlace(@Nonnull ItemStack stack) {
        //Short circuit to avoid looking through the various predicates
        return false;
    }

    @Nonnull
    @Override
    public ItemStack insertItem(@Nonnull ItemStack stack, Action action) {
        //Short circuit don't allow inserting into the output slot
        return stack;
    }

    @Nonnull
    @Override
    public ItemStack remove(int amount) {
        //Simulate extraction so as to not actually modify the slot
        // Note: In theory even though we are "simulating" here instead of actually changing how much is
        // in the slot, this shouldn't be a problem or be a risk of duplication, as there are slots like
        // the MerchantResultSlot which effectively do the same thing. They do it slightly differently
        // by taking it and then just setting the contents again, but effectively it is just returning
        // a copy so if mods cause any duplication glitches because of how we handle this, then in theory
        // they probably also cause duplication glitches with some of vanilla's slots as well.
        ItemStack extracted = getInventorySlot().extractItem(amount, Action.SIMULATE, AutomationType.MANUAL);
        //Adjust amount crafted by the amount that would have actually been extracted
        amountCrafted += extracted.getCount();
        return extracted;
    }

    /**
     * @implNote We override this similar to how {@link net.minecraft.inventory.container.CraftingResultSlot} does, but this never actually ends up getting called for our
     * slots.
     */
    @Override
    protected void onQuickCraft(@Nonnull ItemStack stack, int amount) {
        amountCrafted += amount;
        checkTakeAchievements(stack);
    }

    @Override
    protected void onSwapCraft(int numItemsCrafted) {
        amountCrafted += numItemsCrafted;
    }

    @Nonnull
    @Override
    public ItemStack onTake(@Nonnull PlayerEntity player, @Nonnull ItemStack stack) {
        ItemStack result = craftingWindow.performCraft(player, stack, amountCrafted);
        amountCrafted = 0;
        return result;
    }

    @Override
    public boolean mayPickup(@Nonnull PlayerEntity player) {
        if (player.level.isClientSide || !(player instanceof ServerPlayerEntity)) {
            return canCraft && super.mayPickup(player);
        }
        return craftingWindow.canViewRecipe((ServerPlayerEntity) player) && super.mayPickup(player);
    }

    @Nonnull
    @Override
    public ItemStack getStackToRender() {
        return canCraft ? super.getStackToRender() : ItemStack.EMPTY;
    }

}
