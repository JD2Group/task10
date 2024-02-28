package org.example.dao.impl;

import org.example.dao.TaskDao;
import org.example.pojo.Task;

import javax.persistence.EntityNotFoundException;

public class TaskDaoImpl extends DaoImpl<Task, Long> implements TaskDao {

    public static final long DELETED_TASK_ID = 1L;

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
                System.out.println(String.format("%s with id=%s not found!", Task.class.getSimpleName(), id.toString()));
                throw new EntityNotFoundException();
            }
        }
    }
}
