package org.example.servise.impl;

import org.example.dao.CourseDao;
import org.example.dao.SolutionDao;
import org.example.dao.StudentDao;
import org.example.dao.TaskDao;
import org.example.dao.impl.CourseDaoImpl;
import org.example.dao.impl.SolutionDaoImpl;
import org.example.dao.impl.StudentDaoImpl;
import org.example.dao.impl.TaskDaoImpl;
import org.example.pojo.*;
import org.example.servise.AdminService;
import org.example.utils.MockUtils;
import org.example.utils.QueryManager;
import org.junit.jupiter.api.*;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import java.util.List;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class AdminServiceImplTest {
    private AdminService service = new AdminServiceImpl();
    private QueryManager queryManager = new QueryManager();

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

    @AfterEach
    public void closeQueryManager() {
        queryManager.closeSession();
    }

    @Test
    void createProfessorAccount() {
        int randomNumber = RANDOM.nextInt(MAX_RANDOM_NUMBER);
        String expectedName = PROFESSOR_NAME_PATTERN + randomNumber;
        String expectedSurname = PROFESSOR_SURNAME_PATTERN + randomNumber;
        String expectedEmail = String.format(PROFESSOR_EMAIL_PATTERN, randomNumber) + RANDOM.nextInt(MAX_RANDOM_NUMBER);
        service.createProfessorAccount(expectedName, expectedSurname, expectedEmail);
        Professor actual = service.getProfessorByEmail(expectedEmail);

        assertEquals(expectedName, actual.getName());
        assertEquals(expectedSurname, actual.getSurname());
        assertEquals(expectedEmail, actual.getEmail());
    }

    @Test
    void createStudentAccount() {
        int randomNumber = RANDOM.nextInt(MAX_RANDOM_NUMBER);
        String expectedName = STUDENT_NAME_PATTERN + randomNumber;
        String expectedSurname = STUDENT_SURNAME_PATTERN + randomNumber;
        String expectedEmail = String.format(STUDENT_EMAIL_PATTERN, randomNumber) + RANDOM.nextInt(MAX_RANDOM_NUMBER);
        service.createStudentAccount(expectedName, expectedSurname, expectedEmail);
        Student actual = service.getStudentByEmail(expectedEmail);

        assertEquals(expectedName, actual.getName());
        assertEquals(expectedSurname, actual.getSurname());
        assertEquals(expectedEmail, actual.getEmail());
    }

    @Test
    void deleteStudentAccount() {
        Student student = MockUtils.generateStudent();
        student = service.createStudentAccount(student.getName(), student.getSurname(), student.getEmail());
        String email = student.getEmail();
        service.deleteAccount(student);

        assertThrows(NoResultException.class, () -> service.getStudentByEmail(email));
        Student studentForDelete = MockUtils.generateStudent();
        studentForDelete.setId(ID);
        assertThrows(EntityNotFoundException.class, () -> service.deleteAccount(studentForDelete));
    }

    @Test
    void deleteProfessorAccount() {
        Professor professor = MockUtils.generateProfessor();
        professor = service.createProfessorAccount(professor.getName(), professor.getSurname(), professor.getEmail());
        String email = professor.getEmail();
        service.deleteAccount(professor);

        assertThrows(NoResultException.class, () -> service.getProfessorByEmail(email));
        Professor professorForDelete = MockUtils.generateProfessor();
        professorForDelete.setId(ID);
        assertThrows(EntityNotFoundException.class, () -> service.deleteAccount(professorForDelete));
    }

    @Test
    void getAllStudents() {
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_STUDENTS_QUERY);
        List<Student> actual = service.getAllStudents();

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllStudentsByCourse() {
        Course course = service.getAllCourses().get(1);
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_STUDENTS_BY_COURSE_QUERY, course.getId());
        List<Student> actual = service.getAllStudents(course);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllProfessors() {
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_PROFESSORS_QUERY);
        List<Professor> actual = service.getAllProfessors();

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllCourses() {
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_QUERY);
        List<Course> actual = service.getAllCourses();

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllCoursesByProfessor() {
        Professor professor = service.getProfessorByEmail(PROFESSOR_EMAIL);
        int expected = queryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_BY_PROFESSOR_QUERY, professor.getId());
        List<Course> actual = service.getAllCourses(professor);

        assertEquals(expected, actual.size());
    }

    @Test
    void createCourse() {
        Professor professor = service.getProfessorByEmail(PROFESSOR_EMAIL);
        String courseTitle = COURSE_TITLE_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER);
        service.createCourse(courseTitle, professor);
        Course actual = service.getCoursesByTitleAndProfEmail(courseTitle, PROFESSOR_EMAIL).get(0);

        assertEquals(courseTitle, actual.getTitle());
        assertEquals(professor, actual.getProfessor());
    }

    @Test
    void updateCourseTitle() {
        List<Course> courses = service.getAllCourses();
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        service.updateCourse(course, UPDATE);
        String actual = service.getCoursesByTitleAndProfEmail(UPDATE, course.getProfessor().getEmail()).get(0).getTitle();

        assertEquals(UPDATE, actual);
    }

    @Test
    void updateCourseProfessor() {
        CourseDao courseDao = new CourseDaoImpl();
        List<Course> courses = service.getAllCourses();
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        Professor expected = service.createProfessorAccount(PROFESSOR_NAME_PATTERN, PROFESSOR_SURNAME_PATTERN, PROFESSOR_EMAIL_PATTERN);
        service.updateCourse(course, expected);
        Course courseAfterUpdate = courseDao.read(course.getId());
        Professor actual = courseAfterUpdate.getProfessor();

        assertEquals(expected, actual);
    }

    @Test
    void deleteCourse() {
        CourseDao courseDao = new CourseDaoImpl();
        Course course = service.createCourse(COURSE_TITLE_PATTERN + RANDOM.nextInt(), service.getProfessorByEmail(PROFESSOR_EMAIL));
        service.deleteCourse(course);
        Course actual = courseDao.read(course.getId());

        assertNull(actual);
        courseDao.closeManager();
        assertThrows(EntityNotFoundException.class, () -> service.deleteCourse(course));
    }

    @Test
    void clearSolutions() {
        SolutionDao solutionDao = new SolutionDaoImpl();
        StudentDao studentDao = new StudentDaoImpl();
        TaskDao taskDao = new TaskDaoImpl();
        List<Solution> solutions = solutionDao.readAll();
        solutions.stream()
                .map(Solution::getStudent)
                .distinct()
                .forEach(s -> studentDao.delete(s.getId()));
        solutions.stream()
                .map(Solution::getTask)
                .distinct()
                .forEach(t -> taskDao.delete(t.getId()));
        service.clearBaseFromSolutionsWithoutStudentIdAndTaskId();
        List<Solution> actual = solutionDao.readAll();

        assertTrue(actual.isEmpty());
        solutionDao.closeManager();
        studentDao.closeManager();
        taskDao.closeManager();
    }

}