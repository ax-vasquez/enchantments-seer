package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.api.IContentsListener;
import com.nuggylib.enchantmentsseer.api.annotations.NonNull;
import com.nuggylib.enchantmentsseer.api.inventory.AutomationType;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.slot.BasicInventorySlot;
import com.nuggylib.enchantmentsseer.common.util.SeersEnchantmentHelper;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentData;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnchantInventorySlot extends BasicInventorySlot {

    private List<Enchantment> enchantmentsForCurrentItem = new ArrayList<>();

    public static EnchantInventorySlot at(@Nullable IContentsListener listener, int x, int y) {
        return at(alwaysTrue, listener, x, y);
    }

    public static EnchantInventorySlot at(Predicate<@NonNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y) {
        return at(alwaysTrue, isItemValid, listener, x, y);
    }

    public static EnchantInventorySlot at(Predicate<@NonNull ItemStack> insertPredicate, Predicate<@NonNull ItemStack> isItemValid, @Nullable IContentsListener listener,
                                        int x, int y) {
        Objects.requireNonNull(insertPredicate, "Insertion check cannot be null");
        Objects.requireNonNull(isItemValid, "Item validity check cannot be null");
        return new EnchantInventorySlot(insertPredicate, isItemValid, listener, x, y);
    }

    protected EnchantInventorySlot(Predicate<@NonNull ItemStack> insertPredicate, Predicate<@NonNull ItemStack> isItemValid, @Nullable IContentsListener listener, int x, int y) {
        super(notExternal, (stack, automationType) -> insertPredicate.test(stack), isItemValid, listener, x, y);
        setSlotType(ContainerSlotType.INPUT);
    }

    public List<Enchantment> getEnchantments(ItemStack itemToEnchant) {
        return enchantmentsForCurrentItem;
    }

    /**
     * Handles the logic to generate/refresh the list of enchantments for the item currently in the slot.
     *
     * @param stack
     * @param action
     * @param automationType
     * @return
     */
    @Override
    public ItemStack insertItem(ItemStack stack, Action action, AutomationType automationType) {
        enchantmentsForCurrentItem = SeersEnchantmentHelper.getAvailableEnchantmentResults(stack, null, true);
        for (Enchantment enchantment : enchantmentsForCurrentItem) {
            EnchantmentsSeer.logger.info(String.format("Enchantment (at max): %s", enchantment.getFullname(enchantment.getMaxLevel()).getString()));
        }
        return super.insertItem(stack, action, automationType);
    }
}
