package org.example.dao.impl.entityImpl;

import org.example.dao.ProfessorDao;
import org.example.dao.impl.ProfessorDaoImpl;
import org.example.pojo.Professor;
import org.example.utils.HibernateUtil;
import org.example.utils.MockUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.example.utils.MockConstants.DELETE_ALL_PROFESSORS;
import static org.example.utils.MockConstants.UPDATE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class ProfessorDaoImplTest {
    private final ProfessorDao professorDao = new ProfessorDaoImpl();

    @AfterAll
    public static void deleteAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query deleteAll = manager.createNativeQuery(DELETE_ALL_PROFESSORS);
        deleteAll.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    @Test
    void create() {
        Professor expected = MockUtils.generateProfessor();
        professorDao.create(expected);
        Long id = expected.getId();
        Professor actual = professorDao.read(id);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Professor professor = MockUtils.generateProfessor();
        professorDao.create(professor);
        professor.setName(UPDATE);
        Long id = professor.getId();
        professorDao.update(professor);
        String actual = professorDao.read(id).getName();

        assertEquals(UPDATE, actual);
    }

    @Test
    void delete() {
        Professor professor = MockUtils.generateProfessor();
        professorDao.create(professor);
        Long id = professor.getId();
        professorDao.delete(id);
        professorDao.delete(0L);
        Professor actual = professorDao.read(id);

        assertNull(actual);
    }
}