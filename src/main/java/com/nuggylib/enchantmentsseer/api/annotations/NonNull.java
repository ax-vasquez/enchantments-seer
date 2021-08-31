package com.nuggylib.enchantmentsseer.api.annotations;

import javax.annotation.Nonnull;
import javax.annotation.meta.TypeQualifier;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * {@link javax.annotation.Nonnull}, but for all the things (like type params)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/api/java/mekanism/api/annotations/NonNull.java"
 */
@TypeQualifier
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE_PARAMETER, ElementType.FIELD, ElementType.LOCAL_VARIABLE, ElementType.PARAMETER, ElementType.TYPE, ElementType.TYPE_USE})
@Nonnull
public @interface NonNull {}
