package com.nuggylib.enchantmentsseer.common.block.attribute;

import java.util.function.Function;
import com.nuggylib.enchantmentsseer.client.render.text.TextComponentUtil;
import com.nuggylib.enchantmentsseer.common.inventory.container.ContainerProvider;
import com.nuggylib.enchantmentsseer.common.inventory.container.EnchantmentsSeerContainer;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.EnchantmentsSeerTileContainer;
import com.nuggylib.enchantmentsseer.common.registration.impl.ContainerTypeRegistryObject;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import net.minecraft.inventory.container.INamedContainerProvider;

import java.util.function.Supplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/attribute/AttributeGui.java"
 */
public class AttributeGui implements Attribute {

    private final Supplier<ContainerTypeRegistryObject<? extends EnchantmentsSeerContainer>> containerRegistrar;
    private Function<TileEntityEnchantmentsSeer, INamedContainerProvider> containerSupplier = tile -> new ContainerProvider(TextComponentUtil.build(tile.getBlockType()),
            (i, inv, player) -> new EnchantmentsSeerTileContainer<>(getContainerType(), i, inv, tile));

    public AttributeGui(Supplier<ContainerTypeRegistryObject<? extends EnchantmentsSeerContainer>> containerRegistrar) {
        this.containerRegistrar = containerRegistrar;
    }

    public void setCustomContainer(Function<TileEntityEnchantmentsSeer, INamedContainerProvider> containerSupplier) {
        this.containerSupplier = containerSupplier;
    }

    public ContainerTypeRegistryObject<? extends EnchantmentsSeerContainer> getContainerType() {
        return containerRegistrar.get();
    }

    public INamedContainerProvider getProvider(TileEntityEnchantmentsSeer tile) {
        return containerSupplier.apply(tile);
    }
}
