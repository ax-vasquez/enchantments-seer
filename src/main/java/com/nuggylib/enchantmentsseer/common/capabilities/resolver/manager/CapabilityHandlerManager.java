package com.nuggylib.enchantmentsseer.common.capabilities.resolver.manager;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.IHolder;
import com.nuggylib.enchantmentsseer.common.util.annotations.FieldsAreNonnullByDefault;
import mcp.MethodsReturnNonnullByDefault;
import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;

import javax.annotation.Nullable;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Collections;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.function.BiFunction;

/**
 * Inspired by Mekanism
 *
 * This is a general-purpose capability handler manager class. Mekanism designed it to be used for a variety of different
 * "holders", but for our mod, we only ever use it with an inventory holder of some kind.
 *
 * @see "https://mcforge.readthedocs.io/en/latest/datastorage/capabilities/"
 */
@FieldsAreNonnullByDefault
@ParametersAreNonnullByDefault
@MethodsReturnNonnullByDefault
public class CapabilityHandlerManager<HOLDER extends IHolder, CONTAINER, HANDLER, SIDED_HANDLER extends HANDLER> implements ICapabilityHandlerManager<CONTAINER> {

    private final ProxyCreator<HANDLER, SIDED_HANDLER> proxyCreator;
    private final BiFunction<HOLDER, Direction, List<CONTAINER>> containerGetter;
    private final Map<Direction, LazyOptional<HANDLER>> handlers;
    private final List<Capability<?>> supportedCapability;
    private final SIDED_HANDLER baseHandler;
    private final boolean canHandle;
    @Nullable
    private LazyOptional<HANDLER> readOnlyHandler;
    @Nullable
    protected final HOLDER holder;

    protected CapabilityHandlerManager(@Nullable HOLDER holder, SIDED_HANDLER baseHandler, Capability<HANDLER> supportedCapability,
                                       ProxyCreator<HANDLER, SIDED_HANDLER> proxyCreator, BiFunction<HOLDER, Direction, List<CONTAINER>> containerGetter) {
        this.supportedCapability = Collections.singletonList(supportedCapability);
        this.holder = holder;
        this.canHandle = this.holder != null;
        this.baseHandler = baseHandler;
        this.proxyCreator = proxyCreator;
        this.containerGetter = containerGetter;
        if (this.canHandle) {
            handlers = new EnumMap<>(Direction.class);
        } else {
            handlers = Collections.emptyMap();
        }
    }

    public SIDED_HANDLER getInternal() {
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

    @Override
    public List<Capability<?>> getSupportedCapabilities() {
        return supportedCapability;
    }

    /**
     * Lazily get and cache a handler instance for the given side, and make it be read only if something else is trying to interact with us using the null side
     *
     * @apiNote Assumes that {@link #canHandle} has been called before this and that it was {@code true}.
     */
    @Override
    public <T> LazyOptional<T> resolve(Capability<T> capability) {
        if (getContainers(side).isEmpty()) {
            //If we don't have any containers accessible from that side, don't return a handler
            //TODO: Evaluate moving this somehow into being done via the is disabled check
            return LazyOptional.empty();
        }
        if (side == null) {
            if (readOnlyHandler == null || !readOnlyHandler.isPresent()) {
                readOnlyHandler = LazyOptional.of(() -> proxyCreator.create(baseHandler, null, holder));
            }
            return readOnlyHandler.cast();
        }
        LazyOptional<HANDLER> cachedCapability = handlers.get(side);
        if (cachedCapability == null || !cachedCapability.isPresent()) {
            handlers.put(side, cachedCapability = LazyOptional.of(() -> proxyCreator.create(baseHandler, side, holder)));
        }
        return cachedCapability.cast();
    }

    @Override
    public void invalidate(Capability<?> capability) {
        if (side == null) {
            invalidateReadOnly();
        } else {
            invalidate(handlers.get(side));
        }
    }

    @Override
    public void invalidateAll() {
        invalidateReadOnly();
        handlers.values().forEach(this::invalidate);
    }

    private void invalidateReadOnly() {
        if (readOnlyHandler != null && readOnlyHandler.isPresent()) {
            readOnlyHandler.invalidate();
            readOnlyHandler = null;
        }
    }

    protected void invalidate(@Nullable LazyOptional<?> cachedCapability) {
        if (cachedCapability != null && cachedCapability.isPresent()) {
            cachedCapability.invalidate();
        }
    }

    @FunctionalInterface
    public interface ProxyCreator<HANDLER, SIDED_HANDLER extends HANDLER> {

        HANDLER create(SIDED_HANDLER handler, @Nullable Direction side, @Nullable IHolder holder);
    }
}
