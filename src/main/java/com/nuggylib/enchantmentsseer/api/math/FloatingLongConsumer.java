package com.nuggylib.enchantmentsseer.api.math;

import javax.annotation.Nonnull;
import java.util.function.Consumer;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/math/FloatingLongConsumer.java"
 */
@FunctionalInterface
public interface FloatingLongConsumer extends Consumer<FloatingLong> {

    @Override
    void accept(@Nonnull FloatingLong floatingLong);
}
