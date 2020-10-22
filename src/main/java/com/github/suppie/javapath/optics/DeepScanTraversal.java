package com.github.suppie.javapath.optics;

import com.github.suppie.javapath.optics.contracts.IndefiniteTraversal;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Stream;

/**
 * Provides way to retrieve multiple objects from given one
 */
@Slf4j
public class DeepScanTraversal extends IndefiniteTraversal {
    public DeepScanTraversal(String fieldName) {
        super(fieldName);
    }

    /**
     * Applies this function to the given argument.
     *
     * @param target the function argument
     * @return the function result
     */
    @Override
    public Stream<Object> apply(Object target) {
        Set<Object> visited = new HashSet<>();
        List<Object> result = new ArrayList<>();
        log.debug("Initiated read for {}", target);
        walkObject(target, result, visited);
        return result.stream();
    }

    /**
     * Traverse object in recursive fashion, depth-first
     *
     * @param target  to walk around
     * @param result  to gather values in
     * @param visited objects throughout walk
     */
    private void walkObject(Object target, List<Object> result, Set<Object> visited) {
        if (visited.contains(target)) {
            return;
        } else {
            visited.add(target);
        }

        walkFields(target, result, visited);
        walkIterable(target, result, visited);
    }

    /**
     * Iterate over object declared fields
     *
     * @param target  to walk around
     * @param result  to gather values in
     * @param visited objects throughout walk
     */
    private void walkFields(Object target, List<Object> result, Set<Object> visited) {
        for (Field field : target.getClass().getDeclaredFields()) {
            FieldGetter.tryRead(target, field.getName()).ifSuccess(fieldData -> {
                if (field.getName().equals(fieldName)) {
                    result.add(fieldData);
                } else {
                    if (fieldData.getClass().isArray()) {
                        log.debug("Initiated read for array {}", fieldData);
                        CollectionTraversal.consumeArray(fieldData, item -> walkObject(item, result, visited));
                    } else {
                        log.debug("Initiated read for field {}", fieldData);
                        walkObject(fieldData, result, visited);
                    }
                }
            });
        }
    }

    /**
     * Iterate over declared methods looking for iterators / arrays return types
     *
     * @param target  to walk around
     * @param result  to gather values in
     * @param visited objects throughout walk
     */
    @SuppressWarnings("unchecked")
    private void walkIterable(Object target, List<Object> result, Set<Object> visited) {
        CollectionTraversal.consumeIterator(target, item -> {
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
        });
    }
}
