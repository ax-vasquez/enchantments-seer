package com.nuggylib.enchantmentsseer.common.capabilities;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.capabilities.resolver.ICapabilityResolver;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import java.util.*;
import java.util.function.BooleanSupplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/CapabilityCache.java"
 */
public class CapabilityCache {

    private final Map<Capability<?>, ICapabilityResolver> capabilityResolvers = new HashMap<>();
    /**
     * List of unique resolvers to make invalidating all easier as some resolvers (energy) may support multiple capabilities.
     */
    private final List<ICapabilityResolver> uniqueResolvers = new ArrayList<>();
    private final Set<Capability<?>> alwaysDisabled = new HashSet<>();
    private final Map<Capability<?>, List<BooleanSupplier>> semiDisabled = new HashMap<>();

    /**
     * Adds a capability resolver to the list of resolvers for this cache.
     */
    public void addCapabilityResolver(ICapabilityResolver resolver) {
        uniqueResolvers.add(resolver);
        List<Capability<?>> supportedCapabilities = resolver.getSupportedCapabilities();
        for (Capability<?> supportedCapability : supportedCapabilities) {
            //Don't add null capabilities. (Either ones that are not loaded mod wise or get fired during startup)
            if (supportedCapability != null && capabilityResolvers.put(supportedCapability, resolver) != null) {
                EnchantmentsSeer.logger.warn("Multiple capability resolvers registered for {}. Overriding", supportedCapability.getName(), new Exception());
            }
        }
    }

    /**
     * Marks all the given capabilities as always being disabled.
     */
    public void addDisabledCapabilities(Capability<?>... capabilities) {
        for (Capability<?> capability : capabilities) {
            //Don't add null capabilities. (Either ones that are not loaded mod wise or get fired during startup)
            if (capability != null) {
                alwaysDisabled.add(capability);
            }
        }
    }

    /**
     * Marks all the given capabilities as always being disabled.
     */
    public void addDisabledCapabilities(Collection<Capability<?>> capabilities) {
        for (Capability<?> capability : capabilities) {
            //Don't add null capabilities. (Either ones that are not loaded mod wise or get fired during startup)
            if (capability != null) {
                alwaysDisabled.add(capability);
            }
        }
    }

    /**
     * Marks the given capability as having a check for sometimes being disabled.
     *
     * @implNote These "semi disabled" checks are stored in a list so that children can define more cases a capability should be disabled than the ones the parent already
     * wants them to be disabled in.
     */
    public void addSemiDisabledCapability(Capability<?> capability, BooleanSupplier checker) {
        //Don't add null capabilities. (Either ones that are not loaded mod wise or get fired during startup)
        if (capability != null) {
            semiDisabled.computeIfAbsent(capability, cap -> new ArrayList<>()).add(checker);
        }
    }

    /**
     * Checks if the given capability can be resolved by this capability cache.
     */
    public boolean canResolve(Capability<?> capability) {
        return capabilityResolvers.containsKey(capability);
    }

    /**
     * Gets a capability on the given side not checking to ensure that it is not disabled.
     */
    public <T> LazyOptional<T> getCapabilityUnchecked(Capability<T> capability, @Nullable Direction side) {
        ICapabilityResolver capabilityResolver = capabilityResolvers.get(capability);
        if (capabilityResolver == null) {
            return LazyOptional.empty();
        }
        return capabilityResolver.resolve(capability, side);
    }

    /**
     * Invalidates the given capability on the given side.
     *
     * @param capability Capability
     * @param side       Side
     */
    public void invalidate(Capability<?> capability, @Nullable Direction side) {
        ICapabilityResolver capabilityResolver = capabilityResolvers.get(capability);
        if (capabilityResolver != null) {
            capabilityResolver.invalidate(capability, side);
        }
    }

    /**
     * Invalidates all cached capabilities.
     */
    public void invalidateAll() {
        uniqueResolvers.forEach(ICapabilityResolver::invalidateAll);
    }

    /**
     * Checks if the given capability is disabled for the specific side.
     *
     * @return {@code true} if the capability is disabled, {@code false} otherwise.
     */
    public boolean isCapabilityDisabled(Capability<?> capability, @Nullable Direction side) {
        // TODO: See if we can remove this - we don't need to use capabilities
        return false;
    }
}
