package org.example.dao.impl;

import org.example.dao.SolutionDao;
import org.example.dao.StudentDao;
import org.example.dao.TaskDao;
import org.example.pojo.Solution;
import org.example.pojo.Task;

import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import java.util.ArrayList;
import java.util.List;

public class SolutionDaoImpl extends DaoImpl<Solution, Long> implements SolutionDao {

    public static final String GET_SOLUTION_BY_STUDENT_AND_TASK = "SELECT s FROM Solution s WHERE s.student=%d AND s.task=%d";
    public static final String GET_SOLUTIONS_BY_TASK = "SELECT s FROM Solution s WHERE s.task=%d AND s.readyForReview=true";

    public SolutionDaoImpl() {

        super(Solution.class);
    }

    @Override
    public Solution getByStudentTask(Long studentId, Long taskId) throws NoResultException {

        if (studentId != null && taskId != null) {
            String sqlQuery = String.format(GET_SOLUTION_BY_STUDENT_AND_TASK,
                studentId, taskId);
            TypedQuery<Solution> query = getEm().createQuery(sqlQuery, Solution.class);
            return query.getSingleResult();
        } else {
            throw new NoResultException();
        }
    }

    @Override
    public List<Solution> getAllReadyByTask(Task task) {

        List<Solution> list = new ArrayList<>();
        if (task != null) {
            String sqlQuery = String.format(GET_SOLUTIONS_BY_TASK,
                task.getId());
            TypedQuery<Solution> query = getEm().createQuery(sqlQuery, Solution.class);
            list = query.getResultList();
        }
        return list;
    }

    @Override
    public void deleteSolutionsWithoutStudentIdAndTaskId() {

        String sqlQuery = String.format(GET_SOLUTION_BY_STUDENT_AND_TASK,
            StudentDao.DELETED_STUDENT_ID, TaskDao.DELETED_TASK_ID);
        TypedQuery<Solution> query = getEm().createQuery(sqlQuery, Solution.class);
        List<Solution> list = query.getResultList();

        getEm().getTransaction().begin();
        list.forEach(solution -> getEm().remove(solution));
        getEm().getTransaction().commit();
    }
}
