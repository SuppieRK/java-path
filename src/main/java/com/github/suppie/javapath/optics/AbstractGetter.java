package com.github.suppie.javapath.optics;

import com.github.suppie.javapath.reflection.DeclaredItemsReader;

import java.util.function.UnaryOperator;

/**
 * An abstract Getter is an optic that can focus into a structure and get its focus.
 * <p>
 * It can be seen as a wrapper of a get function (S) -> A that can be composed with other optics.
 * <p>
 * Use reflection only, meta factory won't cut it here
 */
public abstract class AbstractGetter implements UnaryOperator<Object>, DeclaredItemsReader {
    protected final Object root;
    protected final String fieldName;

    protected AbstractGetter(Object root, String fieldName) {
        this.root = root;
        this.fieldName = fieldName;
    }
}
