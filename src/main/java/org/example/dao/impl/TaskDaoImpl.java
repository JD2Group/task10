package org.example.dao.impl;

import org.example.dao.TaskDao;
import org.example.pojo.Task;
import org.example.utils.HibernateUtil;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class TaskDaoImpl extends DaoImpl<Task, Long> implements TaskDao {

    public static final long DELETED_TASK_ID = 1L;
    private static final EntityManager em = HibernateUtil.getEntityManager();

    public TaskDaoImpl() {

        super(Task.class, em);
    }

    @Override
    public void delete(Long id) {

        Task task = super.read(id);
        Task deleted = super.read(DELETED_TASK_ID);
        if (task != null) {
            em.getTransaction().begin();
            em.refresh(task);
            task.getSolutions().stream()
                .peek(solution -> solution.setTask(deleted))
                .forEach(em::merge);
            em.remove(task);
            em.getTransaction().commit();
        } else {
            System.out.println(String.format("%s with id=%s not found!", Task.class.getSimpleName(), id.toString()));
            throw new EntityNotFoundException() ;
        }
    }
}
