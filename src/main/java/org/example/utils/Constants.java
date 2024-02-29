package org.example.utils;

import java.util.Random;

public final class Constants {
    public static final Random RANDOM = new Random();
    public static final int LIST_SIZE = 6;
    public static final String RANDOM_TITLE = "Title No.%d";
    public static final String RANDOM_NAME = "Name No.%d";
    public static final String RANDOM_SURNAME = "Surname  No.%d";
    public static final String RANDOM_EMAIL = "email@mail No.%d";
    public static final String RANDOM_DESCRIPTION = "Description No.%d";
    public static final String RANDOM_RESPONSE = "Response No.%d";
    public static final String RANDOM_REVIEW = "Review No.%d";
    public static final String OBJECT_DELETED = "Object %s was deleted: %b";
    public static final int MARK = 5;
    public static final String SAVE_MESSAGE = "Object saved successfully: %s";
    public static final String SAVE_FAILED_MESSAGE = "Object saving failed: %s, cause: ";
    public static final String DELETE_FAILED_MESSAGE = "Object %s was not deleted, cause: ";
    public static final String UPDATE_MESSAGE = "Object updated successfully: %s";
    public static final String UPDATE_FAILED_MESSAGE = "Object update failed: %s, cause: ";
    public static final String DELETE_MESSAGE = "Object %s deleted successfully";
    public static final String MANAGER_CLOSED_MESSAGE = "EntityManager closed: %s";
    public static final String MANAGER_OPENED_MESSAGE = "EntityManager opened: %s";
    public static final String ENTITY_NOT_FOUND_MESSAGE = "Entity with id = %d not found";
    public static final String OBJECT_CREATED_MESSAGE = "Object created: %s";
    public static final String OBJECT_UPDATED_MESSAGE = "Object updated: %s";
    public static final String STUDENT_CHECK_IN_COURSE_MESSAGE = "Student %s check in course %s";
    public static final String STUDENT_CHECK_OUT_COURSE_MESSAGE = "Student %s check out course %s";
    public static final String SOLUTION_RESOLVED_MESSAGE = "Solution %s is resolved";

    private Constants() {
    }
}
