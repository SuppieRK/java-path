package com.github.suppie.javapath.optics;

import com.github.suppie.java.util.Try;
import com.github.suppie.javapath.optics.contracts.DefiniteGetter;

import java.lang.reflect.Array;

import static com.github.suppie.javapath.reflection.DeclaredItemsReader.readMethodWithSingleParameter;

/**
 * Provides focus on array elements
 */
public class CollectionGetter extends DefiniteGetter {
    private static final String COLLECTION_METHOD_NAME = "get";

    public CollectionGetter(String fieldName) {
        super(fieldName);
    }

    /**
     * Applies this function to the given argument.
     *
     * @param target is an array
     * @return array element with index defined by {@link #fieldName}
     */
    @Override
    public Object apply(Object target) {
        return Try.of(() -> Integer.parseInt(fieldName))
                .flatMap(index -> {
                    if (target.getClass().isArray()) {
                        return Try.of(() -> Array.get(target, index));
                    } else {
                        return Try.of(() -> readMethodWithSingleParameter(target, COLLECTION_METHOD_NAME, Integer.TYPE, index));
                    }
                })
                .orElseTry(() -> readMethodWithSingleParameter(target, COLLECTION_METHOD_NAME, Object.class, fieldName))
                .orElseTry(() -> readMethodWithSingleParameter(target, COLLECTION_METHOD_NAME, String.class, fieldName))
                .get();
    }
}
