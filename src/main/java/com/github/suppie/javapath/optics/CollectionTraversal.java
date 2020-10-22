package com.github.suppie.javapath.optics;

import com.github.suppie.java.util.Try;
import com.github.suppie.javapath.optics.contracts.IndefiniteTraversal;

import java.lang.reflect.Array;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
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

    public static void consumeArray(Object target, Consumer<Object> consumer) {
        for (int i = 0; i < Array.getLength(target); i++) {
            consumer.accept(Array.get(target, i));
        }
    }

    @SuppressWarnings("unchecked")
    public static void consumeIterator(Object target, Consumer<Object> consumer) {
        if (Map.class.isAssignableFrom(target.getClass())) {
            ((Map<Object, Object>) target).entrySet().forEach(consumer);
            return;
        }

        for (Method method : target.getClass().getDeclaredMethods()) {
            if (TO_ARRAY_METHOD.equals(method.getName())) continue;

            Class<?> returnType = method.getReturnType();

            if (returnType.isArray()) {
                Try.of(() -> method.invoke(target)).ifSuccess(array -> consumeArray(array, consumer));
            }

            if (Iterator.class.equals(returnType) && method.getParameterCount() == 0) {
                ParameterizedType genericReturnType = (ParameterizedType) method.getGenericReturnType();
                Type actualTypeArgument = genericReturnType.getActualTypeArguments()[0];

                Try.of(() -> (Iterator<Object>) method.invoke(target)).ifSuccess(iterator -> iterator.forEachRemaining(consumer));
            }
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
            consumeIterator(target, result::add);
        }

        return result.stream();
    }
}
