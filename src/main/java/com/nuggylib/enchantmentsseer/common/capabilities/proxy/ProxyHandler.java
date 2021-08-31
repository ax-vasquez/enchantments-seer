package com.nuggylib.enchantmentsseer.common.capabilities.proxy;

import com.nuggylib.enchantmentsseer.common.capabilities.holder.IHolder;
import com.nuggylib.enchantmentsseer.api.annotations.FieldsAreNonnullByDefault;
import net.minecraft.util.Direction;

import javax.annotation.Nullable;
import java.util.function.BooleanSupplier;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/capabilities/proxy/ProxyHandler.java"
 */
@FieldsAreNonnullByDefault
public class ProxyHandler {

    private static final BooleanSupplier alwaysFalse = () -> false;

    @Nullable
    protected final Direction side;
    protected final boolean readOnly;
    protected final BooleanSupplier readOnlyInsert;
    protected final BooleanSupplier readOnlyExtract;

    protected ProxyHandler(@Nullable Direction side, @Nullable IHolder holder) {
        this.side = side;
        this.readOnly = this.side == null;
        this.readOnlyInsert = holder == null ? alwaysFalse : () -> !holder.canInsert(side);
        this.readOnlyExtract = holder == null ? alwaysFalse : () -> !holder.canExtract(side);
    }
}
