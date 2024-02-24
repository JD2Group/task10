package org.example.dao.impl;

import org.example.dao.TaskDao;
import org.example.pojo.Task;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class TaskDaoImpl extends DaoImpl<Task, Long> implements TaskDao {

    public TaskDaoImpl() {

        super(Task.class);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (id != DELETED_TASK_ID) {
            Task task = super.read(id);
            Task deleted = super.read(DELETED_TASK_ID);
            if (task != null) {
                getEm().getTransaction().begin();
                getEm().refresh(task);
                task.getSolutions().stream()
                    .peek(solution -> solution.setTask(deleted))
                    .forEach(getEm()::merge);
                getEm().remove(task);
                getEm().getTransaction().commit();
            } else {
                throw new EntityNotFoundException();
            }
        }
    }

    @Override
    public List<Task> readAllByCourseId(Long courseId) throws NoResultException {

        String sqlQuery = String
                              .format("SELECT c FROM Task c WHERE c.course=%d AND c.id NOT LIKE '%d'",
                                  courseId, TaskDao.DELETED_TASK_ID);
        getEm().getTransaction().begin();
        TypedQuery<Task> query = getEm().createQuery(sqlQuery, Task.class);
        List<Task> list = query.getResultList();
        getEm().getTransaction().commit();
        return list;
    }

    @Override
    public List<Task> readAllByStudentId(Long studentId) throws NoResultException {

        String sqlQuery = String
                              .format("SELECT t FROM Task t WHERE t.student=%d AND t.id NOT LIKE '%d'",
                                  studentId, TaskDao.DELETED_TASK_ID);
        getEm().getTransaction().begin();
        TypedQuery<Task> query = getEm().createQuery(sqlQuery, Task.class);
        List<Task> list = query.getResultList();
        getEm().getTransaction().commit();
        return list;
    }

    @Override
    protected String getAllSqlString() {

        return String.format("SELECT c FROM Task c WHERE c.id NOT LIKE '%d'", TaskDao.DELETED_TASK_ID);
    }
}
