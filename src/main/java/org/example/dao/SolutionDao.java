package org.example.dao;

import org.example.pojo.Solution;
import org.example.pojo.Task;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface SolutionDao extends DAO<Solution, Long> {

    Solution getByStudentTask(Long studentId, Long taskId) throws EntityNotFoundException;

    List<Solution> getAllReadyByTask(Task task);

    void deleteSolutionsWithoutStudentIdAndTaskId();
}
