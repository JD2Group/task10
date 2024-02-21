package org.example.dao.impl.entityImpl;


import org.example.dao.SolutionDao;
import org.example.pojo.Solution;
import org.example.utils.HibernateUtil;
import org.example.utils.MockUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.example.utils.MockConstants.DELETE_ALL_SOLUTIONS;
import static org.example.utils.MockConstants.UPDATE;
import static org.junit.jupiter.api.Assertions.*;

class SolutionDaoImplTest {
    private final SolutionDao solutionDao = new SolutionDaoImpl();

    @AfterAll
    public static void deleteAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query deleteAll = manager.createNativeQuery(DELETE_ALL_SOLUTIONS);
        deleteAll.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    @Test
    void create() {
        Solution expected = MockUtils.generateSolution();
        solutionDao.create(expected);
        Long id = expected.getId();
        Solution actual = solutionDao.read(id);
        solutionDao.delete(id);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Solution solution = MockUtils.generateSolution();
        solutionDao.create(solution);
        solution.setResponse(UPDATE);
        Long id = solution.getId();
        solutionDao.update(solution);
        String actual = solutionDao.read(id).getResponse();

        assertEquals(UPDATE, actual);
    }

    @Test
    void delete() {
        Solution solution = MockUtils.generateSolution();
        solutionDao.create(solution);
        Long id = solution.getId();
        solutionDao.delete(id);
        solutionDao.delete(0L);
        Solution actual = solutionDao.read(id);

        assertNull(actual);
    }
}