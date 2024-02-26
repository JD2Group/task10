package org.example.servise.impl;

import org.example.dao.*;
import org.example.dao.impl.*;
import org.example.pojo.*;
import org.example.servise.ProfessorService;
import org.example.utils.MockUtils;
import org.example.utils.QueryManager;
import org.junit.jupiter.api.*;
import java.util.List;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class ProfessorServiceImplTest {
    private QueryManager queryManager = new QueryManager();
    private ProfessorService service = new ProfessorServiceImpl();
    private Professor professor;
    private Course course;
    private Student student;
    private Task task;
    private Solution solution;

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
    public void getData() {
        List<Solution> solutions = queryManager.getList(GET_SOLUTIONS_QUERY, Solution.class);
        solution = solutions.get(RANDOM.nextInt(solutions.size()));
        task = solution.getTask();
        student = solution.getStudent();
        course = task.getCourse();
        professor = course.getProfessor();
    }

    @AfterEach
    public void closeQueryManager() {
        queryManager.closeSession();
    }

    @Test
    void getMyCourses() {
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_BY_PROFESSOR_QUERY, professor.getId());
        List<Course> actual = service.getMyCourses(professor);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllTasks() {
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_TASKS_BY_COURSE, course.getId());
        List<Task> actual = service.getAllTasks(course);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllStudents() {
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_STUDENTS_BY_COURSE_QUERY, course.getId());
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