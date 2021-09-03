package com.nuggylib.enchantmentsseer.common.block.states;

import com.nuggylib.enchantmentsseer.api.math.MathUtils;

// TODO: We probably don't need this class - see how we can remove it (without breaking the general transmitter logic - if we need that)
/**
 * Shamelessly-copied from the Mekanism codebase; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/block/states/TransmitterType.java"
 */
public enum TransmitterType {
    UNIVERSAL_CABLE(Size.SMALL),
    MECHANICAL_PIPE(Size.LARGE),
    PRESSURIZED_TUBE(Size.SMALL),
    LOGISTICAL_TRANSPORTER(Size.LARGE),
    RESTRICTIVE_TRANSPORTER(Size.LARGE),
    DIVERSION_TRANSPORTER(Size.LARGE),
    THERMODYNAMIC_CONDUCTOR(Size.SMALL);

    private static final TransmitterType[] TYPES = values();

    private final Size size;

    TransmitterType(Size size) {
        this.size = size;
    }

    public Size getSize() {
        return size;
    }

    public static TransmitterType byIndexStatic(int index) {
        return MathUtils.getByIndexMod(TYPES, index);
    }

    public enum Size {
        SMALL(6),
        LARGE(8);

        public final int centerSize;

        Size(int size) {
            centerSize = size;
        }
    }
}
