package org.example.utils;

import java.util.Arrays;
import java.util.List;

public final class Printer {

    private Printer() {
    }

    public static <T> void printObjects(String message, List<T> objects) {
        System.out.println(message);
        objects.forEach(System.out::println);
    }

    @SafeVarargs
    public static <T> void printObjects(String message, T... object) {
        List<T> objectList = Arrays.asList(object);
        System.out.println(message);
        objectList.forEach(System.out::println);
    }
}
