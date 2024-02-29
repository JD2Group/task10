package org.example.utils;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.utils.Constants.*;

public abstract class Generator {

    public static List<String> generateStrings(String pattern) {
        return IntStream.range(0, LIST_SIZE)
                .mapToObj(i -> String.format(pattern, RANDOM.nextInt()))
                .collect(Collectors.toList());
    }


    public static String generateTitle() {
        return String.format(RANDOM_TITLE, RANDOM.nextInt());
    }

    public static String generateName() {
        return String.format(RANDOM_NAME, RANDOM.nextInt());
    }

    public static String generateSurname() {
        return String.format(RANDOM_SURNAME, RANDOM.nextInt());
    }

    public static String generateEmail() {
        return String.format(RANDOM_EMAIL, RANDOM.nextInt());
    }

    public static String generateDescription() {
        return String.format(RANDOM_DESCRIPTION, RANDOM.nextInt());
    }

    public static String generateResponse() {
        return String.format(RANDOM_RESPONSE, RANDOM.nextInt());
    }

    public static String generateReview() {
        return String.format(RANDOM_REVIEW, RANDOM.nextInt());
    }

}
