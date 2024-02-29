package org.example;

import org.example.excepion.Exceptions;
import org.example.pojo.*;
import org.example.service.AdminService;
import org.example.service.ProfessorService;
import org.example.service.StudentService;
import org.example.service.impl.AdminServiceImpl;
import org.example.service.impl.ProfessorServiceImpl;
import org.example.service.impl.StudentServiceImpl;
import org.example.utils.CustomConsumer;
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
        //admin service (save professors, courses, students)
        saveInitialData();

        List<Course> courses = getObject(ADMIN_SERVICE::getAllCourses, String.format(ALL_OBJECTS, Course.class.getSimpleName()));
        List<Professor> professors = getObject(ADMIN_SERVICE::getAllProfessors, String.format(ALL_OBJECTS, Professor.class.getSimpleName()));
        List<Student> students = getObject(ADMIN_SERVICE::getAllStudents, String.format(ALL_OBJECTS, Student.class.getSimpleName()));

        //admin service (set professors to courses)
        applyChanges(ADMIN_SERVICE::updateCourse, courses, professors);
        //select a random professor and student
        String randomProfessorsEmail = professors.get(RANDOM.nextInt(professors.size())).getEmail();
        String randomStudentEmail = students.get(RANDOM.nextInt(students.size())).getEmail();
        //admin service (find a random professor, student and all courses in the database)
        findDataWithAdminService(randomProfessorsEmail, randomStudentEmail);

        //student service (enroll students in courses)
        applyChanges(STUDENT_SERVICE::checkInCourse, courses, students);

        //select random professor, student and course
        Professor randomProfessor = professors.get(RANDOM.nextInt(professors.size()));
        Course randomCourse = courses.get(RANDOM.nextInt(courses.size()));
        Student randomStudent = students.get(RANDOM.nextInt(students.size()));

        //student service (find student by email)
        Printer.printObjects(OBJECT_BY_EMAIL + randomStudent.getEmail(),
                STUDENT_SERVICE.getStudentByEmail(randomStudent.getEmail()));

        //professor service (get professor's students and select random student)
        List<Student> professorStudents = getObject(() -> PROFESSOR_SERVICE.getAllStudents(randomCourse),
                STUDENTS_BY_PROFESSOR);
        Student student = professorStudents.get(RANDOM.nextInt(professorStudents.size()));

        //student service (get student courses and choose random course)
        List<Course> studentCourses = getObject(() -> STUDENT_SERVICE.getMyCourses(student), String.format(ALL_COURSES_BY_OBJECT, Student.class.getSimpleName()));
        Course studentCourse = studentCourses.get(RANDOM.nextInt(studentCourses.size()));
        //student service (check the possibility of enrolling in a course and unsubscribing from it)
        checkInCheckOutFromCourse(studentCourse, student);

        //professor service (add tasks to courses)
        addTasksToCourses(studentCourses);

        //student service (get all the student's tasks and select a random task)
        List<Task> studentTasks = getObject(() -> STUDENT_SERVICE.getAllMyTasks(student), ALL_TASKS_BY_STUDENT + student.getEmail());
        Task randomTask = studentTasks.get(RANDOM.nextInt(studentTasks.size()));
        //student service (solve task)
        Solution solution = getObject(() -> STUDENT_SERVICE.getSolution(randomTask, student), SOLUTION_BY_TASK_BEFORE);
        solveTask(solution);
        Printer.printObjects(String.format(READY_SOLUTION_BY_TASK, randomTask.getTitle()) + solution.getResponse(), solution);

        //professor service (check the solved task)
        Solution solutionForReview = getObject(() -> PROFESSOR_SERVICE.getSolution(student, randomTask), SOLUTION_FOR_REVIEW);
        PROFESSOR_SERVICE.review(solutionForReview, MARK, Generator.generateReview());
        Printer.printObjects(SOLUTION_AFTER_REVIEW, solutionForReview);

        //admin service (update random course)
        updateCourse(randomCourse, randomProfessor);
        //admin service/professor (delete course, professor, student and task)
        deleteObjects(randomProfessor, randomCourse, randomStudent, randomTask);
    }

    public static void saveInitialData() {
        List<String> professorEmails = Generator.generateStrings(RANDOM_EMAIL);
        save(professorEmails, Professor.class);

        List<String> studentEmails = Generator.generateStrings(RANDOM_EMAIL);
        save(studentEmails, Student.class);

        List<String> courseTitles = Generator.generateStrings(RANDOM_TITLE);
        save(courseTitles, Course.class);
    }

    private static void findDataWithAdminService(String professorsEmail, String studentEmail) {
        Professor randomProfessor = getObject(() -> ADMIN_SERVICE.getProfessorByEmail(professorsEmail),
                String.format(OBJECT_BY_EMAIL, Professor.class.getSimpleName()) + professorsEmail);

        getObject(() -> ADMIN_SERVICE.getStudentByEmail(studentEmail),
                String.format(OBJECT_BY_EMAIL, Student.class.getSimpleName()) + studentEmail);

        getObject(() -> ADMIN_SERVICE.getAllCourses(randomProfessor), String.format(ALL_COURSES_BY_OBJECT, Professor.class.getSimpleName()));
    }

    private static void updateCourse(Course course, Professor professor) {
        updateObject(() -> ADMIN_SERVICE.updateCourse(course, professor), course, Course.class);
    }

    public static void addTasksToCourses(List<Course> professorCourses) {
        Course randomCourse = professorCourses.get(RANDOM.nextInt(professorCourses.size()));

        save(professorCourses, Task.class);

        Task newTask = PROFESSOR_SERVICE.addTask(randomCourse, Generator.generateTitle(), Generator.generateDescription());
        updateTask(newTask);
    }

    private static void updateTask(Task task) {
        updateObject(() -> PROFESSOR_SERVICE.updateTask(task, Generator.generateTitle(), Generator.generateDescription()),
                task, Task.class);
    }

    public static void checkInCheckOutFromCourse(Course course, Student student) {
        STUDENT_SERVICE.checkOutCourse(course, student);
        Printer.printObjects(STUDENT_CHECK_OUT_COURSE, !STUDENT_SERVICE.getMyCourses(student).contains(course));

        STUDENT_SERVICE.checkInCourse(course, student);
        Printer.printObjects(STUDENT_CHECK_IN_COURSE, STUDENT_SERVICE.getMyCourses(student).contains(course));
    }

    public static <T> void save(List<T> list, Class<?> clazz) {
        list.forEach(o -> selectSaveMethod(o, clazz));
    }

    private static <T> void selectSaveMethod(T object, Class<?> clazz) {
        if (clazz == Professor.class) {
            ADMIN_SERVICE.createProfessorAccount(Generator.generateName(), Generator.generateSurname(), (String) object);
        } else if (clazz == Student.class) {
            ADMIN_SERVICE.createStudentAccount(Generator.generateName(), Generator.generateSurname(), (String) object);
        } else if (clazz == Course.class) {
            ADMIN_SERVICE.createCourse(Generator.generateTitle(), null);
        } else if (clazz == Task.class) {
            PROFESSOR_SERVICE.addTask((Course) object, Generator.generateTitle(), Generator.generateDescription());
        }
    }

    public static <T, R> void applyChanges(CustomConsumer<T, R> method, List<T> list1, List<R> list2) {
        IntStream.range(0, list1.size())
                .forEach(i -> method.accept(list1.get(i), list2.get(i)));
    }

    public static <T> void updateObject(Supplier<T> method, T object, Class<T> clazz) {
        Printer.printObjects(String.format(OBJECT_BEFORE_UPDATE, clazz.getSimpleName()), object);
        T objectAfterUpdate = method.get();
        Printer.printObjects(String.format(OBJECT_AFTER_UPDATE, clazz.getSimpleName()), objectAfterUpdate);
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

    private static void solveTask(Solution solution) {
        try {
            STUDENT_SERVICE.solveTask(solution, true, Generator.generateResponse());
        } catch (Exceptions.SolutionIsResolvedException e) {
            e.printStackTrace();
        }
    }

    private static void deleteObjects(Professor randomProfessor, Course randomCourse, Student randomStudent, Task randomTask) {
        deleteObject(ADMIN_SERVICE::deleteCourse, randomCourse, ADMIN_SERVICE::getAllCourses);
        deleteObject(PROFESSOR_SERVICE::deleteTask, randomTask, () -> PROFESSOR_SERVICE.getAllTasks(randomTask.getCourse()));
        deleteObject(ADMIN_SERVICE::deleteAccount, randomProfessor, ADMIN_SERVICE::getAllProfessors);
        deleteObject(ADMIN_SERVICE::deleteAccount, randomStudent, ADMIN_SERVICE::getAllStudents);
    }
}
