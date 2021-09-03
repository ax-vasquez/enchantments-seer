package com.nuggylib.enchantmentsseer.common.lib.transmitter.acceptor;

import net.minecraft.tileentity.TileEntity;

/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/lib/transmitter/acceptor/AbstractAcceptorInfo.java"
 */
public abstract class AbstractAcceptorInfo {

    private final TileEntity tile;

    protected AbstractAcceptorInfo(TileEntity tile) {
        this.tile = tile;
    }

    public TileEntity getTile() {
        return tile;
    }
}
