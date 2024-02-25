package org.example.dao.impl.entityImpl;


import org.example.dao.CourseDao;
import org.example.pojo.Course;
import org.example.utils.HibernateUtil;
import org.example.utils.MockUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;
import javax.persistence.Query;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class CourseDaoImplTest {
    private final CourseDao courseDao = new CourseDaoImpl();


    @AfterAll
    public static void deleteAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query deleteAllFromStudentsCourses = manager.createNativeQuery(DELETE_ALL_STUDENTS_COURSES);
        deleteAllFromStudentsCourses.executeUpdate();
        manager.flush();
        Query deleteAll = manager.createNativeQuery(DELETE_ALL_COURSES);
        deleteAll.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    @Test
    public void createTest() {
        Course expected = MockUtils.generateCourse();
        courseDao.create(expected);
        Long id = expected.getId();
        Course actual = courseDao.read(id);

        assertEquals(expected, actual);
    }

    @Test
    public void update() {
        Course course = MockUtils.generateCourse();
        courseDao.create(course);
        course.setTitle(UPDATE);
        Long id = course.getId();
        courseDao.update(course);
        String actual = courseDao.read(id).getTitle();

        assertEquals(UPDATE, actual);
    }

    @Test
    public void delete() {
        Course course = MockUtils.generateCourse();
        courseDao.create(course);
        Long id = course.getId();
        courseDao.delete(id);
        Course actual = courseDao.read(id);

        assertNull(actual);
        assertThrows(EntityNotFoundException.class, () -> courseDao.delete(0L));
    }

//    Хмммм, и где же это будет?
//    @Test
//    public void readAll() {
//        List<Course> expected = new ArrayList<>(MockUtils.generateRandomCourses());
//        expected.forEach(courseDao::create);
//        List<Course> actual = courseDao.readAll();
//
//        assertIterableEquals(expected, actual);
//    }

}