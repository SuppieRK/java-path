package com.github.suppie.javapath.optics;

import com.github.suppie.java.util.Try;
import com.github.suppie.javapath.exceptions.PathException;

import java.lang.reflect.Array;

/**
 * Provides focus on array elements
 */
public class CollectionGetter extends AbstractGetter {
    private static final String INTEGER_PARSE_EXCEPTION = "Cannot access array element, %s in not a valid index";
    private static final String COLLECTION_METHOD_NAME = "get";

    public CollectionGetter(Object root, String fieldName) {
        super(root, fieldName);
    }

    /**
     * Applies this function to the given argument.
     *
     * @param target is an array
     * @return array element with index defined by {@link #fieldName}
     */
    @Override
    public Object apply(Object target) {
        return parseIndex()
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

    /**
     * Attempt to parse integer from {@link #fieldName}
     *
     * @return {@link Try.Success} if {@link #fieldName} represents valid index
     */
    private Try<Integer> parseIndex() {
        return Try.of(() -> {
            try {
                return Integer.parseInt(fieldName);
            } catch (NumberFormatException nfe) {
                throw new PathException(String.format(INTEGER_PARSE_EXCEPTION, fieldName), nfe);
            }
        });
    }
}
