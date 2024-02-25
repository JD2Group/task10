package org.example.utils;

import java.util.Random;

public abstract class MockConstants {
    public static final int MAX_RANDOM_NUMBER = 100;
    public static final Random RANDOM = new Random();
    public static final String COURSE_TITLE_PATTERN = "Course title No.";
    public static final String PROFESSOR_NAME_PATTERN = "Professor name No.";
    public static final String PROFESSOR_SURNAME_PATTERN = "Professor surname  No.";
    public static final String PROFESSOR_EMAIL_PATTERN = "Professor No.%d email  No.";
    public static final String RESPONSE_PATTERN = "response No.";
    public static final String REVIEW = "review No.1";
    public static final int MARK = 5;
    public static final String STUDENT_NAME_PATTERN = "Student No.";
    public static final String STUDENT_SURNAME_PATTERN = "Student surname No.";
    public static final String STUDENT_EMAIL_PATTERN = "Student No.%d email  No.";
    public static final String TASK_TITLE_PATTERN = "Task title No.";
    public static final String TASK_DESCRIPTION_PATTERN = "Task description No.";
    public static final int MAX_LIST_SIZE = 15;
    public static final String UPDATE = "updated";
    public static final String DELETE_ALL_COURSES = "delete from courses where id <> 1";
    public static final String DELETE_ALL_PROFESSORS = "delete from professors";
    public static final String DELETE_ALL_SOLUTIONS = "delete from solutions";
    public static final String DELETE_ALL_STUDENTS = "delete from students where id <> 1";
    public static final String DELETE_ALL_STUDENTS_COURSES = "delete from students_courses";
    public static final String DELETE_ALL_TASK = "delete from tasks where id <> 1";
    public static final String GET_NUMBER_OF_STUDENTS_QUERY = "select count(*) from students where id <> 1";
    public static final String GET_NUMBER_OF_STUDENTS_BY_COURSE_QUERY = "select count(*) from students_courses where course_id = ?1";
    public static final String GET_NUMBER_OF_PROFESSORS_QUERY = "select count(*) from professors";
    public static final String GET_NUMBER_OF_COURSES_QUERY = "select count(*) from courses where id <> 1";
    public static final String GET_NUMBER_OF_COURSES_BY_PROFESSOR_QUERY = "select count(*) from courses where professor_id = ?1";
    public static final String GET_NUMBER_OF_TASKS_BY_COURSE = "select count(*) from tasks where course_id = ?1";
    public static final String PROFESSOR_EMAIL_ADMIN_TEST = "admin test email";
    public static final String PROFESSOR_EMAIL_PROFESSOR_TEST = "professor test email";
    public static final String PROFESSOR_EMAIL_STUDENTS_TEST = "student test email";
    public static final String GET_STUDENTS_LIST_QUERY = "select * from students where id IN (SELECT student_id from solutions)";
    public static final String GET_NUMBER_OF_COURSES_BY_STUDENT_QUERY = "select count(*) from students_courses where student_id = ?1";
    public static final String IS_STUDENT_REGISTERED_FOR_COURSE_QUERY = "select exists(select * from students_courses where course_id=?1 and student_id=?2)";
}
