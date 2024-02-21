package org.example.dao.impl.entityImpl;

import org.example.dao.StudentDao;
import org.example.pojo.Student;
import org.example.utils.HibernateUtil;
import org.example.utils.MockUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.example.utils.MockConstants.*;
import static org.example.utils.MockConstants.UPDATE;
import static org.junit.jupiter.api.Assertions.*;

class StudentDaoImplTest {
    private final StudentDao studentDao = new StudentDaoImpl();

    @AfterAll
    public static void deleteAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query deleteAll = manager.createNativeQuery(DELETE_ALL_STUDENTS);
        deleteAll.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    @Test
    void create() {
        Student expected = MockUtils.generateStudent();
        studentDao.create(expected);
        Long id = expected.getId();
        Student actual = studentDao.read(id);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Student student = MockUtils.generateStudent();
        studentDao.create(student);
        student.setName(UPDATE);
        Long id = student.getId();
        studentDao.update(student);
        String actual = studentDao.read(id).getName();

        assertEquals(UPDATE, actual);
    }

    @Test
    void delete() {
        Student student = MockUtils.generateStudent();
        studentDao.create(student);
        Long id = student.getId();
        studentDao.delete(id);
        studentDao.delete(0L);
        Student actual = studentDao.read(id);

        assertNull(actual);
    }
}