package com.github.suppie.javapath.optics;

import com.github.suppie.javapath.TestData;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

class DeepScanGetterTest {
    @Test
    void name() {
        DeepScanGetter deepScanGetter = new DeepScanGetter("author");
        List<Object> objects = deepScanGetter.apply(TestData.JSON_OBJECT);
        System.out.println(objects);
        Assertions.assertNotEquals(0, objects.size());
    }

    @Test
    void name2() {
        DeepScanGetter deepScanGetter = new DeepScanGetter("author");
        List<Object> objects = deepScanGetter.apply(TestData.POJO_OBJECT);
        System.out.println(objects);
        Assertions.assertNotEquals(0, objects.size());
    }

    @Test
    void name3() {
        DeepScanGetter deepScanGetter = new DeepScanGetter("author");
        List<Object> objects = deepScanGetter.apply(TestData.POJO_OBJECT_WITH_ARRAY);
        System.out.println(objects);
        Assertions.assertNotEquals(0, objects.size());
    }
}
