package com.nuggylib.enchantmentsseer.common.registration.impl;

import com.nuggylib.enchantmentsseer.common.inventory.container.tile.EnchantmentsSeerTileContainer;
import com.nuggylib.enchantmentsseer.common.registration.INamedEntry;
import com.nuggylib.enchantmentsseer.common.registration.WrappedDeferredRegister;
import com.nuggylib.enchantmentsseer.common.tile.base.TileEntityEnchantmentsSeer;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.common.extensions.IForgeContainerType;
import net.minecraftforge.fml.network.IContainerFactory;
import net.minecraftforge.registries.ForgeRegistries;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/registration/impl/ContainerTypeDeferredRegister.java"
 */
public class ContainerTypeDeferredRegister extends WrappedDeferredRegister<ContainerType<?>> {

    public ContainerTypeDeferredRegister(String modid) {
        super(modid, ForgeRegistries.CONTAINERS);
    }

    public <TILE extends TileEntityEnchantmentsSeer> ContainerTypeRegistryObject<EnchantmentsSeerTileContainer<TILE>> register(INamedEntry nameProvider, Class<TILE> tileClass) {
        return register(nameProvider.getInternalRegistryName(), tileClass);
    }

    public <TILE extends TileEntityEnchantmentsSeer> ContainerTypeRegistryObject<EnchantmentsSeerTileContainer<TILE>> register(String name, Class<TILE> tileClass) {
        //Temporarily generate this using null as we replace it with a proper value before we actually use this so it is fine
        ContainerTypeRegistryObject<EnchantmentsSeerTileContainer<TILE>> registryObject = new ContainerTypeRegistryObject<>(null);
        IContainerFactory<EnchantmentsSeerTileContainer<TILE>> factory = (id, inv, buf) ->
                new EnchantmentsSeerTileContainer<>(registryObject, id, inv, EnchantmentsSeerTileContainer.getTileFromBuf(buf, tileClass));
        return register(name, () -> IForgeContainerType.create(factory), registryObject::setRegistryObject);
    }

    public <CONTAINER extends Container> ContainerTypeRegistryObject<CONTAINER> register(INamedEntry nameProvider, IContainerFactory<CONTAINER> factory) {
        return register(nameProvider.getInternalRegistryName(), factory);
    }

    public <CONTAINER extends Container> ContainerTypeRegistryObject<CONTAINER> register(String name, IContainerFactory<CONTAINER> factory) {
        return register(name, () -> IForgeContainerType.create(factory), ContainerTypeRegistryObject::new);
    }

    public <TILE extends TileEntityEnchantmentsSeer> ContainerBuilder<TILE> custom(INamedEntry nameProvider, Class<TILE> tileClass) {
        return new ContainerBuilder<>(nameProvider.getInternalRegistryName(), tileClass);
    }

    public <TILE extends TileEntityEnchantmentsSeer> ContainerBuilder<TILE> custom(String name, Class<TILE> tileClass) {
        return new ContainerBuilder<>(name, tileClass);
    }

    public class ContainerBuilder<TILE extends TileEntityEnchantmentsSeer> {

        private final String name;
        private final Class<TILE> tileClass;
        private int offsetX, offsetY;

        private ContainerBuilder(String name, Class<TILE> tileClass) {
            this.name = name;
            this.tileClass = tileClass;
        }

        public ContainerBuilder<TILE> offset(int offsetX, int offsetY) {
            this.offsetX = offsetX;
            this.offsetY = offsetY;
            return this;
        }

        public ContainerTypeRegistryObject<EnchantmentsSeerTileContainer<TILE>> build() {
            ContainerTypeRegistryObject<EnchantmentsSeerTileContainer<TILE>> registryObject = new ContainerTypeRegistryObject<>(null);
            IContainerFactory<EnchantmentsSeerTileContainer<TILE>> factory = (id, inv, buf) ->
                    new EnchantmentsSeerTileContainer<TILE>(registryObject, id, inv, EnchantmentsSeerTileContainer.getTileFromBuf(buf, tileClass)) {
                        @Override
                        protected int getInventoryXOffset() {
                            return super.getInventoryXOffset() + offsetX;
                        }

                        @Override
                        protected int getInventoryYOffset() {
                            return super.getInventoryYOffset() + offsetY;
                        }
                    };
            return register(name, () -> IForgeContainerType.create(factory), registryObject::setRegistryObject);
        }
    }
}
