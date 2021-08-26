package com.nuggylib.enchantmentsseer.common.lib.security;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * Shamelessly-copied from the Mekanism codebase (because they are awesome and their stuff works <3)
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/security/ISecurityObject.java"
 */
public interface ISecurityObject {

    ISecurityObject NO_SECURITY = new ISecurityObject() {
        @Override
        public boolean hasSecurity() {
            return false;
        }

        @Nullable
        @Override
        public UUID getOwnerUUID() {
            return null;
        }

        @Nullable
        @Override
        public String getOwnerName() {
            return null;
        }

        @Override
        public SecurityMode getSecurityMode() {
            return SecurityMode.PUBLIC;
        }

        @Override
        public void setSecurityMode(SecurityMode mode) {
        }
    };

    default boolean hasSecurity() {
        return true;
    }

    @Nullable
    UUID getOwnerUUID();

    @Nullable
    String getOwnerName();

    SecurityMode getSecurityMode();

    void setSecurityMode(SecurityMode mode);

    default void onSecurityChanged(SecurityMode old, SecurityMode mode) {
    }
}

