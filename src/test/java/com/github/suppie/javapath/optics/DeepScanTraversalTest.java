package com.github.suppie.javapath.optics;

import com.fasterxml.jackson.databind.node.TextNode;
import com.github.suppie.javapath.TestData;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

class DeepScanTraversalTest {
    @Test
    void testDeepScanTraversal_json() {
        DeepScanTraversal deepScanTraversal = new DeepScanTraversal(TestData.AUTHOR_FIELD);
        List<Object> objects = deepScanTraversal.apply(TestData.JSON_OBJECT).collect(Collectors.toList());

        assertEquals(TestData.EXPECTED_AUTHORS.size(), objects.size());
        assertThat(objects).allMatch(o -> o instanceof TextNode);

        List<String> strings = objects.stream().map(o -> ((TextNode) o).textValue()).collect(Collectors.toList());

        assertThat(strings).isEqualTo(TestData.EXPECTED_AUTHORS);
    }

    @Test
    void testDeepScanTraversal_pojo() {
        DeepScanTraversal deepScanTraversal = new DeepScanTraversal(TestData.AUTHOR_FIELD);
        List<Object> objects = deepScanTraversal.apply(TestData.POJO_OBJECT).collect(Collectors.toList());

        assertEquals(TestData.EXPECTED_AUTHORS.size(), objects.size());
        assertThat(objects).isEqualTo(TestData.EXPECTED_AUTHORS);
    }

    @Test
    void testDeepScanTraversal_pojoWithArray() {
        DeepScanTraversal deepScanTraversal = new DeepScanTraversal(TestData.AUTHOR_FIELD);
        List<Object> objects = deepScanTraversal.apply(TestData.POJO_OBJECT_WITH_ARRAY).collect(Collectors.toList());

        assertEquals(TestData.EXPECTED_AUTHORS.size(), objects.size());
        assertThat(objects).isEqualTo(TestData.EXPECTED_AUTHORS);
    }
}
