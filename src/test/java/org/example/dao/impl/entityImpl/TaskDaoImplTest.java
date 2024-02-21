package org.example.dao.impl.entityImpl;

import org.example.dao.TaskDao;
import org.example.pojo.Task;
import org.example.utils.HibernateUtil;
import org.example.utils.MockUtils;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import static org.example.utils.MockConstants.*;
import static org.junit.jupiter.api.Assertions.*;

class TaskDaoImplTest {
    private final TaskDao taskDao = new TaskDaoImpl();

    @AfterAll
    public static void deleteAll() {
        EntityManager manager = HibernateUtil.getEntityManager();
        manager.getTransaction().begin();
        Query deleteAll = manager.createNativeQuery(DELETE_ALL_TASK);
        deleteAll.executeUpdate();
        manager.getTransaction().commit();
        manager.close();
    }

    @Test
    void create() {
        Task expected = MockUtils.generateTask();
        taskDao.create(expected);
        Long id = expected.getId();
        Task actual = taskDao.read(id);

        assertEquals(expected, actual);
    }

    @Test
    void update() {
        Task task = MockUtils.generateTask();
        taskDao.create(task);
        task.setTitle(UPDATE);
        Long id = task.getId();
        taskDao.update(task);
        String actual = taskDao.read(id).getTitle();

        assertEquals(UPDATE, actual);
    }

    @Test
    void delete() {
        Task task = MockUtils.generateTask();
        taskDao.create(task);
        Long id = task.getId();
        taskDao.delete(id);
        taskDao.delete(0L);
        Task actual = taskDao.read(id);

        assertNull(actual);
    }
}