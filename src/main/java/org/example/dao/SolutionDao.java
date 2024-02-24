package org.example.dao;

import org.example.pojo.Solution;
import org.example.pojo.Student;
import org.example.pojo.Task;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface SolutionDao extends DAO<Solution, Long> {

    Solution getByStudentTask(Student student, Task task) throws EntityNotFoundException;

    List<Solution> getAllReadyByTask(Task task);
}
