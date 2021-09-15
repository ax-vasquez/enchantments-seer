package com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager;

import com.nuggylib.enchantmentsseer.common.capabilities.resolver.ICapabilityResolver;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Inspired by Mekanism
 *
 *
 *
 * @param <CONTAINER>
 */
@MethodsReturnNonnullByDefault
public interface ICapabilityHandlerManager<CONTAINER> extends ICapabilityResolver {

    /**
     * Checks if the capability handler manager can handle this substance type.
     *
     * @return {@code true} if it can handle the substance type, {@code false} otherwise.
     */
    boolean canHandle();

    /**
     * Gets the containers
     */
    List<CONTAINER> getContainers();
}
