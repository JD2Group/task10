package org.example.servise.impl;

import org.example.dao.CourseDao;
import org.example.dao.ProfessorDao;
import org.example.dao.StudentDao;
import org.example.dao.impl.CourseDaoImpl;
import org.example.dao.impl.ProfessorDaoImpl;
import org.example.dao.impl.StudentDaoImpl;
import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.pojo.Student;
import org.example.servise.AdministratorServ;
import org.example.utils.HibernateUtil;
import org.example.utils.MockUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class AdministratorServiceImplTest {
    private AdministratorServ service = new AdministratorServiceImpl();

    @BeforeAll
    public static void saveStudent() {
        CourseDao courseDao = new CourseDaoImpl();
        StudentDao studentDao = new StudentDaoImpl();
        ProfessorDao professorDao = new ProfessorDaoImpl();

        Course stubForDeletedCourses = Course.builder()
                .title("Deleted")
                .build();
        courseDao.create(stubForDeletedCourses);

       Student stubForDeletedStudent = Student.builder()
                .name("deleted")
                .surname("deleted")
                .email("deleted")
                .build();

        Professor professor = MockUtils.generateProfessor();
        professorDao.create(professor);

        List<Course> courses = new ArrayList<>(MockUtils.generateRandomCourses());
        courses.forEach(c -> {
            courseDao.create(c);
            c.setProfessor(professor);
            courseDao.update(c);
        });

        studentDao.create(stubForDeletedStudent);
        List<Student> students = new ArrayList<>(MockUtils.generateRandomStudents());
        students.forEach(s -> {
            studentDao.create(s);
            s.addCourse(courses.get(RANDOM.nextInt(courses.size())));
        });
    }

    @Test
    void createProfessorAccount() {
        int randomNumber = RANDOM.nextInt(MAX_RANDOM_NUMBER);
        String expectedName = PROFESSOR_NAME + randomNumber;
        String expectedSurname = PROFESSOR_SURNAME + randomNumber;
        String expectedEmail = String.format(PROFESSOR_EMAIL, randomNumber) + RANDOM.nextInt(MAX_RANDOM_NUMBER);
        service.createProfessorAccount(expectedName, expectedSurname, expectedEmail);
        Professor actual = service.getProfessorByEmail(expectedEmail);

        assertEquals(expectedName, actual.getName());
        assertEquals(expectedSurname, actual.getSurname());
        assertEquals(expectedEmail, actual.getEmail());
    }

    @Test
    void createStudentAccount() {
        int randomNumber = RANDOM.nextInt(MAX_RANDOM_NUMBER);
        String expectedName = STUDENT_NAME + randomNumber;
        String expectedSurname = STUDENT_SURNAME + randomNumber;
        String expectedEmail = String.format(STUDENT_EMAIL, randomNumber) + RANDOM.nextInt(MAX_RANDOM_NUMBER);
        service.createProfessorAccount(expectedName, expectedSurname, expectedEmail);
        Student actual = service.getStudentByEmail(expectedEmail);

        assertEquals(expectedName, actual.getName());
        assertEquals(expectedSurname, actual.getSurname());
        assertEquals(expectedEmail, actual.getEmail());
    }

    @Test
    void deleteStudentAccount() {
        Student student = MockUtils.generateStudent();
        service.createStudentAccount(student.getName(), student.getSurname(), student.getEmail());
        service.deleteAccount(student);
        Student actual = service.getStudentByEmail(student.getEmail());

        assertNull(actual);
    }

    @Test
    void deleteProfessorAccount() {
        Professor professor = MockUtils.generateProfessor();
        service.createStudentAccount(professor.getName(), professor.getSurname(), professor.getEmail());
        service.deleteAccount(professor);
        Professor actual = service.getProfessorByEmail(professor.getEmail());

        assertNull(actual);
    }

    @Test
    void getAllStudents() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query numberOfStudentsQuery = manager.createNativeQuery(GET_AMOUNT_OF_STUDENTS_QUERY);
        BigInteger expected = (BigInteger) numberOfStudentsQuery.getSingleResult();
        manager.close();
        List<Student> actual = service.getAllStudents();

        assertEquals(expected.intValue() - 1, actual.size());
    }

    @Test
    void getAllStudentsByCourse() {
        Course course = service.getAllCourses().get(1);
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query numberOfStudentsQuery = manager.createNativeQuery(GET_AMOUNT_OF_STUDENTS_BY_COURSE_QUERY);
        numberOfStudentsQuery.setParameter(1, course.getId());
        BigInteger expected = (BigInteger) numberOfStudentsQuery.getSingleResult();
        manager.close();
        List<Student> actual = service.getAllStudents(course);

        assertEquals(expected.intValue(), actual.size());
    }

    @Test
    void getAllProfessors() {
    }

    @Test
    void getAllCourses() {
    }

    @Test
    void testGetAllCourses() {
    }

    @Test
    void createCourse() {
    }

    @Test
    void updateCourse() {
    }

    @Test
    void testUpdateCourse() {
    }

    @Test
    void deleteCourse() {
    }

    @Test
    void getCourseByTitleAndProfEmail() {
    }
}