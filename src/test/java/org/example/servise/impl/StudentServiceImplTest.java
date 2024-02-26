package org.example.servise.impl;

import org.example.dao.SolutionDao;
import org.example.dao.impl.SolutionDaoImpl;
import org.example.excepion.Exceptions;
import org.example.pojo.Course;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;
import org.example.servise.StudentService;
import org.example.utils.QueryManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class StudentServiceImplTest {
    private QueryManager queryManager = new QueryManager();
    private StudentService service = new StudentServiceImpl();
    private Student student;

    @BeforeAll
    public static void createData() {
        QueryManager queryManager = new QueryManager();
        queryManager.createTestData(PROFESSOR_EMAIL);
        queryManager.closeSession();
    }

    @AfterAll
    public static void deleteAll() {
        QueryManager queryManager = new QueryManager();
        queryManager.deleteAll();
        queryManager.closeSession();
    }

    @BeforeEach
    public void setStudents() {
        List<Student> students = queryManager.getList(GET_STUDENTS_LIST_QUERY, Student.class);
        student = students.get(RANDOM.nextInt(students.size()));
    }

    @AfterEach
    public void closeQueryManager() {
        queryManager.closeSession();
    }

    @Test
    void getByEmail() {
        Student actual = service.getByEmail(student.getEmail());

        assertEquals(student, actual);
    }

    @Test
    void getAllCourses() {
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_QUERY);
        List<Course> actual = service.getAllCourses();

        assertEquals(expected, actual.size());
    }

    @Test
    void getMyCourses() {
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_BY_STUDENT_QUERY, student.getId());
        List<Course> actual = service.getMyCourses(student);

        assertEquals(expected, actual.size());
    }

    @Test
    void checkInCourse() {
        List<Course> courses = queryManager.getList(GET_COURSES_WITHOUT_CURRENT_STUDENT_QUERY, Course.class, student.getId());
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        service.checkInCourse(course, student);
        int expected = 1;
        int actual = queryManager.getNumberOfObjects(IS_STUDENT_REGISTERED_FOR_COURSE_QUERY, course.getId(), student.getId());

        assertEquals(expected, actual);
    }

    @Test
    void checkOutCourse() {
        List<Course> courses = queryManager.getList(GET_COURSES_BY_STUDENT_QUERY, Course.class, student.getId());
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        service.checkOutCourse(course, student);
        int expected = 0;
        int actual = queryManager.getNumberOfObjects(IS_STUDENT_REGISTERED_FOR_COURSE_QUERY, course.getId(), student.getId());

        assertEquals(expected, actual);
    }

    @Test
    void getTasksFromCourse() {
        List<Course> courses = queryManager.getList(GET_COURSES_BY_STUDENT_QUERY, Course.class, student.getId());
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        List<Task> expected = queryManager.getList(GET_TASK_BY_COURSE_QUERY, Task.class, course.getId());
        List<Task> actual = service.getTasksFromCourse(course);

        assertIterableEquals(expected, actual);

    }

    @Test
    void getSolution() {
        List<Solution> solutions = queryManager.getList(GET_SOLUTIONS_QUERY, Solution.class);
        Solution expected = solutions.get(RANDOM.nextInt(solutions.size()));
        Task task = expected.getTask();
        Student student = expected.getStudent();
        Solution actual = service.getSolution(task, student);

        assertEquals(expected, actual);
    }

    @Test
    void solveTask() {
        List<Solution> solutions = queryManager.getList(GET_SOLUTIONS_QUERY, Solution.class);
        Solution solution = solutions.get(RANDOM.nextInt(solutions.size()));
        try {
            service.solveTask(solution, true, UPDATE);
        } catch (Exceptions.SolutionIsResolvedException e) {
            e.printStackTrace();
        }
        Solution actual = service.getSolution(solution.getTask(), solution.getStudent());

        assertEquals(UPDATE, actual.getResponse());
    }
}