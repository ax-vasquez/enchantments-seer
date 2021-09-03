package com.nuggylib.enchantmentsseer.common.tile.component.config;

import com.nuggylib.enchantmentsseer.api.RelativeSide;
import com.nuggylib.enchantmentsseer.common.tile.component.config.slot.ISlotInfo;
import com.nuggylib.enchantmentsseer.common.util.EnumUtils;
import net.minecraft.util.Direction;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.*;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Shamelessly-copied from Mekanism; all credit goes to them
 *
 * @see "https://github.com/mekanism/Mekanism/blob/v10.1/src/main/java/mekanism/common/tile/component/config/ConfigInfo.java"
 */
public class ConfigInfo {

    private final Supplier<Direction> facingSupplier;
    //TODO: Ejecting/can eject, how do we want to use these
    private boolean canEject;
    private boolean ejecting;
    private final Map<RelativeSide, DataType> sideConfig;
    private final Map<DataType, ISlotInfo> slotInfo;
    // used so slot & tank GUIs can quickly reference which color overlay to render
    private final Map<Object, List<DataType>> containerTypeMapping;
    //Not final so that it can be lazily initialized
    private Set<RelativeSide> disabledSides;

    public ConfigInfo(@Nonnull Supplier<Direction> facingSupplier) {
        this.facingSupplier = facingSupplier;
        canEject = true;
        ejecting = false;
        sideConfig = new EnumMap<>(RelativeSide.class);
        for (RelativeSide side : EnumUtils.SIDES) {
            sideConfig.put(side, DataType.NONE);
        }
        slotInfo = new EnumMap<>(DataType.class);
        containerTypeMapping = new HashMap<>();
    }

    public boolean canEject() {
        return canEject;
    }

    public void setCanEject(boolean canEject) {
        this.canEject = canEject;
    }

    public boolean isEjecting() {
        return ejecting;
    }

    public void setEjecting(boolean ejecting) {
        this.ejecting = ejecting;
    }

    public void addDisabledSides(@Nonnull RelativeSide... sides) {
        if (disabledSides == null) {
            disabledSides = EnumSet.noneOf(RelativeSide.class);
        }
        for (RelativeSide side : sides) {
            disabledSides.add(side);
            sideConfig.put(side, DataType.NONE);
        }
    }

    public boolean isSideEnabled(@Nonnull RelativeSide side) {
        if (disabledSides == null) {
            return true;
        }
        return !disabledSides.contains(side);
    }

    @Nonnull
    public DataType getDataType(@Nonnull RelativeSide side) {
        return sideConfig.get(side);
    }

    public void setDataType(@Nonnull DataType dataType, @Nonnull RelativeSide... sides) {
        for (RelativeSide side : sides) {
            if (isSideEnabled(side)) {
                sideConfig.put(side, dataType);
            }
        }
    }

    @Nonnull
    public Set<DataType> getSupportedDataTypes() {
        Set<DataType> dataTypes = EnumSet.of(DataType.NONE);
        dataTypes.addAll(slotInfo.keySet());
        return dataTypes;
    }

    public void fill(@Nonnull DataType dataType) {
        for (RelativeSide side : EnumUtils.SIDES) {
            setDataType(dataType, side);
        }
    }

    @Nullable
    public ISlotInfo getSlotInfo(@Nonnull RelativeSide side) {
        return getSlotInfo(getDataType(side));
    }

    @Nullable
    public ISlotInfo getSlotInfo(@Nonnull DataType dataType) {
        return slotInfo.get(dataType);
    }

    public List<DataType> getDataTypeForContainer(Object container) {
        return containerTypeMapping.getOrDefault(container, new ArrayList<>());
    }

    public void setDefaults() {
        if (slotInfo.containsKey(DataType.INPUT)) {
            fill(DataType.INPUT);
        }
    }

    public Set<Direction> getSidesForData(@Nonnull DataType dataType) {
        return getSides(type -> type == dataType);
    }

    public Set<Direction> getSides(Predicate<DataType> predicate) {
        Direction facing = facingSupplier.get();
        Set<Direction> directions = null;
        for (Map.Entry<RelativeSide, DataType> entry : sideConfig.entrySet()) {
            if (predicate.test(entry.getValue())) {
                if (directions == null) {
                    //Lazy init the set so that if there are none that match we can just use an empty set
                    // instead of having to initialize an enum set
                    directions = EnumSet.noneOf(Direction.class);
                }
                directions.add(entry.getKey().getDirection(facing));
            }
        }
        return directions == null ? Collections.emptySet() : directions;
    }

    public Set<Direction> getSidesForOutput(DataType outputType) {
        return getSides(type -> type == outputType || type == DataType.INPUT_OUTPUT);
    }

    /**
     * @return The new data type
     */
    @Nonnull
    public DataType incrementDataType(@Nonnull RelativeSide relativeSide) {//TODO - 10.1: Check if changed on things that use this?
        DataType current = getDataType(relativeSide);
        if (isSideEnabled(relativeSide)) {
            Set<DataType> supportedDataTypes = getSupportedDataTypes();
            DataType newType = current.getNext(supportedDataTypes::contains);
            sideConfig.put(relativeSide, newType);
            return newType;
        }
        return current;
    }

    /**
     * @return The new data type
     */
    @Nonnull
    public DataType decrementDataType(@Nonnull RelativeSide relativeSide) {//TODO - 10.1: Check if changed on things that use this?
        DataType current = getDataType(relativeSide);
        if (isSideEnabled(relativeSide)) {
            Set<DataType> supportedDataTypes = getSupportedDataTypes();
            DataType newType = current.getPrevious(supportedDataTypes::contains);
            sideConfig.put(relativeSide, newType);
            return newType;
        }
        return current;
    }

    public void addSlotInfo(@Nonnull DataType dataType, @Nonnull ISlotInfo info) {
        slotInfo.put(dataType, info);
    }
}
