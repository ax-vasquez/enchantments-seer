package com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager;

import com.nuggylib.enchantmentsseer.common.capabilities.resolver.ICapabilityResolver;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @param <CONTAINER>
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/resolver/manager/ICapabilityHandlerManager.java"
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
     * Gets the containers for a given side.
     *
     * @param side The side
     *
     * @return Containers on the given side
     */
    List<CONTAINER> getContainers(@Nullable Direction side);
}
