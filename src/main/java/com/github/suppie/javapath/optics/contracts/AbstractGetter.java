package com.github.suppie.javapath.optics.contracts;

import com.github.suppie.javapath.reflection.DeclaredItemsReader;

import java.util.function.Function;

/**
 * An abstract Getter is an optic that can focus into a structure and get its focus.
 * <p>
 * It can be seen as a wrapper of a get function (S) -> A that can be composed with other optics.
 * <p>
 * Use reflection only, meta factory won't cut it here
 */
public abstract class AbstractGetter<T, R> implements Function<T, R>, DeclaredItemsReader {
    protected final String fieldName;

    protected AbstractGetter(String fieldName) {
        this.fieldName = fieldName;
    }

    public abstract boolean isDefinite();
}
