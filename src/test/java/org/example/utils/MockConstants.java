package org.example.utils;

import java.util.Random;

public abstract class MockConstants {
    public static final int MAX_RANDOM_NUMBER = 100;
    public static final Random RANDOM = new Random();
    public static final String COURSE_TITLE = "Course title No.";
    public static final String PROFESSOR_NAME = "Professor name No.";
    public static final String PROFESSOR_SURNAME = "Professor surname  No.";
    public static final String PROFESSOR_EMAIL = "Professor No.%d email  No.";
    public static final String RESPONSE = "response No.";
    public static final String REVIEW = "review No.";
    public static final int MARK = 5;
    public static final String STUDENT_NAME = "Student No.";
    public static final String STUDENT_SURNAME = "Student surname No.";
    public static final String STUDENT_EMAIL = "Student No.%d email  No.";
    public static final String TASK_TITLE = "Task title No.";
    public static final String TASK_DESCRIPTION = "Task description No.";
    public static final int MAX_LIST_SIZE = 5;
    public static final String UPDATE = "updated";
    public static final String DELETE_ALL_COURSES = "delete from courses";
    public static final String DELETE_ALL_PROFESSORS = "delete from professors";
    public static final String DELETE_ALL_SOLUTIONS = "delete from solutions";
    public static final String DELETE_ALL_STUDENTS = "delete from students";
    public static final String DELETE_ALL_TASK = "delete from tasks";
    public static final String GET_AMOUNT_OF_STUDENTS_QUERY = "select count(*) from students";
    public static final String GET_AMOUNT_OF_STUDENTS_BY_COURSE_QUERY = "select count(*) from students_courses where course_id = ?1";
}
