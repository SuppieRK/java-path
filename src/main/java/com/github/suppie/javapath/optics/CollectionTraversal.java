package com.github.suppie.javapath.optics;

import com.github.suppie.java.util.Try;
import com.github.suppie.javapath.optics.contracts.IndefiniteTraversal;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * Provides focus on all array elements by casting object to list type
 */
public class CollectionTraversal extends IndefiniteTraversal {
    private static final String TO_ARRAY_METHOD = "toArray";

    public CollectionTraversal() {
        super("");
    }

    /**
     * Invoke consumer for all Java array items
     *
     * @param target   is an array
     * @param consumer to invoke for array items
     */
    public static void consumeArray(Object target, Consumer<Object> consumer) {
        for (int i = 0; i < Array.getLength(target); i++) {
            consumer.accept(Array.get(target, i));
        }
    }

    /**
     * Invoke consumer for all iterable items found on target
     *
     * @param target   to scan
     * @param deepScan determining whether invoke every iterator found or stick to {@link Iterable} interface
     * @param consumer to invoke
     */
    @SuppressWarnings("unchecked")
    public static void consumeIterator(Object target, boolean deepScan, Consumer<Object> consumer) {
        Class<?> targetClass = target.getClass();

        if (Map.class.isAssignableFrom(targetClass)) {
            if (deepScan) {
                ((Map<Object, Object>) target).entrySet().forEach(consumer);
            } else {
                ((Map<Object, Object>) target).values().forEach(consumer);
            }
            return;
        }

        for (Method method : targetClass.getDeclaredMethods()) {
            Class<?> returnType = method.getReturnType();

            if (returnType.isArray() && !TO_ARRAY_METHOD.equals(method.getName())) {
                Try.of(() -> method.invoke(target)).ifSuccess(array -> consumeArray(array, consumer));
            }

            if (deepScan && Iterator.class.equals(returnType) && method.getParameterCount() == 0) {
                Try.of(() -> (Iterator<Object>) method.invoke(target)).ifSuccess(iterator -> iterator.forEachRemaining(consumer));
            }
        }

        if (!deepScan && isIterable(targetClass)) {
            Try.of(((Iterable<Object>) target)::iterator).ifSuccess(iterator -> iterator.forEachRemaining(consumer));
        }
    }

    /**
     * Check if class inherits {@link Iterable} interface
     *
     * @param clazz to check
     * @return true, if class is inherited {@link Iterable}
     */
    private static boolean isIterable(Class<?> clazz) {
        for (Class<?> inherited : clazz.getInterfaces()) {
            if (inherited.equals(Iterable.class)) {
                return true;
            }
        }

        if (clazz.equals(Object.class)) {
            return false;
        } else {
            return isIterable(clazz.getSuperclass()) || Arrays.stream(clazz.getInterfaces()).anyMatch(CollectionTraversal::isIterable);
        }
    }

    /**
     * Applies this function to the given argument.
     *
     * @param target the function argument
     * @return the function result
     */
    @Override
    public Stream<Object> apply(Object target) {
        List<Object> result = new ArrayList<>();

        if (target.getClass().isArray()) {
            consumeArray(target, result::add);
        } else {
            consumeIterator(target, false, result::add);
        }

        return result.stream();
    }
}
