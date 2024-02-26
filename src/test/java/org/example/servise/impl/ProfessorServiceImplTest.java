package org.example.servise.impl;

import org.example.dao.*;
import org.example.dao.impl.*;
import org.example.pojo.*;
import org.example.servise.AdminService;
import org.example.servise.ProfessorService;
import org.example.utils.MockUtils;
import org.example.utils.QueryManager;
import org.junit.jupiter.api.*;

import java.util.List;
import java.util.stream.Collectors;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class ProfessorServiceImplTest {
    private QueryManager queryManager = new QueryManager();
    private ProfessorService service = new ProfessorServiceImpl();
    private AdminService adminService = new AdminServiceImpl();
    private SolutionDao solutionDao;
    private StudentDao studentDao;
    private CourseDao courseDao;
    private TaskDao taskDao;

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
        studentDao = new StudentDaoImpl();
        solutionDao = new SolutionDaoImpl();
        courseDao = new CourseDaoImpl();
        taskDao = new TaskDaoImpl();
    }

    @AfterEach
    public void closeQueryManager() {
        studentDao.closeManager();
        solutionDao.closeManager();
        courseDao.closeManager();
        taskDao.closeManager();
        queryManager.closeSession();
    }

    @Test
    void getMyCourses() {
        Professor professor = adminService.getProfessorByEmail(PROFESSOR_EMAIL);
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_BY_PROFESSOR_QUERY, professor.getId());
        List<Course> actual = service.getMyCourses(professor);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllTasks() {
        List<Course> courses = adminService.getAllCourses();
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_TASKS_BY_COURSE, course.getId());
        List<Task> actual = service.getAllTasks(course);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllStudents() {
        List<Course> courses = adminService.getAllCourses();
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_STUDENTS_BY_COURSE_QUERY, course.getId());
        List<Student> actual = service.getAllStudents(course);

        assertEquals(expected, actual.size());
    }

    @Test
    void getSolution() {
        List<Solution> solutions = solutionDao.readAll();
        Solution expected = solutions.get(RANDOM.nextInt(solutions.size()));
        Solution actual = service.getSolution(expected.getStudent(), expected.getTask());

        assertEquals(expected, actual);
    }

    @Test
    void getAllReadySolutions() {
        List<Solution> solutions = solutionDao.readAll().stream()
                .filter(Solution::getReadyForReview)
                .collect(Collectors.toList());
        Solution solution = solutions.get(RANDOM.nextInt(solutions.size()));
        Task task = solution.getTask();
        List<Solution> readySolutions = solutionDao.getAllReadyByTask(task);
        int expected = readySolutions.size();
        int actual = service.getAllReadySolutions(solution.getTask()).size();

        assertEquals(expected, actual);
    }

    @Test
    void addTasks() {
        List<Course> courses = courseDao.readAll();
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        Task expected = MockUtils.generateTask();
//        expected = service.addTasks(course, expected);
        List<Task> actual = service.getAllTasks(course);

        assertTrue(actual.contains(expected));
    }

    @Test
    void updateTask() {
        List<Task> tasks = taskDao.readAll();
        Task task = tasks.get(RANDOM.nextInt(tasks.size()));
        service.updateTask(task, UPDATE, UPDATE);
        Task actual = taskDao.read(task.getId());

        assertEquals(UPDATE, actual.getTitle());
        assertEquals(UPDATE, actual.getDescription());
    }

    @Test
    void deleteTask() {
        List<Task> tasks = taskDao.readAll();
        Task task = tasks.get(RANDOM.nextInt(tasks.size()));
        service.deleteTask(task);
        List<Task> actual = service.getAllTasks(task.getCourse());

        assertFalse(actual.contains(task));
    }

    @Test
    void review() {
        List<Solution> solutions = solutionDao.readAll();
        Solution solution = solutions.get(RANDOM.nextInt(solutions.size()));
        service.review(solution, MARK, REVIEW);
        Solution actual = solutionDao.read(solution.getId());

        assertEquals(MARK, actual.getMark());
        assertEquals(REVIEW, actual.getReview());
    }
}