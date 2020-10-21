package com.github.suppie.javapath.optics.contracts;

public abstract class DefiniteGetter extends AbstractGetter<Object, Object> {
    protected DefiniteGetter(String fieldName) {
        super(fieldName);
    }

    @Override
    public boolean isDefinite() {
        return true;
    }
}
