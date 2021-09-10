package com.nuggylib.enchantmentsseer.common.lib;

import com.nuggylib.enchantmentsseer.common.EnchantmentsSeer;
import com.nuggylib.enchantmentsseer.common.inventory.container.sync.dynamic.SyncMapper;
import it.unimi.dsi.fastutil.objects.Object2ObjectArrayMap;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.moddiscovery.ModAnnotation;
import net.minecraftforge.forgespi.language.ModFileScanData;
import org.objectweb.asm.Type;

import javax.annotation.Nullable;
import java.lang.annotation.ElementType;
import java.lang.invoke.MethodType;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class EnchantmentsSeerAnnotationScanner {

    public static void collectScanData() {
        Map<String, Class<?>> classNameCache = new Object2ObjectOpenHashMap<>();
        Map<BaseAnnotationScanner, ScanData> scanners = new Object2ObjectArrayMap<>();
        Map<ElementType, List<ScanData>> elementBasedScanData = new EnumMap<>(ElementType.class);
        addScanningSupport(scanners, elementBasedScanData, SyncMapper.INSTANCE);
        for (ModFileScanData scanData : ModList.get().getAllScanData()) {
            for (ModFileScanData.AnnotationData data : scanData.getAnnotations()) {
                //If the annotation is on a field, and is the sync type
                gatherScanData(elementBasedScanData, classNameCache, data);
            }
        }
        for (Map.Entry<BaseAnnotationScanner, ScanData> entry : scanners.entrySet()) {
            Map<Class<?>, List<ModFileScanData.AnnotationData>> knownClasses = entry.getValue().knownClasses;
            if (!knownClasses.isEmpty()) {
                try {
                    entry.getKey().collectScanData(classNameCache, knownClasses);
                } catch (Throwable throwable) {
                    //Should never really happen unless something goes drastically wrong
                    EnchantmentsSeer.logger.info("Failed to collect scan data", throwable);
                }
            }
        }
    }

    private static void gatherScanData(Map<ElementType, List<ScanData>> elementBasedScanData, Map<String, Class<?>> classNameCache, ModFileScanData.AnnotationData data) {
        ElementType targetType = data.getTargetType();
        List<ScanData> elementScanData = elementBasedScanData.get(targetType);
        if (elementScanData != null) {
            for (ScanData scannerData : elementScanData) {
                for (Type type : scannerData.supportedTypes.get(targetType)) {
                    if (type.equals(data.getAnnotationType())) {
                        Class<?> clazz = getClassForName(classNameCache, data.getClassType().getClassName());
                        if (clazz != null) {
                            //If the class was successfully found, add it to the known classes
                            scannerData.knownClasses.computeIfAbsent(clazz, c -> new ArrayList<>()).add(data);
                        }
                        //Annotations should be unique so if we found one match the other scanners shouldn't match that same annotation data
                        return;
                    }
                }
            }
        }
    }

    private static void addScanningSupport(Map<BaseAnnotationScanner, ScanData> scanners, Map<ElementType, List<ScanData>> elementBasedScanData,
                                           BaseAnnotationScanner... baseScanners) {
        for (BaseAnnotationScanner baseScanner : baseScanners) {
            if (baseScanner.isEnabled()) {
                ScanData scanData = new ScanData(baseScanner);
                scanners.put(baseScanner, scanData);
                for (ElementType elementType : scanData.supportedTypes.keySet()) {
                    elementBasedScanData.computeIfAbsent(elementType, type -> new ArrayList<>()).add(scanData);
                }
            }
        }
    }

    @Nullable
    private static Class<?> getClassForName(Map<String, Class<?>> classNameCache, String className) {
        if (classNameCache.containsKey(className)) {
            //Note: We have to check if it is contained, as we keep track of failed classes as null values
            return classNameCache.get(className);
        }
        Class<?> clazz;
        try {
            clazz = Class.forName(className);
        } catch (ClassNotFoundException e) {
            EnchantmentsSeer.logger.error("Failed to find class '{}'", className);
            clazz = null;
        }
        classNameCache.put(className, clazz);
        return clazz;
    }

    private static class ScanData {

        private final Map<Class<?>, List<ModFileScanData.AnnotationData>> knownClasses = new Object2ObjectOpenHashMap<>();
        private final Map<ElementType, Type[]> supportedTypes;

        public ScanData(BaseAnnotationScanner scanner) {
            supportedTypes = scanner.getSupportedTypes();
        }
    }

    public static abstract class BaseAnnotationScanner {

        protected boolean isEnabled() {
            return true;
        }

        protected abstract Map<ElementType, Type[]> getSupportedTypes();

        protected abstract void collectScanData(Map<String, Class<?>> classNameCache, Map<Class<?>, List<ModFileScanData.AnnotationData>> knownClasses);

        /**
         * Gets the value of an annotation or null if it is not present. Used for getting classes
         */
        @Nullable
        protected static Class<?> getAnnotationValue(Map<String, Class<?>> classNameCache, ModFileScanData.AnnotationData data, String key) {
            Type type = (Type) data.getAnnotationData().get(key);
            return type == null ? null : getClassForName(classNameCache, type.getClassName());
        }

        /**
         * Gets the value of an annotation or falls back to the default if the key isn't present.
         */
        protected static <T> T getAnnotationValue(ModFileScanData.AnnotationData data, String key, T defaultValue) {
            return (T) data.getAnnotationData().getOrDefault(key, defaultValue);
        }

        /**
         * Gets the value of an annotation or falls back to the default if the key isn't present. Enum version
         */
        protected static <T extends Enum<T>> T getAnnotationValue(ModFileScanData.AnnotationData data, String key, T defaultValue) {
            Map<String, Object> annotationData = data.getAnnotationData();
            if (annotationData.containsKey(key)) {
                Object value = annotationData.get(key);
                if (value instanceof ModAnnotation.EnumHolder) {
                    //This should always be an enum holder, but check just in case
                    ModAnnotation.EnumHolder enumHolder = (ModAnnotation.EnumHolder) value;
                    //Note: We ignore the description on the enumHolder, as we can just grab the enum's class
                    // directly from the default value
                    try {
                        return Enum.valueOf(defaultValue.getDeclaringClass(), enumHolder.getValue());
                    } catch (IllegalArgumentException e) {
                        EnchantmentsSeer.logger.error("Could not find enum value of: {}. Defaulting.", enumHolder.getValue());
                    }
                } else {
                    EnchantmentsSeer.logger.warn("Unknown property value for enum should have been an enum holder. Defaulting.");
                }
            }
            return defaultValue;
        }

        /**
         * Gets the value of an annotation or falls back to the default if the key isn't present, or the set value is not valid
         */
        protected static <T> T getAnnotationValue(ModFileScanData.AnnotationData data, String key, T defaultValue, Predicate<T> validator) {
            Map<String, Object> annotationData = data.getAnnotationData();
            if (annotationData.containsKey(key)) {
                T value = (T) annotationData.get(key);
                if (validator.test(value)) {
                    return value;
                }
            }
            return defaultValue;
        }

        /**
         * Gets the value of an annotation or falls back to the default if the key isn't present, or the set value is not valid. Enum version
         */
        protected static <T extends Enum<T>> T getAnnotationValue(ModFileScanData.AnnotationData data, String key, T defaultValue, Predicate<T> validator) {
            Map<String, Object> annotationData = data.getAnnotationData();
            if (annotationData.containsKey(key)) {
                Object value = annotationData.get(key);
                if (value instanceof ModAnnotation.EnumHolder) {
                    //This should always be an enum holder, but check just in case
                    ModAnnotation.EnumHolder enumHolder = (ModAnnotation.EnumHolder) value;
                    //Note: We ignore the description on the enumHolder, as we can just grab the enum's class
                    // directly from the default value
                    try {
                        T returnValue = Enum.valueOf(defaultValue.getDeclaringClass(), enumHolder.getValue());
                        if (validator.test(returnValue)) {
                            return returnValue;
                        }
                    } catch (IllegalArgumentException e) {
                        EnchantmentsSeer.logger.error("Could not find enum value of: {}. Defaulting.", enumHolder.getValue());
                    }
                } else {
                    EnchantmentsSeer.logger.warn("Unknown property value for enum should have been an enum holder. Defaulting.");
                }
            }
            return defaultValue;
        }

        @Nullable
        protected static Field getField(Class<?> annotatedClass, String fieldName) {
            Field field;
            try {
                field = annotatedClass.getDeclaredField(fieldName);
            } catch (NoSuchFieldException e) {
                EnchantmentsSeer.logger.error("Failed to find field '{}' for class '{}'", fieldName, annotatedClass.getSimpleName());
                return null;
            }
            field.setAccessible(true);
            return field;
        }

        @Nullable
        protected static Method getMethod(Class<?> annotatedClass, String methodName, String methodDescriptor) {
            MethodType methodType;
            try {
                methodType = MethodType.fromMethodDescriptorString(methodDescriptor, annotatedClass.getClassLoader());
            } catch (IllegalArgumentException | TypeNotPresentException e) {
                EnchantmentsSeer.logger.error("Failed to generate method type. {}", e.getMessage());
                return null;
            }
            Method method;
            try {
                method = annotatedClass.getDeclaredMethod(methodName, methodType.parameterList().toArray(new Class[0]));
            } catch (NoSuchMethodException e) {
                EnchantmentsSeer.logger.error("Failed to find method '{}' with descriptor '{}' for class '{}'", methodName, methodDescriptor,
                        annotatedClass.getSimpleName());
                return null;
            }
            method.setAccessible(true);
            return method;
        }

        /**
         * Goes up the various parent classes until we find a cache that matches it and returns the reference to it.
         *
         * @apiNote This should only be used really for read only purposes as changing it will then also adjust the parent's data.
         */
        protected static <DATA> DATA getData(Map<Class<?>, DATA> map, Class<?> clazz, DATA empty) {
            Class<?> current = clazz;
            while (current.getSuperclass() != null) {
                current = current.getSuperclass();
                DATA superCache = map.get(current);
                if (superCache != null) {
                    //If we already have an overall cache for the super class, return a reference to it and break out of checking super classes
                    // We don't need to copy it as we only use it for readonly purposes
                    return superCache;
                }
                //Otherwise continue going up to the root superclass
            }
            return empty;
        }

        /**
         * Gathers all info's into a list sorted by class name and adds any info from parent classes to it as well.
         */
        protected static <INFO> List<ClassBasedInfo<INFO>> combineWithParents(Map<Class<?>, List<INFO>> flatMap) {
            Map<Class<?>, List<INFO>> map = new Object2ObjectOpenHashMap<>();
            for (Map.Entry<Class<?>, List<INFO>> entry : flatMap.entrySet()) {
                Class<?> clazz = entry.getKey();
                List<INFO> info = entry.getValue();
                Class<?> current = clazz;
                while (current.getSuperclass() != null) {
                    current = current.getSuperclass();
                    List<INFO> superInfo = map.get(current);
                    if (superInfo != null) {
                        //If we already have an overall cache for the super class, add from it and break out of checking super classes
                        info.addAll(superInfo);
                        break;
                    }
                    //Otherwise continue building up the cache, collecting all the class names up to the root superclass
                    superInfo = flatMap.get(current);
                    if (superInfo != null) {
                        //If the map has the super class, grab the fields that correspond to it
                        //Note: We keep going here as it may have super classes higher up
                        info.addAll(superInfo);
                    }
                }
                map.put(clazz, info);
            }
            return map.entrySet().stream().map(entry -> new ClassBasedInfo<>(entry.getKey(), entry.getValue()))
                    .sorted(Comparator.comparing(info -> info.className)).collect(Collectors.toList());
        }

        protected static class ClassBasedInfo<INFO> {

            public final Class<?> clazz;
            public final String className;
            public final List<INFO> infoList;

            private ClassBasedInfo(Class<?> clazz, List<INFO> infoList) {
                this.clazz = clazz;
                this.className = clazz.getName();
                this.infoList = infoList;
            }
        }
    }

}
