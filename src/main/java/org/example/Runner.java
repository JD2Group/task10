package org.example;

import org.example.excepion.Exceptions;
import org.example.pojo.*;
import org.example.servise.AdminService;
import org.example.servise.ProfessorService;
import org.example.servise.StudentService;
import org.example.servise.impl.AdminServiceImpl;
import org.example.servise.impl.ProfessorServiceImpl;
import org.example.servise.impl.StudentServiceImpl;
import org.example.utils.Generator;

import javax.persistence.NoResultException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.example.utils.Constants.*;

public class Runner {
    private static final AdminService ADMIN_SERVICE = new AdminServiceImpl();
    private static final ProfessorService PROFESSOR_SERVICE = new ProfessorServiceImpl();
    private static final StudentService STUDENT_SERVICE = new StudentServiceImpl();

    public static void main(String[] args) {
        Professor professor = ADMIN_SERVICE.createProfessorAccount(Generator.generateName(), Generator.generateSurname(), Generator.generateEmail());

        List<Course> courses = IntStream.range(0, INITIAL_NUMBER_OF_COURSES)
                .mapToObj(i -> ADMIN_SERVICE.createCourse(Generator.generateTitle(), professor))
                .collect(Collectors.toList());

        List<Student> students = IntStream.range(0, INITIAL_NUMBER_OF_STUDENTS)
                .mapToObj(i -> {
                    Student s = ADMIN_SERVICE.createStudentAccount(Generator.generateName(), Generator.generateSurname(), Generator.generateEmail());
                    courses.forEach(c -> STUDENT_SERVICE.checkInCourse(c, s));
                    return s;
                })
                .collect(Collectors.toList());

        List<Task> tasks = IntStream.range(0, INITIAL_NUMBER_OF_TASKS)
                .mapToObj(i -> PROFESSOR_SERVICE.addTask(courses.get(i), Generator.generateTitle(), Generator.generateDescription()))
                .collect(Collectors.toList());

        List<Solution> solutions = IntStream.range(0, INITIAL_NUMBER_OF_SOLUTIONS)
                .mapToObj(i -> {
                    Solution s = STUDENT_SERVICE.getSolution(tasks.get(i), students.get(i));
                    return solveSolution(s, i % 2 == 0);
                })
                .collect(Collectors.toList());

        tasks.forEach(t -> PROFESSOR_SERVICE.getAllReadySolutions(t).forEach(s ->
                PROFESSOR_SERVICE.review(s, MARK, Generator.generateReview()
                )));

        solutions.forEach(s -> PROFESSOR_SERVICE.review(s, MARK, Generator.generateReview()));

        printAdminService(professor, students.get(0), courses.get(0));
        deleteAccounts(professor, students.get(0), courses.get(0));

    }

    public static void printAdminService(Professor professor, Student student, Course course) {
        ADMIN_SERVICE.createCourse(Generator.generateTitle(), null);
        ADMIN_SERVICE.getAllCourses().forEach(System.out::println);
        System.out.println();
        ADMIN_SERVICE.getAllCourses(professor).forEach(System.out::println);
        System.out.println();
        ADMIN_SERVICE.getProfessorByEmail(professor.getEmail());
        ADMIN_SERVICE.getStudentByEmail(student.getEmail());
        System.out.println();
        ADMIN_SERVICE.getAllStudents().forEach(System.out::println);
        ADMIN_SERVICE.getAllStudents(course).forEach(System.out::println);
        Professor newProfessor = ADMIN_SERVICE.createProfessorAccount(Generator.generateName(), Generator.generateSurname(), Generator.generateEmail());
        ADMIN_SERVICE.getAllProfessors().forEach(System.out::println);
        ADMIN_SERVICE.getCoursesByTitleAndProfEmail(course.getTitle(), professor.getEmail()).forEach(System.out::println);
        ADMIN_SERVICE.updateCourse(course, Generator.generateTitle());
        ADMIN_SERVICE.updateCourse(course, newProfessor);
    }

    public static void deleteAccounts(Professor professor, Student student, Course course) {
        ADMIN_SERVICE.deleteAccount(professor);
        ADMIN_SERVICE.deleteAccount(student);
        ADMIN_SERVICE.deleteCourse(course);
        List<Professor> professors = ADMIN_SERVICE.getAllProfessors();
        List<Student> students = ADMIN_SERVICE.getAllStudents();
        List<Course>  courses = ADMIN_SERVICE.getAllCourses();
        System.out.println(String.format(OBJECT_DELETED, professor.toString(), !professors.contains(professor)));
        System.out.println(String.format(OBJECT_DELETED, student.toString(), !students.contains(student)));
        System.out.println(String.format(OBJECT_DELETED, course.toString(), !courses.contains(course)));

        ADMIN_SERVICE.clearBaseFromSolutionsWithoutStudentIdAndTaskId();
    }

    public static Solution solveSolution(Solution solution, boolean readyForCheck) {
        try {
            STUDENT_SERVICE.solveTask(solution, readyForCheck, Generator.generateResponse());
        } catch (Exceptions.SolutionIsResolvedException e) {
            e.printStackTrace();
        }
        return solution;
    }


}
