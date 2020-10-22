package com.github.suppie.javapath.optics.contracts;

import java.util.stream.Stream;

public abstract class IndefiniteTraversal extends AbstractOptic<Object, Stream<Object>> {
    protected IndefiniteTraversal(String fieldName) {
        super(fieldName);
    }

    @Override
    public boolean isDefinite() {
        return false;
    }
}
