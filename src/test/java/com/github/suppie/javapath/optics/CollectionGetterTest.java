package com.github.suppie.javapath.optics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.suppie.javapath.exceptions.PathException;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CollectionGetterTest {
    private static final String VALUE = "test";

    private static final String[] ARRAY;
    private static final List<String> LIST;
    private static final Map<String, String> MAP;
    private static final JsonNode JSON_NODE;

    static {
        ARRAY = new String[]{VALUE};

        LIST = new ArrayList<>();
        LIST.add(VALUE);

        MAP = new HashMap<>();
        MAP.put(VALUE, VALUE);

        JSON_NODE = new ObjectMapper().valueToTree(MAP);
    }

    @Test
    void testCollectionGetter_forArrays() {
        CollectionGetter collectionGetter = new CollectionGetter(null, "0");

        assertEquals(
                VALUE,
                assertDoesNotThrow(() -> collectionGetter.apply(ARRAY), "Array elements must be accessible"),
                "Accessed value of array must be equal to the expected"
        );
        assertEquals(
                VALUE,
                assertDoesNotThrow(() -> collectionGetter.apply(LIST), "List elements must be accessible"),
                "Accessed value of list must be equal to the expected"
        );
    }

    @Test
    void testCollectionGetter_forMaps() {
        CollectionGetter collectionGetter = new CollectionGetter(null, VALUE);

        assertEquals(
                VALUE,
                assertDoesNotThrow(() -> collectionGetter.apply(MAP), "Map elements must be accessible"),
                "Accessed value of map must be equal to the expected"
        );
        assertEquals(
                JSON_NODE.get(VALUE),
                assertDoesNotThrow(() -> collectionGetter.apply(JSON_NODE), "Map-like object elements must be accessible"),
                "Accessed value of map-like object must be equal to the expected"
        );
    }

    @Test
    void testCollectionGetter_incorrectMapLikeObjects() {
        CollectionGetter collectionGetter = new CollectionGetter(null, VALUE);

        ThrowingMapLikeObject throwingMapLikeObject = new ThrowingMapLikeObject();
        PackageDefaultMapLikeObject packageDefaultMapLikeObject = new PackageDefaultMapLikeObject();
        ProtectedMapLikeObject protectedMapLikeObject = new ProtectedMapLikeObject();
        PrivateMapLikeObject privateMapLikeObject = new PrivateMapLikeObject();

        assertThrows(PathException.class, () -> collectionGetter.apply(throwingMapLikeObject), "Map-like object throwing exception on invocation must fail");
        assertThrows(PathException.class, () -> collectionGetter.apply(packageDefaultMapLikeObject), "Map-like object with package default method must fail");
        assertThrows(PathException.class, () -> collectionGetter.apply(protectedMapLikeObject), "Map-like object with protected method must fail");
        assertThrows(PathException.class, () -> collectionGetter.apply(privateMapLikeObject), "Map-like object with private method must fail");
    }

    public static class ThrowingMapLikeObject {
        public String get(String key) {
            throw new IllegalStateException();
        }
    }

    public static class PackageDefaultMapLikeObject {
        String get(String key) {
            return key;
        }
    }

    public static class ProtectedMapLikeObject {
        protected String get(String key) {
            return key;
        }
    }

    public static class PrivateMapLikeObject {
        private String get(String key) {
            return key;
        }
    }
}
