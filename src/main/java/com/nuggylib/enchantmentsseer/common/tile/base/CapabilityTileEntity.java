package com.nuggylib.enchantmentsseer.common.tile.base;

import com.nuggylib.enchantmentsseer.common.capabilities.CapabilityCache;
import com.nuggylib.enchantmentsseer.common.capabilities.resolver.ICapabilityResolver;
import com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager.ICapabilityHandlerManager;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.function.BooleanSupplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/base/CapabilityTileEntity.java"
 */
public abstract class CapabilityTileEntity extends TileEntityUpdateable {

    private final CapabilityCache capabilityCache = new CapabilityCache();

    public CapabilityTileEntity(TileEntityType<?> type) {
        super(type);
    }

    protected final void addCapabilityResolvers(List<ICapabilityHandlerManager<?>> capabilityHandlerManagers) {
        for (ICapabilityHandlerManager<?> capabilityHandlerManager : capabilityHandlerManagers) {
            //Add all managers that we support in our tile, as capability resolvers
            if (capabilityHandlerManager.canHandle()) {
                addCapabilityResolver(capabilityHandlerManager);
            }
        }
    }

    protected final void addCapabilityResolver(ICapabilityResolver resolver) {
        capabilityCache.addCapabilityResolver(resolver);
    }

    protected final void addDisabledCapabilities(Capability<?>... capabilities) {
        capabilityCache.addDisabledCapabilities(capabilities);
    }

    protected final void addDisabledCapabilities(Collection<Capability<?>> capabilities) {
        capabilityCache.addDisabledCapabilities(capabilities);
    }

    protected final void addSemiDisabledCapability(Capability<?> capability, BooleanSupplier checker) {
        capabilityCache.addSemiDisabledCapability(capability, checker);
    }

    protected <T> boolean canEverResolve(Capability<T> capability) {
        return capabilityCache.canResolve(capability);
    }

    @Nonnull
    @Override
    public <T> LazyOptional<T> getCapability(@Nonnull Capability<T> capability, @Nullable Direction side) {
        if (capabilityCache.isCapabilityDisabled(capability, side)) {
            return LazyOptional.empty();
        } else if (capabilityCache.canResolve(capability)) {
            return capabilityCache.getCapabilityUnchecked(capability, side);
        }
        //Call to the TileEntity's Implementation of getCapability if we could not find a capability ourselves
        return super.getCapability(capability, side);
    }

    @Override
    protected void invalidateCaps() {
        super.invalidateCaps();
        //When the capabilities on our tile get invalidated, make sure to also invalidate all our cached ones
        invalidateCachedCapabilities();
    }

    public void invalidateCachedCapabilities() {
        capabilityCache.invalidateAll();
    }

    public void invalidateCapability(@Nonnull Capability<?> capability, @Nullable Direction side) {
        capabilityCache.invalidate(capability, side);
    }

    public void invalidateCapabilities(@Nonnull Collection<Capability<?>> capabilities, @Nullable Direction side) {
        for (Capability<?> capability : capabilities) {
            invalidateCapability(capability, side);
        }
    }
}
