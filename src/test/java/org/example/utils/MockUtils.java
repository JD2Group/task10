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
                .title(COURSE_TITLE + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

    public static Professor generateProfessor() {
        return Professor.builder()
                .name(PROFESSOR_NAME + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .surname(PROFESSOR_SURNAME + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

    public static Solution generateSolution() {
        return Solution.builder()
                .build();
    }

    public static Student generateStudent() {
        return Student.builder()
                .name(STUDENT_NAME + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .surname(STUDENT_SURNAME + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

    public static Task generateTask() {
        return Task.builder()
                .title(TASK_TITLE + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .description(TASK_DESCRIPTION + RANDOM.nextInt(MAX_RANDOM_NUMBER))
                .build();
    }

}
