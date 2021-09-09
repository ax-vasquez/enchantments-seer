package com.nuggylib.enchantmentsseer.common.inventory.container.slot;

import com.nuggylib.enchantmentsseer.api.Action;
import com.nuggylib.enchantmentsseer.api.IContentsListener;
import com.nuggylib.enchantmentsseer.api.annotations.NonNull;
import com.nuggylib.enchantmentsseer.api.inventory.AutomationType;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.slot.BasicInventorySlot;
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

    protected List<EnchantmentData> getEnchantments(ItemStack itemToEnchant) {
        EnchantmentsSeer.logger.info("Getting enchantments for item");
        // TODO: Cache the data for the enchantments somewhere
        return EnchantmentHelper.getAvailableEnchantmentResults(10, itemToEnchant, false);
    }

    @Override
    public ItemStack insertItem(ItemStack stack, Action action, AutomationType automationType) {
        EnchantmentsSeer.logger.info("EnchantInventorySlot#insertItem");
        List<EnchantmentData> enchantments = getEnchantments(stack);
        for (EnchantmentData item : enchantments) {
            EnchantmentsSeer.logger.info(String.format("Enchantment: %s", item.enchantment.getFullname(0).getString()));
        }
        return super.insertItem(stack, action, automationType);
    }
}
