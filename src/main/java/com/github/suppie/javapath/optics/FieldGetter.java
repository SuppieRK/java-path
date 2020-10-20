package com.github.suppie.javapath.optics;

import com.github.suppie.java.util.Try;

import static com.github.suppie.javapath.utils.StringUtils.capitalize;

/**
 * Provides focus on target fields
 */
public class FieldGetter extends AbstractGetter {
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";

    public FieldGetter(String fieldName) {
        super(fieldName);
    }

    /**
     * Applies this function to the given argument.
     *
     * @param target is a Java object to get field from
     * @return field value
     */
    @Override
    public Object apply(Object target) {
        return Try
                // Attempt to read field as public one
                .of(() -> readField(target, fieldName))
                // Attempt to read field as boolean field through getter with 'is' prefix
                .orElseTry(() -> readMethod(target, IS_PREFIX + capitalize(fieldName)))
                // Attempt to read field using getter
                .orElseTry(() -> readMethod(target, GET_PREFIX + capitalize(fieldName)))
                .get();
    }
}
