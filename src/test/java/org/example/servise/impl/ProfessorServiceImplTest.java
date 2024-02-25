package org.example.servise.impl;

import org.example.dao.*;
import org.example.dao.impl.*;
import org.example.pojo.*;
import org.example.servise.AdminService;
import org.example.servise.ProfessorService;
import org.example.utils.MockUtils;
import org.example.utils.QueryManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class ProfessorServiceImplTest {
    private ProfessorService service = new ProfessorServiceImpl();
    private AdminService adminService = new AdminServiceImpl();
    private Professor professor;
    private List<Course> courses;
    private Course course;
    private Student student;
    private Task task;
    private Solution solution;

    @BeforeAll
    public static void createData() {
        QueryManager.createTestData(PROFESSOR_EMAIL_PROFESSOR_TEST);
    }

    @AfterAll
    public static void deleteAll() {
//        QueryManager.deleteAll();
    }

    @BeforeEach
    public void getData() {
        professor = adminService.getProfessorByEmail(PROFESSOR_EMAIL_PROFESSOR_TEST);
        courses = adminService.getAllCourses(professor).stream()
        .filter(c -> !c.getTasks().isEmpty())
        .collect(Collectors.toList());
        course = courses.get(RANDOM.nextInt(courses.size()));
        List<Task> tasks = service.getAllTasks(course);
        task = tasks.get(RANDOM.nextInt(tasks.size()));
        List<Solution> solutions = new ArrayList<>(task.getSolutions());
        solution = solutions.get(RANDOM.nextInt(solutions.size()));
        student = solution.getStudent();
    }

    @Test
    void getMyCourses() {
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_BY_PROFESSOR_QUERY, professor.getId());
        List<Course> actual = service.getMyCourses(professor);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllTasks() {
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_TASKS_BY_COURSE, course.getId());
        List<Task> actual = service.getAllTasks(course);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllStudents() {
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_STUDENTS_BY_COURSE_QUERY, course.getId());
        List<Student> actual = service.getAllStudents(course);

        assertEquals(expected, actual.size());
    }

    @Test
    void getSolution() {
        Solution actual = service.getSolution(student, task);

        assertTrue(student.getSolutions().contains(actual));
    }

    @Test
    void getAllReadySolutions() {
        int expected = (int) task.getSolutions().stream()
                .filter(Solution::getReadyForReview).count();
        int actual = service.getAllReadySolutions(task).size();

        assertEquals(expected, actual);
    }

    @Test
    void addTasks() {
        Task expected = MockUtils.generateTask();
        service.addTasks(course, expected);
        List<Task> actual = service.getAllTasks(course);

        assertTrue(actual.contains(task));
    }

    @Test
    void updateTask() {
        TaskDao taskDao = new TaskDaoImpl();
        service.updateTask(task, UPDATE, UPDATE);
        Task actual = taskDao.read(task.getId());

        assertEquals(UPDATE, actual.getTitle());
        assertEquals(UPDATE, actual.getDescription());
    }

    @Test
    void deleteTask() {
        TaskDao taskDao = new TaskDaoImpl();
        service.deleteTask(task);
        Task actual = taskDao.read(task.getId());

        assertNull(actual);
    }

    @Test
    void review() {
        SolutionDao solutionDao = new SolutionDaoImpl();
        service.review(solution, MARK, REVIEW);
        Solution actual = solutionDao.read(solution.getId());

        assertEquals(MARK, actual.getMark());
        assertEquals(REVIEW, actual.getReview());
    }
}