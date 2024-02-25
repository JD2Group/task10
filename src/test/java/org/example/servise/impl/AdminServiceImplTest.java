package org.example.servise.impl;

import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.pojo.Student;
import org.example.servise.AdminService;
import org.example.utils.MockUtils;
import org.example.utils.QueryManager;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import javax.persistence.NoResultException;
import java.util.List;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class AdminServiceImplTest {
    private AdminService service = new AdminServiceImpl();

    @BeforeAll
    public static void createData() {
        QueryManager.createTestData(PROFESSOR_EMAIL_ADMIN_TEST);
    }

    @AfterAll
    public static void deleteAll() {
//       QueryManager.deleteAll();
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
    }

    @Test
    void deleteProfessorAccount() {
        Professor professor = MockUtils.generateProfessor();
        professor = service.createProfessorAccount(professor.getName(), professor.getSurname(), professor.getEmail());
        String email = professor.getEmail();
        service.deleteAccount(professor);

        assertThrows(NoResultException.class, () -> service.getProfessorByEmail(email));
    }

    @Test
    void getAllStudents() {
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_STUDENTS_QUERY);
        List<Student> actual = service.getAllStudents();

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllStudentsByCourse() {
        Course course = service.getAllCourses().get(1);
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_STUDENTS_BY_COURSE_QUERY, course.getId());
        List<Student> actual = service.getAllStudents(course);

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllProfessors() {
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_PROFESSORS_QUERY);
        List<Professor> actual = service.getAllProfessors();

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllCourses() {
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_QUERY);
        List<Course> actual = service.getAllCourses();

        assertEquals(expected, actual.size());
    }

    @Test
    void getAllCoursesByProfessor() {
        Professor professor = service.getProfessorByEmail(PROFESSOR_EMAIL_ADMIN_TEST);
        int expected = QueryManager.getNumberOfObjects(GET_NUMBER_OF_COURSES_BY_PROFESSOR_QUERY, professor.getId());
        List<Course> actual = service.getAllCourses(professor);

        assertEquals(expected, actual.size());
    }

    @Test
    void createCourse() {
        Professor professor = service.getProfessorByEmail(PROFESSOR_EMAIL_ADMIN_TEST);
        String courseTitle = COURSE_TITLE_PATTERN + RANDOM.nextInt(MAX_RANDOM_NUMBER);
        service.createCourse(courseTitle, professor);
        Course actual = service.getCoursesByTitleAndProfEmail(courseTitle, PROFESSOR_EMAIL_ADMIN_TEST).get(0);

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
        List<Course> courses = service.getAllCourses();
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        Professor expected = service.createProfessorAccount(PROFESSOR_NAME_PATTERN, PROFESSOR_SURNAME_PATTERN, PROFESSOR_EMAIL_PATTERN);
        service.updateCourse(course, expected);
        Professor actual = service.getCoursesByTitleAndProfEmail(course.getTitle(), course.getProfessor().getEmail()).get(0).getProfessor();

        assertEquals(expected, actual);
    }

    @Test
    void deleteCourse() {
        List<Course> courses = service.getAllCourses();
        Course course = courses.get(RANDOM.nextInt(courses.size()));
        service.deleteCourse(course);
        List<Course> actual = service.getCoursesByTitleAndProfEmail(course.getTitle(), course.getProfessor().getEmail());

        assertTrue(actual.isEmpty());
    }
}