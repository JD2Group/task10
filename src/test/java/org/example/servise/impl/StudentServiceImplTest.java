package org.example.servise.impl;

import org.example.dao.CourseDao;
import org.example.dao.SolutionDao;
import org.example.dao.StudentDao;
import org.example.dao.TaskDao;
import org.example.dao.impl.CourseDaoImpl;
import org.example.dao.impl.SolutionDaoImpl;
import org.example.dao.impl.StudentDaoImpl;
import org.example.dao.impl.TaskDaoImpl;
import org.example.excepion.Exceptions;
import org.example.pojo.*;
import org.example.servise.ProfessorService;
import org.example.servise.StudentService;
import org.example.utils.QueryManager;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class StudentServiceImplTest {
    private QueryManager queryManager = new QueryManager();
    private StudentService service = new StudentServiceImpl();
    private StudentDao studentDao;
    private CourseDao courseDao;
    private TaskDao taskDao;
    private SolutionDao solutionDao;
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
        studentDao = new StudentDaoImpl();
        courseDao = new CourseDaoImpl();
        taskDao = new TaskDaoImpl();
        solutionDao = new SolutionDaoImpl();
        List<Student> students = studentDao.readAll();
        student = students.get(RANDOM.nextInt(students.size()));
    }

    @AfterEach
    public void closeQueryManager() {
        studentDao.closeManager();
        courseDao.closeManager();
        taskDao.closeManager();
        solutionDao.closeManager();
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
        int expected = studentDao.readAllByStudentId(student.getId()).size();
        List<Course> actual = service.getMyCourses(student);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllMyTasks() {
        List<Student> students = studentDao.readAll();
        Student student = students.get(RANDOM.nextInt(students.size()));
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_TASKS_BY_STUDENT, student.getId());
        int actual = service.getAllMyTasks(student).size();

        assertEquals(expected, actual);
    }

    @Test
    void checkInCourse() {
        List<Course> courses = studentDao.readAllByStudentId(student.getId());
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        service.checkInCourse(course, student);
        int expected = 1;
        int actual = queryManager.getNumberOfObjects(IS_STUDENT_REGISTERED_FOR_COURSE_QUERY, course.getId(), student.getId());

        assertEquals(expected, actual);
    }

    @Test
    void checkOutCourse() {
        List<Course> courses = studentDao.readAllByStudentId(student.getId());
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        service.checkOutCourse(course, student);
        int expected = 0;
        int actual = queryManager.getNumberOfObjects(IS_STUDENT_REGISTERED_FOR_COURSE_QUERY, course.getId(), student.getId());

        assertEquals(expected, actual);
    }

    @Test
    void getTasksFromCourse() {
        List<Course> courses = studentDao.readAllByStudentId(student.getId());
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        List<Task> expected = taskDao.readAllByCourseId(course.getId());
        List<Task> actual = service.getTasksFromCourse(course);

        assertIterableEquals(expected, actual);
    }

    @Test
    void getSolution() {
        List<Solution> solutions = solutionDao.readAll();
        Solution expected = solutions.get(RANDOM.nextInt(solutions.size()));
        Task task = expected.getTask();
        Student student = expected.getStudent();
        Solution actual = service.getSolution(task, student);

        assertEquals(expected, actual);

        Solution newSolution = service.getSolution(task, student);
        assertNotNull(newSolution);
    }

    @Test
    void solveTask() {
        List<Solution> solutions = solutionDao.readAll();
        Solution solution = solutions.get(RANDOM.nextInt(solutions.size()));
        try {
            service.solveTask(solution, true, UPDATE);
        } catch (Exceptions.SolutionIsResolvedException e) {
            e.printStackTrace();
        }
        Solution actual = service.getSolution(solution.getTask(), solution.getStudent());

        assertEquals(UPDATE, actual.getResponse());

        ProfessorService professorService = new ProfessorServiceImpl();
        professorService.review(solution, MARK, REVIEW);

        assertThrows(Exceptions.SolutionIsResolvedException.class, () -> service.solveTask(solution, true, UPDATE));
    }
}