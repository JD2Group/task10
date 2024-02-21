package org.example.dao.impl.entityImpl;



import org.example.dao.TaskDao;
import org.example.dao.impl.DaoImpl;
import org.example.pojo.Professor;
import org.example.pojo.Task;

import javax.persistence.EntityManager;
import javax.persistence.EntityNotFoundException;

public class TaskDaoImpl extends DaoImpl<Task> implements TaskDao {

    public static final long DELETED_TASK_ID = 1L;
    private EntityManager em;

    public TaskDaoImpl() {

        super(Task.class);
        this.em = getEm();
    }

    @Override
    public void delete(Object id) {

        Task task = super.read(id);
        Task deleted=super.read(DELETED_TASK_ID);
        try {
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
            }
        } catch (EntityNotFoundException e) {
            em.getTransaction().rollback();
            System.out.println("Entity not found!");
        }
    }
}
