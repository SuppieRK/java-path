package com.github.suppie.javapath;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

public final class TestData {
    // The authors of all books
    // getStore().getBook().forEach(it -> it.getAuthor())
    public static final String AUTHORS_OF_ALL_BOOKS = "$.store.book[*].author";
    // All authors
    // deepScan().forEach(it -> it.getAuthor()).collectIfNotNull()
    public static final String ALL_AUTHORS = "$..author";
    // All things, both books and bicycles
    // getStore()
    public static final String ALL_THINGS = "$.store.*";
    // The price of everything
    // getStore().forEach(it1 -> it1.deepScan().forEach(it2 -> it2.getPrice())).collectIfNotNull()
    public static final String PRICES = "$.store..price";
    // The third book
    // deepScan().forEach()
    public static final String THIRD_BOOK = "$..book[2]";
    // The second to last book
    public static final String SECOND_TO_LAST_BOOK = "$..book[-2]";
    // The first two books
    public static final String FIRST_TWO_BOOKS = "$..book[0,1]";
    // All books from index 0 (inclusive) until index 2 (exclusive)
    public static final String BOOKS_UNTIL_THIRD = "$..book[:2]";
    // All books from index 1 (inclusive) until index 2 (exclusive)
    public static final String BOOKS_FROM_SECOND_TO_THIRD = "$..book[1:2]";
    // Last two books
    public static final String LAST_TWO_BOOKS = "$..book[-2:]";
    // Book number two from tail
    public static final String SECOND_FROM_LAST_BOOK = "$..book[2:]";
    // All books with an ISBN number
    public static final String BOOKS_WITH_ISBN = "$..book[?(@.isbn)]";
    // All books in store cheaper than 10
    public static final String BOOKS_CHEAPER_THAN_TEN = "$.store.book[?(@.price < 10)]";
    // All books in store that are not "expensive"
    public static final String NOT_EXPENSIVE_BOOKS = "$..book[?(@.price <= $['expensive'])]";
    // All books matching regex (ignore case)
    public static final String BOOKS_MATCHING_REGEX = "$..book[?(@.author =~ /.*REES/i)]";
    // Give me every thing
    public static final String EVERYTHING = "$..*";
    // The number of books
    public static final String NUMBER_OF_BOOKS = "$..book.length()";

    public static final String AUTHOR_FIELD = "author";
    public static final List<String> EXPECTED_AUTHORS = new ArrayList<String>() {{
        add("Nigel Rees");
        add("Evelyn Waugh");
        add("Herman Melville");
        add("J. R. R. Tolkien");
    }};

    //language=JSON
    public static final String JSON = "{\n" +
            "  \"store\": {\n" +
            "    \"book\": [\n" +
            "      {\n" +
            "        \"category\": \"reference\",\n" +
            "        \"author\": \"Nigel Rees\",\n" +
            "        \"title\": \"Sayings of the Century\",\n" +
            "        \"price\": 8.95\n" +
            "      },\n" +
            "      {\n" +
            "        \"category\": \"fiction\",\n" +
            "        \"author\": \"Evelyn Waugh\",\n" +
            "        \"title\": \"Sword of Honour\",\n" +
            "        \"price\": 12.99\n" +
            "      },\n" +
            "      {\n" +
            "        \"category\": \"fiction\",\n" +
            "        \"author\": \"Herman Melville\",\n" +
            "        \"title\": \"Moby Dick\",\n" +
            "        \"isbn\": \"0-553-21311-3\",\n" +
            "        \"price\": 8.99\n" +
            "      },\n" +
            "      {\n" +
            "        \"category\": \"fiction\",\n" +
            "        \"author\": \"J. R. R. Tolkien\",\n" +
            "        \"title\": \"The Lord of the Rings\",\n" +
            "        \"isbn\": \"0-395-19395-8\",\n" +
            "        \"price\": 22.99\n" +
            "      }\n" +
            "    ],\n" +
            "    \"bicycle\": {\n" +
            "      \"color\": \"red\",\n" +
            "      \"price\": 19.95\n" +
            "    }\n" +
            "  },\n" +
            "  \"expensive\": 10\n" +
            "}";

    public static final JsonNode JSON_OBJECT;
    public static final Warehouse POJO_OBJECT;
    public static final WarehouseArray POJO_OBJECT_WITH_ARRAY;

    static {
        ObjectMapper MAPPER = new ObjectMapper();

        JsonNode JSON_OBJECT_TMP;
        try {
            JSON_OBJECT_TMP = MAPPER.readTree(JSON);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            JSON_OBJECT_TMP = null;
        }
        JSON_OBJECT = JSON_OBJECT_TMP;

        Warehouse POJO_OBJECT_TMP;
        try {
            POJO_OBJECT_TMP = MAPPER.readValue(JSON, Warehouse.class);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            POJO_OBJECT_TMP = null;
        }
        POJO_OBJECT = POJO_OBJECT_TMP;

        WarehouseArray POJO_OBJECT_WITH_ARRAY_TMP;
        if (POJO_OBJECT_TMP == null) {
            POJO_OBJECT_WITH_ARRAY_TMP = null;
        } else {
            StoreArray store = new StoreArray();
            store.setBicycle(POJO_OBJECT.getStore().getBicycle());
            store.setBook(POJO_OBJECT.getStore().getBook().toArray(new Book[0]));

            POJO_OBJECT_WITH_ARRAY_TMP = new WarehouseArray();
            POJO_OBJECT_WITH_ARRAY_TMP.setExpensive(POJO_OBJECT.getExpensive());
            POJO_OBJECT_WITH_ARRAY_TMP.setStore(store);
        }
        POJO_OBJECT_WITH_ARRAY = POJO_OBJECT_WITH_ARRAY_TMP;
    }

    private TestData() {
        // No instance
    }

    @Data
    public static class Book {
        private String category;
        private String author;
        private String title;
        private String isbn;
        private Double price;
    }

    @Data
    public static class Bicycle {
        private String color;
        private Double price;
    }

    @Data
    public static class Store {
        private List<Book> book;
        private Bicycle bicycle;
    }

    @Data
    public static class Warehouse {
        private Store store;
        private Integer expensive;
    }

    @Data
    public static class StoreArray {
        private Book[] book;
        private Bicycle bicycle;
    }

    @Data
    public static class WarehouseArray {
        private StoreArray store;
        private Integer expensive;
    }
}
