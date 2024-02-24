package org.example.dao;

import org.example.pojo.Task;

import javax.persistence.NoResultException;
import java.util.List;

public interface TaskDao extends DAO<Task, Long> {

    long DELETED_TASK_ID = 1L;

    List<Task> readAllByCourseId(Long courseId) throws NoResultException;

    List<Task> readAllByStudentId(Long studentId) throws NoResultException;
}
