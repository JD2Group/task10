package org.example;

import org.example.dao.impl.SolutionDaoImpl;
import org.example.excepion.Exceptions;
import org.example.pojo.*;
import org.example.service.AdminService;
import org.example.service.ProfessorService;
import org.example.service.StudentService;
import org.example.service.impl.AdminServiceImpl;
import org.example.service.impl.ProfessorServiceImpl;
import org.example.service.impl.StudentServiceImpl;
import org.example.utils.Generator;
import org.example.utils.HibernateUtil;

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
        printProfessorService(professor);
        printStudentService(students.get(0));
        deleteAccounts(professor, students.get(0), courses.get(0));

        HibernateUtil.closeFactory();
    }

    public static void printStudentService(Student student) {
        System.out.println(STUDENT_SERVICE.getStudentByEmail(student.getEmail()));
        List<Course> courses = STUDENT_SERVICE.getAllCourses();
        courses.forEach(System.out::println);
        List<Course> studentCourses = STUDENT_SERVICE.getMyCourses(student);
        studentCourses.forEach(System.out::println);
        Course randomCourse = studentCourses.get(RANDOM.nextInt(studentCourses.size()));
        STUDENT_SERVICE.checkOutCourse(randomCourse, student);
        System.out.println(STUDENT_SERVICE.getMyCourses(student).contains(randomCourse));
        STUDENT_SERVICE.checkInCourse(randomCourse,student);
        System.out.println(STUDENT_SERVICE.getMyCourses(student).contains(randomCourse));
        List<Task> tasks = STUDENT_SERVICE.getTasksFromCourse(randomCourse);
        tasks.forEach(System.out::println);
        List<Task> studentTasks = STUDENT_SERVICE.getAllMyTasks(student);
        studentTasks.forEach(System.out::println);
        Task randomTask = tasks.get(RANDOM.nextInt(tasks.size()));
        Solution solution = STUDENT_SERVICE.getSolution(randomTask, student);
        System.out.println(solution);
        if (!solution.getReadyForReview()) {
            solveSolution(solution, true);
        }
    }

    public static void printProfessorService(Professor professor) {
        System.out.println(PROFESSOR_SERVICE.getProfessorByEmail(professor.getEmail()));
        List<Course> courses = PROFESSOR_SERVICE.getMyCourses(professor);
        courses.forEach(System.out::println);
        List<Task> tasks = PROFESSOR_SERVICE.getAllTasks(courses.get(RANDOM.nextInt(courses.size())));
        tasks.forEach(System.out::println);
        List<Student> students = PROFESSOR_SERVICE.getAllStudents(courses.get(RANDOM.nextInt(courses.size())));
        students.forEach(System.out::println);
        Task task = tasks.get(RANDOM.nextInt(tasks.size()));
        System.out.println(task);
        List<Solution> taskSolutions = new SolutionDaoImpl().readAll();
        Solution randomSolution = taskSolutions.get(RANDOM.nextInt(taskSolutions.size()));
        Student student = randomSolution.getStudent();
        Task randomSolutionTask = randomSolution.getTask();
        System.out.println(randomSolution.equals(PROFESSOR_SERVICE.getSolution(student, randomSolutionTask)));
        List<Solution> readySol = taskSolutions.stream()
                .filter(Solution::getReadyForReview)
                .collect(Collectors.toList());
        Task randomTask = readySol.get(RANDOM.nextInt(readySol.size())).getTask();
        List<Solution> readySolutions = PROFESSOR_SERVICE.getAllReadySolutions(randomTask);
        readySolutions.forEach(System.out::println);
        Solution randomReadySolution = readySolutions.get(RANDOM.nextInt(readySolutions.size()));
        PROFESSOR_SERVICE.review(randomReadySolution, MARK, Generator.generateReview());
        System.out.println(PROFESSOR_SERVICE.getSolution(randomReadySolution.getStudent(), randomReadySolution.getTask()));
        Course randomCourse = courses.get(RANDOM.nextInt(courses.size()));
        Task newTask = PROFESSOR_SERVICE.addTask(randomCourse, Generator.generateTitle(), Generator.generateDescription());
        System.out.println(newTask);
        Task newTaskAfterUpdate = PROFESSOR_SERVICE.updateTask(newTask, Generator.generateTitle(), Generator.generateDescription());
        System.out.println(newTaskAfterUpdate);
        PROFESSOR_SERVICE.deleteTask(newTaskAfterUpdate);
        List<Task> tasksAfterDelete = PROFESSOR_SERVICE.getAllTasks(newTaskAfterUpdate.getCourse());
        System.out.println(String.format(OBJECT_DELETED, newTaskAfterUpdate.toString(), !tasksAfterDelete.contains(newTaskAfterUpdate)));
    }

    public static void printAdminService(Professor professor, Student student, Course course) {
        System.out.println(ADMIN_SERVICE.createCourse(Generator.generateTitle(), null));
        ADMIN_SERVICE.getAllCourses().forEach(System.out::println);
        System.out.println();
        ADMIN_SERVICE.getAllCourses(professor).forEach(System.out::println);
        System.out.println();
        System.out.println(ADMIN_SERVICE.getProfessorByEmail(professor.getEmail()));
        System.out.println(ADMIN_SERVICE.getStudentByEmail(student.getEmail()));
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
        System.out.printf((OBJECT_DELETED) + STRING_N, professor.toString(), !professors.contains(professor));
        System.out.printf((OBJECT_DELETED) + STRING_N, student.toString(), !students.contains(student));
        System.out.printf((OBJECT_DELETED) + STRING_N, course.toString(), !courses.contains(course));
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
