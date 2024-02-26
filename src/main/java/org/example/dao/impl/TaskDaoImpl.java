package org.example.dao.impl;

import org.example.dao.TaskDao;
import org.example.pojo.Task;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.List;

public class TaskDaoImpl extends DaoImpl<Task, Long> implements TaskDao {

    public static final String GET_TASKS_BY_COURSE = "SELECT c FROM Task c WHERE c.course=%d AND c.id NOT LIKE '%d'";
    public static final String GET_ALL_TASKS = "SELECT c FROM Task c WHERE c.id NOT LIKE '%d'";

    public TaskDaoImpl() {

        super(Task.class);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (id != null) {
            if (!TaskDao.DELETED_TASK_ID.equals(id)) {
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
    }

    @Override
    public List<Task> readAllByCourseId(Long courseId) throws NoResultException {

        String sqlQuery = String
                              .format(GET_TASKS_BY_COURSE,
                                  courseId, TaskDao.DELETED_TASK_ID);
        TypedQuery<Task> query = getEm().createQuery(sqlQuery, Task.class);
        return query.getResultList();
    }

    @Override
    protected String getAllSqlString() {

        return String.format(GET_ALL_TASKS, TaskDao.DELETED_TASK_ID);
    }
}
