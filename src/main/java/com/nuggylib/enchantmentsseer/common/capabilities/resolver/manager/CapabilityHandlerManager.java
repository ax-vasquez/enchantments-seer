package com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.IHolder;
import com.nuggylib.enchantmentsseer.common.capabilities.holder.slot.IInventorySlotHolder;
import com.nuggylib.enchantmentsseer.common.util.annotations.FieldsAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.List;
import java.util.function.BiFunction;

/**
 * Inspired by Mekanism
 *
 * This is a general-purpose capability handler manager class. Mekanism designed it to be used for a variety of different
 * "holders", but for our mod, we only ever use it with an inventory holder of some kind.
 *
 * Our version is significantly simplified by removing any reference to sided logic, which also removes the need for
 * using {@link LazyOptional}s. Not that there is a problem using those; we simply don't need them since Mekanism only
 * used them for side-related logic, which we don't need. It is entirely possible that we could leverage {@link LazyOptional}s
 * in a different manner to gain some benefit, but we're following the "make it work > make it right" mentality for the first
 * releases.
 *
 * @see "https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/"
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CapabilityHandlerManager<HOLDER extends IHolder, CONTAINER, HANDLER> implements ICapabilityHandlerManager<CONTAINER> {

    // TODO: Update this so we no longer need to use a BiFunction - that's a remnant from Mekanism code, but it works for now
    private final BiFunction<HOLDER, Direction, List<CONTAINER>> containerGetter;

    private final List<Capability<?>> supportedCapability;
    /**
     * The underlying handler (e.g., item handler, fluid handler, energy handler)
     *
     * In Mekanism's code, they use this base handler to construct more handlers using their "side" logic (using the
     * {@link Direction} class). Since we have no need for side logic, we simply use this base handler as the "main"
     * handler for the given {@link CapabilityHandlerManager} instance.
     */
    private final HANDLER baseHandler;
    // TODO: We can probably remove this since it will always be true in our case (`holder` will always be defined in
    //  our mod since we only have one block and it stores items, so it will always have a corresponding `holder`)
    private final boolean canHandle;
    /**
     * The "holder" for the corresponding type that this {@link CapabilityHandlerManager} "holds"
     *
     * In our case, this will only ever be an {@link IInventorySlotHolder} since we only have one block that needs to
     * use this class, and it's one that stores items.
     */
    @Nullable
    protected final HOLDER holder;

    // TODO: See what impact removing the proxy-related logic has
    protected CapabilityHandlerManager(@Nullable HOLDER holder, HANDLER baseHandler, Capability<HANDLER> supportedCapability,
                                       BiFunction<HOLDER, Direction, List<CONTAINER>> containerGetter) {
        this.supportedCapability = Collections.singletonList(supportedCapability);
        this.holder = holder;
        this.canHandle = this.holder != null;
        this.baseHandler = baseHandler;
        this.containerGetter = containerGetter;
    }

    public HANDLER getInternal() {
        return baseHandler;
    }

    @Override
    public boolean canHandle() {
        return canHandle;
    }

    @Override
    public List<CONTAINER> getContainers() {
        return canHandle() ? containerGetter.apply(holder, null) : Collections.emptyList();
    }

}
