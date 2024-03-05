package org.example.dao.impl;

import org.apache.log4j.Logger;
import org.example.dao.TaskDao;
import org.example.pojo.Task;

import javax.persistence.EntityNotFoundException;
import javax.persistence.NoResultException;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import java.util.List;

import static org.example.utils.Constants.*;

public class TaskDaoImpl extends DaoImpl<Task, Long> implements TaskDao {

    public static final String GET_TASKS_BY_COURSE = "SELECT c FROM Task c WHERE c.course=%d AND c.id NOT LIKE '%d'";
    public static final String GET_ALL_TASKS = "SELECT c FROM Task c WHERE c.id NOT LIKE '%d'";
    private final Logger log = Logger.getLogger(TaskDaoImpl.class);

    public TaskDaoImpl() {

        super(Task.class);
    }

    @Override
    public void delete(Long id) throws EntityNotFoundException {

        if (DELETED_TASK_ID != id) {
            Task task = super.read(id);
            if (task != null) {
                getEm().getTransaction().begin();

                String JPQL = String.format("UPDATE solutions s SET s.task_id=%d WHERE s.task_id=%d", DELETED_TASK_ID, id);
                Query query = getEm().createNativeQuery(JPQL);
                query.executeUpdate();
                getEm().remove(task);
                try {
                    getEm().getTransaction().commit();
                    log.info(String.format(DELETE_MESSAGE, task.toString()));
                } catch (Exception e) {
                    getEm().getTransaction().rollback();
                    log.error(String.format(DELETE_FAILED_MESSAGE,  e.getCause()));
                }
            } else {
                log.error(String.format(ENTITY_NOT_FOUND_MESSAGE, id));
                throw new EntityNotFoundException();
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
