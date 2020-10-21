package com.github.suppie.javapath.optics.contracts;

import java.util.List;

public abstract class IndefiniteGetter extends AbstractGetter<Object, List<Object>> {
    protected IndefiniteGetter(String fieldName) {
        super(fieldName);
    }

    @Override
    public boolean isDefinite() {
        return false;
    }
}
