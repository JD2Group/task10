package org.example.utils;

import org.example.pojo.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.utils.MockConstants.*;

public abstract class MockUtils {

    public static List<Course> generateRandomCourses() {
        return IntStream.range(0, LIST_SIZE)
                .mapToObj(i -> MockUtils.generateCourse())
                .collect(Collectors.toList());
    }

    public static List<Professor> generateRandomProfessors() {
        return IntStream.range(0, LIST_SIZE)
                .mapToObj(i -> MockUtils.generateProfessor())
                .collect(Collectors.toList());
    }

    public static List<Student> generateRandomStudents() {
        return IntStream.range(0, LIST_SIZE)
                .mapToObj(i -> MockUtils.generateStudent())
                .collect(Collectors.toList());
    }

    public static List<Task> generateRandomTasks() {
        return IntStream.range(0, LIST_SIZE)
                .mapToObj(i -> MockUtils.generateTask())
                .collect(Collectors.toList());
    }

    public static List<Solution> generateRandomSolutions() {
        return IntStream.range(0, LIST_SIZE)
                .mapToObj(i -> MockUtils.generateSolution())
                .collect(Collectors.toList());
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
                .email(String.format(PROFESSOR_EMAIL_PATTERN, randomNumber) + RANDOM.nextInt())
                .build();
    }

    public static Solution generateSolution() {
        return Solution.builder()
                .response(RESPONSE_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

    public static Student generateStudent() {
        int randomNumber = RANDOM.nextInt(MAX_RANDOM_NUMBER);
        return Student.builder()
                .name(STUDENT_NAME_PATTERN + randomNumber)
                .surname(STUDENT_SURNAME_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .email(String.format(STUDENT_EMAIL_PATTERN, randomNumber) + RANDOM.nextInt())
                .build();
    }

    public static Task generateTask() {
        return Task.builder()
                .title(TASK_TITLE_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .description(TASK_DESCRIPTION_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

}
