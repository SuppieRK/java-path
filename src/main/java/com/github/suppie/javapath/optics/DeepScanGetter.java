package com.github.suppie.javapath.optics;

import com.github.suppie.java.util.Try;
import com.github.suppie.javapath.optics.contracts.IndefiniteGetter;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Slf4j
public class DeepScanGetter extends IndefiniteGetter {
    public DeepScanGetter(String fieldName) {
        super(fieldName);
    }

    /**
     * Applies this function to the given argument.
     *
     * @param target the function argument
     * @return the function result
     */
    @Override
    public List<Object> apply(Object target) {
        Set<Object> visited = new HashSet<>();
        List<Object> result = new ArrayList<>();
        log.debug("Initiated read for {}", target);
        walkObject(target, result, visited);
        return result;
    }

    private void walkObject(Object target, List<Object> result, Set<Object> visited) {
        if (visited.contains(target)) {
            return;
        } else {
            visited.add(target);
        }

        walkFields(target, result, visited);
        walkMethods(target, result, visited);
    }

    private void walkFields(Object target, List<Object> result, Set<Object> visited) {
        for (Field field : target.getClass().getDeclaredFields()) {
            FieldGetter.tryRead(target, field.getName()).ifSuccess(fieldData -> {
                if (field.getName().equals(fieldName)) {
                    result.add(fieldData);
                } else {
                    if (fieldData.getClass().isArray()) {
                        walkArray(fieldData, result, visited);
                    } else {
                        log.debug("Initiated read for field {}", fieldData);
                        walkObject(fieldData, result, visited);
                    }
                }
            });
        }
    }

    private void walkArray(Object target, List<Object> result, Set<Object> visited) {
        for (int i = 0; i < Array.getLength(target); i++) {
            Object arrayElement = Array.get(target, i);
            log.debug("Initiated read for array element {}", arrayElement);
            walkObject(arrayElement, result, visited);
        }
    }

    @SuppressWarnings("unchecked")
    private void walkMethods(Object target, List<Object> result, Set<Object> visited) {
        for (Method declaredMethod : target.getClass().getDeclaredMethods()) {
            if (Iterator.class.isAssignableFrom(declaredMethod.getReturnType()) && declaredMethod.getParameterCount() == 0) {
                Try.of(() -> (Iterator<Object>) declaredMethod.invoke(target))
                        .ifSuccess(iterator -> iterator.forEachRemaining(item -> walkItem(item, result, visited)));
            } else if (declaredMethod.getReturnType().isArray()) {
                Try.of(() -> declaredMethod.invoke(target))
                        .ifSuccess(objects -> walkArray(objects, result, visited));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private void walkItem(Object item, List<Object> result, Set<Object> visited) {
        if (Map.Entry.class.isAssignableFrom(item.getClass())) {
            Map.Entry<Object, Object> entry = (Map.Entry<Object, Object>) item;
            if (entry.getKey().equals(fieldName)) {
                result.add(entry.getValue());
            } else {
                log.debug("Initiated read for Map.Entry value {}", entry.getValue());
                walkObject(entry.getValue(), result, visited);
            }
        } else {
            log.debug("Initiated read for Iterator object {}", item);
            walkObject(item, result, visited);
        }
    }
}
