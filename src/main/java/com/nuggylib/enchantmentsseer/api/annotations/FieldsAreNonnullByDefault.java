package com.nuggylib.enchantmentsseer.api.annotations;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifierDefault;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * Interface to declare that all fields in a class are {@link Nonnull}
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/annotations/FieldsAreNonnullByDefault.java"
 */
@Nonnull
@TypeQualifierDefault(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface FieldsAreNonnullByDefault {
}
