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
import org.example.utils.Printer;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.stream.IntStream;

import static org.example.utils.Constants.*;

public class Runner {
    private static final AdminService ADMIN_SERVICE = new AdminServiceImpl();
    private static final StudentService STUDENT_SERVICE = new StudentServiceImpl();
    private static final ProfessorService PROFESSOR_SERVICE = new ProfessorServiceImpl();

    public static void main(String[] args) {
        //adm
        saveProfessorsStudentsCourses();

        List<Course> courses = getObject(ADMIN_SERVICE::getAllCourses, "All courses");
        List<Professor> professors = getObject(ADMIN_SERVICE::getAllProfessors, "All professors");
        List<Student> students = getObject(ADMIN_SERVICE::getAllStudents, "All students");

        setProfessorsToCourses(professors, courses);

        String randomProfessorsEmail = professors.get(RANDOM.nextInt(professors.size())).getEmail();
        String randomStudentEmail = students.get(RANDOM.nextInt(students.size())).getEmail();

        findDataWithAdminService(randomProfessorsEmail, randomStudentEmail);

        //st
        checkInStudentsToCourses(students, courses);

        //adm
        Professor randomProfessor = professors.get(RANDOM.nextInt(professors.size()));
        Course randomCourse = courses.get(RANDOM.nextInt(courses.size()));
        Student randomStudent = students.get(RANDOM.nextInt(students.size()));

        //st
        Printer.printObjects("Student by email: " + randomStudent.getEmail(),
                STUDENT_SERVICE.getStudentByEmail(randomStudent.getEmail()));

        Printer.printObjects("All courses", STUDENT_SERVICE.getAllCourses());

        //pr
        List<Student> professorStudents = getObject(() -> PROFESSOR_SERVICE.getAllStudents(randomCourse),
                "Students by professor: ");

        //st
        Student student = professorStudents.get(RANDOM.nextInt(professorStudents.size()));

        List<Course> studentCourses = getObject(() -> STUDENT_SERVICE.getMyCourses(student), "All courses by student: ");
        Course studentCourse = studentCourses.get(RANDOM.nextInt(studentCourses.size()));

        addTasksToCourses(studentCourses);

        //pr
        checkInCheckOutCourse(studentCourse, student);

        List<Task> studentTasks = getObject(() -> STUDENT_SERVICE.getAllMyTasks(student), "All tasks by student: " + student.getEmail());
        Task randomTask = studentTasks.get(RANDOM.nextInt(studentTasks.size()));

        Solution solution = getObject(() -> STUDENT_SERVICE.getSolution(randomTask, student),
                "Solution by task before: ");
        solveSolution(solution);
        Printer.printObjects("Solution by task ready: " + randomTask.getTitle() + ": " + solution.getResponse(), solution);

        //pr
        Solution solutionForReview = getObject(() -> PROFESSOR_SERVICE.getSolution(student, randomTask), "Solution for review");
        PROFESSOR_SERVICE.review(solutionForReview, MARK, Generator.generateReview());
        Printer.printObjects("Solution after review", solutionForReview);

        deleteObject(PROFESSOR_SERVICE::deleteTask, randomTask, () -> PROFESSOR_SERVICE.getAllTasks(randomTask.getCourse()));

        //adm
        updateCourse(randomCourse, randomProfessor);
        deleteObject(ADMIN_SERVICE::deleteAccount, randomProfessor, ADMIN_SERVICE::getAllProfessors);
        deleteObject(ADMIN_SERVICE::deleteAccount, randomStudent, ADMIN_SERVICE::getAllStudents);
        deleteObject(ADMIN_SERVICE::deleteCourse, randomCourse, ADMIN_SERVICE::getAllCourses);
    }

    public static void saveProfessorsStudentsCourses() {
        List<String> professorEmails = Generator.generateStrings(RANDOM_EMAIL);
        saveProfessors(professorEmails);

        List<String> studentEmails = Generator.generateStrings(RANDOM_EMAIL);
        saveStudents(studentEmails);

        List<String> courseTitles = Generator.generateStrings(RANDOM_TITLE);
        saveCourses(courseTitles);
    }

    private static void findDataWithAdminService(String professorsEmail, String studentEmail) {
        Professor randomProfessor = getObject(() -> ADMIN_SERVICE.getProfessorByEmail(professorsEmail),
                "Professor with email: %s :" + professorsEmail + " : ");

        getObject(() -> ADMIN_SERVICE.getStudentByEmail(studentEmail),
                "Student with email: " + studentEmail + " : ");

        getObject(() -> ADMIN_SERVICE.getAllCourses(randomProfessor), "All courses by professor: ");
    }

    private static void updateCourse(Course course, Professor professor) {
        System.out.println("Course before update: " + course);
        ADMIN_SERVICE.updateCourse(course, professor);
        System.out.println("Course after update: " + course);

    }

    public static void addTasksToCourses(List<Course> professorCourses) {
        Course randomCourse = professorCourses.get(RANDOM.nextInt(professorCourses.size()));

        saveTasks(professorCourses);

        Task newTask = PROFESSOR_SERVICE.addTask(randomCourse, Generator.generateTitle(), Generator.generateDescription());
        updateTask(newTask);
    }

    private static void updateTask(Task task) {
        Printer.printObjects("Task before update: ", task);
        Task newTaskAfterUpdate = PROFESSOR_SERVICE.updateTask(task, Generator.generateTitle(), Generator.generateDescription());
        Printer.printObjects("Task after update: ", newTaskAfterUpdate);

    }

    public static void saveTasks(List<Course> courses) {
        IntStream.range(0, courses.size())
                .forEach(i -> PROFESSOR_SERVICE.addTask(courses.get(i), Generator.generateTitle(), Generator.generateDescription()));
    }

    public static void checkInCheckOutCourse(Course course, Student student) {
        STUDENT_SERVICE.checkOutCourse(course, student);
        Printer.printObjects("Student check out course:", !STUDENT_SERVICE.getMyCourses(student).contains(course));

        STUDENT_SERVICE.checkInCourse(course, student);
        Printer.printObjects("Student check in course:", STUDENT_SERVICE.getMyCourses(student).contains(course));
    }

    public static void saveProfessors(List<String> emails) {
        IntStream.range(0, emails.size())
                .forEach(i -> ADMIN_SERVICE.createProfessorAccount(Generator.generateName(),
                        Generator.generateSurname(), emails.get(i)));
    }

    public static void saveStudents(List<String> emails) {
        IntStream.range(0, emails.size())
                .forEach(i -> ADMIN_SERVICE.createStudentAccount(Generator.generateName(),
                        Generator.generateSurname(), emails.get(i)));
    }

    public static void saveCourses(List<String> titles) {
        IntStream.range(0, titles.size())
                .forEach(i -> ADMIN_SERVICE.createCourse(titles.get(i), null));
    }

    private static void setProfessorsToCourses(List<Professor> professors, List<Course> courses) {
        IntStream.range(0, courses.size())
                .forEach(i -> ADMIN_SERVICE.updateCourse(courses.get(i), professors.get(i)));
    }

    public static void checkInStudentsToCourses(List<Student> students, List<Course> courses) {
        IntStream.range(0, students.size())
                .forEach(i -> STUDENT_SERVICE.checkInCourse(courses.get(i), students.get(i)));
    }

    public static <T> T getObject(Supplier<T> method, String messageForPrint) {
        T result = method.get();
        Printer.printObjects(messageForPrint, result);
        return result;
    }

    private static <T> void deleteObject(Consumer<T> method, T object, Supplier<List<T>> supplier) {
        method.accept(object);
        boolean isObjectDeleted = !supplier.get().contains(object);
        System.out.println(String.format(OBJECT_DELETED, object.toString(), isObjectDeleted));
    }

    private static void solveSolution(Solution solution) {
        try {
            STUDENT_SERVICE.solveTask(solution, true, Generator.generateResponse());
        } catch (Exceptions.SolutionIsResolvedException e) {
            e.printStackTrace();
        }
    }
}
