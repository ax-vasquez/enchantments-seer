package com.nuggylib.enchantmentsseer.common.capabilities.resolver;

import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;

/**
 * Inspired by Mekanism
 */
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public interface ICapabilityResolver {

    /**
     * Gets the list of capabilities this resolver is able to resolve.
     *
     * @return List of capabilities this resolver can resolve.
     */
    List<Capability<?>> getSupportedCapabilities();

    /**
     * Resolves a given capability. This value should be cached for later invalidation, as well as quicker re-lookup.
     *
     * @param capability Capability
     *
     * @return LazyOptional for the given capability
     *
     * @apiNote This method should only be called with capabilities that are in {@link #getSupportedCapabilities()}
     * @implNote The result should be cached
     */
    <T> LazyOptional<T> resolve(Capability<T> capability);

    /**
     * Invalidates the given capability.
     *
     * @param capability Capability
     */
    void invalidate(Capability<?> capability);

    /**
     * Invalidates all cached capabilities.
     */
    void invalidateAll();
}
