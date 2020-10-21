package com.github.suppie.javapath.optics;

import com.github.suppie.java.util.Try;
import com.github.suppie.javapath.optics.contracts.DefiniteGetter;

import static com.github.suppie.javapath.reflection.DeclaredItemsReader.readField;
import static com.github.suppie.javapath.reflection.DeclaredItemsReader.readMethod;
import static com.github.suppie.javapath.utils.StringUtils.capitalize;

/**
 * Provides focus on target fields
 */
public class FieldGetter extends DefiniteGetter {
    private static final String IS_PREFIX = "is";
    private static final String GET_PREFIX = "get";

    public FieldGetter(String fieldName) {
        super(fieldName);
    }

    public static Try<Object> tryRead(Object target, String fieldName) {
        return Try
                // Attempt to read field as public one
                .of(() -> readField(target, fieldName))
                // Attempt to read field as boolean field through getter with 'is' prefix
                .orElseTry(() -> readMethod(target, IS_PREFIX + capitalize(fieldName)))
                // Attempt to read field using getter
                .orElseTry(() -> readMethod(target, GET_PREFIX + capitalize(fieldName)));
    }

    /**
     * Applies this function to the given argument.
     *
     * @param target is a Java object to get field from
     * @return field value
     */
    @Override
    public Object apply(Object target) {
        return tryRead(target, fieldName).get();
    }
}
