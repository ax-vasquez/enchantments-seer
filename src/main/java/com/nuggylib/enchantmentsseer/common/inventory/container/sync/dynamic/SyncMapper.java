package com.nuggylib.enchantmentsseer.common.inventory.container.sync.dynamic;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;
import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.property.PropertyType;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.ISyncableData;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.SyncableEnum;
import com.nuggylib.enchantmentsseer.common.inventory.container.tile.EnchantmentsSeerTileContainer;
import com.nuggylib.enchantmentsseer.common.lib.EnchantmentsSeerAnnotationScanner.BaseAnnotationScanner;
import com.nuggylib.enchantmentsseer.common.util.LambdaMetaFactoryUtil;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import java.lang.annotation.ElementType;
import java.lang.reflect.Field;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

public class SyncMapper extends BaseAnnotationScanner {

    public static final SyncMapper INSTANCE = new SyncMapper();
    public static final String DEFAULT_TAG = "default";
    private final Map<Class<?>, PropertyDataClassCache> syncablePropertyMap = new Object2ObjectOpenHashMap<>();

    private SyncMapper() {}

    @Override
    protected Map<ElementType, Type[]> getSupportedTypes() {
        return Collections.singletonMap(ElementType.FIELD, new Type[]{Type.getType(ContainerSync.class)});
    }

    @Override
    protected void collectScanData(Map<String, Class<?>> classNameCache, Map<Class<?>, List<ModFileScanData.AnnotationData>> knownClasses) {
        Map<Class<?>, List<PropertyFieldInfo>> rawPropertyMap = new Object2ObjectOpenHashMap<>();
        //Only create the list once for the default fallback
        List<String> fallbackTagsList = Collections.singletonList(DEFAULT_TAG);
        for (Map.Entry<Class<?>, List<ModFileScanData.AnnotationData>> entry : knownClasses.entrySet()) {
            Class<?> annotatedClass = entry.getKey();
            List<PropertyFieldInfo> propertyInfo = new ArrayList<>();
            rawPropertyMap.put(annotatedClass, propertyInfo);
            for (ModFileScanData.AnnotationData data : entry.getValue()) {
                String fieldName = data.getMemberName();
                Field field = getField(annotatedClass, fieldName);
                if (field == null) {
                    continue;
                }
                String getterName = getAnnotationValue(data, "getter", "");
                PropertyField newField;
                try {
                    PropertyType type = PropertyType.getFromType(field.getType());
                    String setterName = getAnnotationValue(data, "setter", "");
                    if (type != null) {
                        newField = new PropertyField(new TrackedFieldData(LambdaMetaFactoryUtil.createGetter(field, annotatedClass, getterName),
                                LambdaMetaFactoryUtil.createSetter(field, annotatedClass, setterName), type));
                    } else if (field.getType().isEnum()) {
                        newField = new PropertyField(new EnumFieldData(LambdaMetaFactoryUtil.createGetter(field, annotatedClass, getterName),
                                LambdaMetaFactoryUtil.createSetter(field, annotatedClass, setterName), field.getType()));
                    } else {
                        EnchantmentsSeer.logger.error("Attempted to sync an invalid field '{}' in class '{}'.", fieldName, annotatedClass.getSimpleName());
                        continue;
                    }
                } catch (Throwable throwable) {
                    EnchantmentsSeer.logger.error("Failed to create sync data for field '{}' in class '{}'.", fieldName, annotatedClass.getSimpleName(), throwable);
                    continue;
                }
                String fullPath = annotatedClass.getName() + "#" + fieldName;
                //If the annotation data has tags add them, and otherwise fallback to the default tag
                for (String tag : getAnnotationValue(data, "tags", fallbackTagsList)) {
                    propertyInfo.add(new PropertyFieldInfo(fullPath, tag, newField));
                }
            }
        }
        List<ClassBasedInfo<PropertyFieldInfo>> propertyMap = combineWithParents(rawPropertyMap);
        for (ClassBasedInfo<PropertyFieldInfo> classPropertyInfo : propertyMap) {
            PropertyDataClassCache cache = new PropertyDataClassCache();
            classPropertyInfo.infoList.sort(Comparator.comparing(info -> info.fieldPath + "|" + info.tag));
            for (PropertyFieldInfo field : classPropertyInfo.infoList) {
                cache.propertyFieldMap.put(field.tag, field.field);
            }
            syncablePropertyMap.put(classPropertyInfo.clazz, cache);
        }
    }

    public void setup(EnchantmentsSeerTileContainer container, Class<?> holderClass, Supplier<Object> holderSupplier) {
        setup(container, holderClass, holderSupplier, DEFAULT_TAG);
    }

    public void setup(EnchantmentsSeerTileContainer container, Class<?> holderClass, Supplier<Object> holderSupplier, String tag) {
        PropertyDataClassCache cache = syncablePropertyMap.computeIfAbsent(holderClass, clazz -> getData(syncablePropertyMap, clazz, PropertyDataClassCache.EMPTY));
        for (PropertyField field : cache.propertyFieldMap.get(tag)) {
            for (TrackedFieldData data : field.trackedData) {
                container.track(data.createSyncableData(holderSupplier));
            }
        }
    }

    private static class PropertyDataClassCache {

        private static final PropertyDataClassCache EMPTY = new PropertyDataClassCache();

        //Note: This needs to be a linked map to ensure that the order is preserved
        private final Multimap<String, PropertyField> propertyFieldMap = LinkedHashMultimap.create();
    }

    private static class PropertyField {

        private final List<TrackedFieldData> trackedData = new ArrayList<>();

        private PropertyField(TrackedFieldData... data) {
            trackedData.addAll(Arrays.asList(data));
        }

        private void addTrackedData(TrackedFieldData data) {
            trackedData.add(data);
        }
    }

    protected static class TrackedFieldData {

        private PropertyType propertyType;
        private final Function<Object, Object> getter;
        private final BiConsumer<Object, Object> setter;

        protected TrackedFieldData(Function<Object, Object> getter, BiConsumer<Object, Object> setter) {
            this.getter = getter;
            this.setter = setter;
        }

        private TrackedFieldData(Function<Object, Object> getter, BiConsumer<Object, Object> setter, PropertyType propertyType) {
            this(getter, setter);
            this.propertyType = propertyType;
        }

        protected Object get(Object dataObj) {
            return getter.apply(dataObj);
        }

        protected void set(Object dataObj, Object value) {
            setter.accept(dataObj, value);
        }

        protected ISyncableData createSyncableData(Supplier<Object> obj) {
            return create(() -> {
                Object dataObj = obj.get();
                return dataObj == null ? getDefault() : get(dataObj);
            }, val -> {
                Object dataObj = obj.get();
                if (dataObj != null) {
                    set(dataObj, val);
                }
            });
        }

        protected ISyncableData create(Supplier<Object> getter, Consumer<Object> setter) {
            return propertyType.create(getter, setter);
        }

        protected Object getDefault() {
            return propertyType.getDefault();
        }

        protected static TrackedFieldData create(Class<?> propertyType, Function<Object, Object> getter, BiConsumer<Object, Object> setter) {
            if (propertyType.isEnum()) {
                return new EnumFieldData(getter, setter, propertyType);
            }
            PropertyType type = PropertyType.getFromType(propertyType);
            if (type == null) {
                EnchantmentsSeer.logger.error("Tried to create property data for invalid type '{}'.", propertyType.getName());
                return null;
            }
            return new TrackedFieldData(getter, setter, type);
        }
    }

    protected static class EnumFieldData extends TrackedFieldData {

        private final Object[] constants;

        private EnumFieldData(Function<Object, Object> getter, BiConsumer<Object, Object> setter, Class<?> enumClass) {
            super(getter, setter);
            constants = enumClass.getEnumConstants();
        }

        @Override
        protected ISyncableData create(Supplier<Object> getter, Consumer<Object> setter) {
            return createData((Enum[]) constants, getter, setter);
        }

        protected <ENUM extends Enum<ENUM>> ISyncableData createData(ENUM[] constants, Supplier<Object> getter, Consumer<Object> setter) {
            return SyncableEnum.create(val -> constants[val], constants[0], () -> (ENUM) getter.get(), setter::accept);
        }

        @Override
        protected Object getDefault() {
            return constants[0];
        }
    }

    private static class PropertyFieldInfo {

        private final PropertyField field;
        private final String fieldPath;
        private final String tag;

        private PropertyFieldInfo(String fieldPath, String tag, PropertyField field) {
            this.fieldPath = fieldPath;
            this.field = field;
            this.tag = tag;
        }
    }
}
