package com.github.suppie.javapath.optics;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.TextNode;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

class CollectionTraversalTest {
    private static final String[] ARRAY = new String[]{"first", "second", "third"};
    private static final List<String> LIST;
    private static final Set<String> SET;
    private static final Map<String, String> MAP;
    private static final JsonNode LIST_JSON_NODE;
    private static final JsonNode MAP_JSON_NODE;

    static {
        LIST = new ArrayList<>();
        SET = new HashSet<>();
        MAP = new HashMap<>();

        for (String item : ARRAY) {
            LIST.add(item);
            SET.add(item);
            MAP.put(item, item);
        }

        LIST_JSON_NODE = new ObjectMapper().valueToTree(LIST);
        MAP_JSON_NODE = new ObjectMapper().valueToTree(MAP);
    }

    private final CollectionTraversal collectionTraversal = new CollectionTraversal();

    @Test
    void testCollectionTraversal_array() {
        List<Object> list = collectionTraversal.apply(ARRAY).collect(Collectors.toList());
        assertThat(list).isNotEmpty().containsExactlyElementsOf(LIST);
    }

    @Test
    void testCollectionTraversal_set() {
        List<Object> list = collectionTraversal.apply(SET).collect(Collectors.toList());
        assertThat(list).isNotEmpty().containsExactlyInAnyOrderElementsOf(LIST);
    }

    @Test
    void testCollectionTraversal_list() {
        List<Object> list = collectionTraversal.apply(LIST).collect(Collectors.toList());
        assertThat(list).isNotEmpty().containsExactlyElementsOf(LIST);
    }

    @Test
    void testCollectionTraversal_map() {
        List<Object> list = collectionTraversal.apply(MAP).collect(Collectors.toList());
        assertThat(list).isNotEmpty().containsExactlyInAnyOrderElementsOf(LIST);
    }

    @Test
    void testCollectionTraversal_listJsonNode() {
        List<Object> list = collectionTraversal.apply(LIST_JSON_NODE).collect(Collectors.toList());
        assertThat(list).isNotEmpty().allMatch(o -> o instanceof TextNode);

        List<String> strings = list.stream().map(o -> ((TextNode) o).textValue()).collect(Collectors.toList());

        assertThat(strings).isNotEmpty().containsExactlyInAnyOrderElementsOf(LIST);
    }

    @Test
    void testCollectionTraversal_mapJsonNode() {
        List<Object> list = collectionTraversal.apply(MAP_JSON_NODE).collect(Collectors.toList());
        assertThat(list).isNotEmpty().allMatch(o -> o instanceof TextNode);

        List<String> strings = list.stream().map(o -> ((TextNode) o).textValue()).collect(Collectors.toList());

        assertThat(strings).isNotEmpty().containsExactlyInAnyOrderElementsOf(LIST);
    }
}
