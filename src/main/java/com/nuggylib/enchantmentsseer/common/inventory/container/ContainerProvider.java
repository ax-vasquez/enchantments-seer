package com.nuggylib.enchantmentsseer.common.inventory.container;

import com.nuggylib.enchantmentsseer.client.render.text.ILangEntry;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.IContainerProvider;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/inventory/container/ContainerProvider.java"
 */
public class ContainerProvider implements INamedContainerProvider {

    private final ITextComponent displayName;
    private final IContainerProvider provider;

    public ContainerProvider(ILangEntry translationHelper, IContainerProvider provider) {
        this(translationHelper.translate(), provider);
    }

    public ContainerProvider(ITextComponent displayName, IContainerProvider provider) {
        this.displayName = displayName;
        this.provider = provider;
    }

    @Nullable
    @Override
    public Container createMenu(int i, @Nonnull PlayerInventory inv, @Nonnull PlayerEntity player) {
        return provider.createMenu(i, inv, player);
    }

    @Nonnull
    @Override
    public ITextComponent getDisplayName() {
        return displayName;
    }
}
