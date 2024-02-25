package org.example.utils;

import org.example.pojo.*;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.utils.MockConstants.*;

public abstract class MockUtils {

    public static Set<Course> generateRandomCourses() {
        return IntStream.range(0, MAX_LIST_SIZE)
                .mapToObj(i -> MockUtils.generateCourse())
                .collect(Collectors.toSet());
    }

    public static Set<Professor> generateRandomProfessors() {
        return IntStream.range(0, MAX_LIST_SIZE)
                .mapToObj(i -> MockUtils.generateProfessor())
                .collect(Collectors.toSet());
    }

    public static Set<Student> generateRandomStudents() {
        return IntStream.range(0, MAX_LIST_SIZE)
                .mapToObj(i -> MockUtils.generateStudent())
                .collect(Collectors.toSet());
    }

    public static Set<Task> generateRandomTasks() {
        return IntStream.range(0, MAX_LIST_SIZE)
                .mapToObj(i -> MockUtils.generateTask())
                .collect(Collectors.toSet());
    }

    public static Course generateCourse() {
        return Course.builder()
                .title(COURSE_TITLE_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

    public static Professor generateProfessor() {
        int randomNumber = RANDOM.nextInt(MAX_RANDOM_NUMBER);
        return Professor.builder()
                .name(PROFESSOR_NAME_PATTERN + randomNumber)
                .surname(PROFESSOR_SURNAME_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .email(String.format(PROFESSOR_EMAIL_PATTERN, randomNumber) + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

    public static Solution generateSolution() {
        return Solution.builder()
                .build();
    }

    public static Student generateStudent() {
        int randomNumber = RANDOM.nextInt(MAX_RANDOM_NUMBER);
        return Student.builder()
                .name(STUDENT_NAME_PATTERN + randomNumber)
                .surname(STUDENT_SURNAME_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .email(String.format(STUDENT_EMAIL_PATTERN, randomNumber) + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

    public static Task generateTask() {
        return Task.builder()
                .title(TASK_TITLE_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .description(TASK_DESCRIPTION_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

}
