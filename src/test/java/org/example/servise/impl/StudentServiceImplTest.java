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
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;

class StudentServiceImplTest {
    private StudentService service = new StudentServiceImpl();
    private Student student;

    @BeforeAll
    public static void createData() {
        QueryManager.createTestData(PROFESSOR_EMAIL_STUDENTS_TEST);
    }

    @AfterAll
    public static void deleteAll() {
//        QueryManager.deleteAll();
    }

    @BeforeEach
    public void setStudents() {
        List<Student> students = QueryManager.getList(GET_STUDENTS_LIST_QUERY, Student.class);
        student = students.get(RANDOM.nextInt(students.size()));
    }

    @Test
    void getByEmail() {
        Student actual = service.getByEmail(student.getEmail());

        assertEquals(student, actual);
    }

    @Test
    void getAllCourses() {
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_QUERY);
        List<Course> actual = service.getAllCourses();

        assertEquals(expected, actual.size());
    }

    @Test
    void getMyCourses() {
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_BY_STUDENT_QUERY, student.getId());
        List<Course> actual = service.getMyCourses(student);

        assertEquals(expected, actual.size());
    }

    @Test
    void checkInCourse() {
        List<Course> courses = QueryManager.getList(String.format(GET_COURSES_WITHOUT_CURRENT_STUDENT_QUERY, student.getId()), Course.class);
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        service.checkInCourse(course, student);
        int expected = 1;
        int actual = QueryManager.getNumberOfObjects(IS_STUDENT_REGISTERED_FOR_COURSE_QUERY, course.getId(), student.getId());

        assertEquals(expected, actual);
    }

    @Test
    void checkOutCourse() {
        List<Course> courses = QueryManager.getList(String.format(GET_COURSES_BY_STUDENT_QUERY, student.getId()), Course.class);
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        service.checkOutCourse(course, student);
        int expected = 0;
        int actual = QueryManager.getNumberOfObjects(IS_STUDENT_REGISTERED_FOR_COURSE_QUERY, course.getId(), student.getId());

        assertEquals(expected, actual);
    }

    @Test
    void getTasksFromCourse() {
        List<Course> courses = QueryManager.getList(String.format(GET_COURSES_BY_STUDENT_QUERY, student.getId()), Course.class);
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        List<Task> expected = QueryManager.getList(String.format(GET_TASK_BY_COURSE_QUERY, course.getId()), Task.class);
        List<Task> actual = service.getTasksFromCourse(course);

        assertIterableEquals(expected, actual);

    }

    @Test
    void getSolution() {
        SolutionDao solutionDao = new SolutionDaoImpl();
        List<Course> courses = QueryManager.getList(String.format(GET_COURSES_BY_STUDENT_QUERY, student.getId()), Course.class);
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        List<Task> tasks = service.getTasksFromCourse(course);
        Task task = tasks.get(RANDOM.nextInt(tasks.size()));
        Solution expected = service.getSolution(task, student);
        Solution actual = solutionDao.getByStudentTask(student, task);

        assertEquals(expected, actual);
    }

    @Test
    void solveTask() {
        List<Solution> solutions = QueryManager.getList(GET_SOLUTION_QUERY, Solution.class);
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