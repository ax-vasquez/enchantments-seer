package com.nuggylib.enchantmentsseer.common.inventory.container;

import javax.annotation.Nonnull;
import java.util.Objects;
import java.util.function.IntPredicate;

/**
 * Copied from Mekanism
 *
 * TODO: Document the purpose for this class in greater detail
 */
public class SelectedWindowData {

    public static final SelectedWindowData UNSPECIFIED = new SelectedWindowData(WindowType.UNSPECIFIED);

    @Nonnull
    public final WindowType type;
    public final byte extraData;

    public SelectedWindowData(@Nonnull WindowType type) {
        this(type, (byte) 0);
    }

    /**
     * It is expected to only call this with a piece of extra data that is valid. If it is not valid this end up treating it as zero instead.
     */
    public SelectedWindowData(@Nonnull WindowType type, byte extraData) {
        this.type = Objects.requireNonNull(type);
        this.extraData = this.type.validator.test(extraData) ? extraData : 0;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        } else if (o == null || getClass() != o.getClass()) {
            return false;
        }
        SelectedWindowData other = (SelectedWindowData) o;
        return extraData == other.extraData && type == other.type;
    }

    @Override
    public int hashCode() {
        return Objects.hash(type, extraData);
    }

    public enum WindowType {
        /**
         * For use by windows that don't actually have any server side specific logic required.
         */
        UNSPECIFIED;

        private final IntPredicate validator;

        WindowType() {
            this(v -> v == 0);
            //TODO: Evaluate putting this in some constant if we end up with lots of window types.
            // The issue is that we need to make sure the constant is initialized before here so for now we just let there be multiple
        }

        WindowType(IntPredicate validator) {
            this.validator = validator;
        }
    }

}
