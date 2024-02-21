package org.example.services;

import org.example.dao.CourseDao;
import org.example.dao.ProfessorDao;
import org.example.dao.impl.entityImpl.CourseDaoImpl;
import org.example.dao.impl.entityImpl.ProfessorDaoImpl;
import org.example.pojo.Course;
import org.example.pojo.Professor;
import org.example.utils.HibernateUtil;
import org.example.utils.MockUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import static org.example.utils.MockConstants.DELETE_ALL_COURSES;
import static org.example.utils.MockConstants.DELETE_ALL_PROFESSORS;
import static org.junit.jupiter.api.Assertions.*;

public class AdministratorServiceTest {
    private final CourseDao courseDao = new CourseDaoImpl();
    private final ProfessorDao professorDao = new ProfessorDaoImpl();
    private Course course;
    private Professor professor;

    @AfterAll
    public static void deleteAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query deleteAllCourses = manager.createNativeQuery(DELETE_ALL_COURSES);
        deleteAllCourses.executeUpdate();
        manager.flush();
        Query deleteAllProfessors = manager.createNativeQuery(DELETE_ALL_PROFESSORS);
        deleteAllProfessors.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    @BeforeEach
    public void createData() {
        course = MockUtils.generateCourse();
        professor = MockUtils.generateProfessor();
        courseDao.create(course);
        professorDao.create(professor);
    }  

    @Test
    public void setProfessorTest() {
        course.setProfessor(professor);
        courseDao.update(course);

        Course actualCourse = courseDao.read(course.getId());
        Professor actualProfessor = professorDao.read(professor.getId());

        assertEquals(course, actualCourse);
        assertEquals(professor, actualProfessor);
        assertEquals(professor, actualCourse.getProfessor());
    }

    @Test
    public void deleteProfessorTest() {
        Set<Course> courses = MockUtils.generateRandomCourses();
        courses.forEach(c -> {
                    courseDao.create(c);
                    c.setProfessor(professor);
                    courseDao.update(c);
                });
        professorDao.delete(professor.getId());
        Professor actual = professorDao.read(professor.getId());
        List<Course> actualCourses = courses.stream()
                .filter(c -> c.getProfessor() == null)
                .collect(Collectors.toList());

        assertNull(actual);
        assertTrue(actualCourses.isEmpty());
    }
}
