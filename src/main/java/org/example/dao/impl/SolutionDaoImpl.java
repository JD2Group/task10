package org.example.dao.impl;

import org.example.dao.SolutionDao;
import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;

import javax.persistence.EntityNotFoundException;
import javax.persistence.TypedQuery;
import java.util.List;

public class SolutionDaoImpl extends DaoImpl<Solution, Long> implements SolutionDao {

    public SolutionDaoImpl() {

        super(Solution.class);
    }

    @Override
    public Solution getByStudentTask(Student student, Task task) throws EntityNotFoundException {

        Solution solution;
        String sqlQuery = String.format("SELECT s FROM Solution s WHERE s.student=%d AND s.task=%d",
            student.getId(), task.getId());
        getEm().getTransaction().begin();
        TypedQuery<Solution> query = getEm().createQuery(sqlQuery, Solution.class);
        solution = query.getSingleResult();
        getEm().getTransaction().commit();
        return solution;
    }

    @Override
    public List<Solution> getAllReadyByTask(Task task) {

        String sqlQuery = String.format("SELECT s FROM Solution s WHERE s.task=%d AND s.readyForReview=true",
            task.getId());
        getEm().getTransaction().begin();
        TypedQuery<Solution> query = getEm().createQuery(sqlQuery, Solution.class);
        List<Solution> solutions = query.getResultList();
        getEm().getTransaction().commit();
        return solutions;
    }
}
