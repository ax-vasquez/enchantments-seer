package com.nuggylib.enchantmentsseer.common.lib.security;

import javax.annotation.Nullable;
import java.util.UUID;

public interface ISecurityTile extends ISecurityObject {

    TileComponentSecurity getSecurity();

    @Nullable
    @Override
    default UUID getOwnerUUID() {
        TileComponentSecurity security = getSecurity();
        return security == null ? null : security.getOwnerUUID();
    }

    @Nullable
    @Override
    default String getOwnerName() {
        TileComponentSecurity security = getSecurity();
        return security == null ? null : security.getOwnerName();
    }

    @Override
    default SecurityMode getSecurityMode() {
        TileComponentSecurity security = getSecurity();
        return security == null ? SecurityMode.PUBLIC : security.getMode();
    }

    @Override
    default void setSecurityMode(SecurityMode mode) {
        TileComponentSecurity security = getSecurity();
        if (security != null) {
            security.setMode(mode);
        }
    }
}
