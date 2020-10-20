package com.github.suppie.javapath;

public final class TestData {
    // The authors of all books
    public static final String AUTHORS_OF_ALL_BOOKS = "$.store.book[*].author";
    // All authors
    public static final String ALL_AUTHORS = "$..author";
    // All things, both books and bicycles
    public static final String ALL_THINGS = "$.store.*";
    // The price of everything
    public static final String PRICES = "$.store..price";
    // The third book
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

    private TestData() {
        // No instance
    }
}
