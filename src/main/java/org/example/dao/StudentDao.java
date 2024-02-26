package org.example.dao;

import org.example.pojo.Course;
import org.example.pojo.Student;

import javax.persistence.NoResultException;
import java.util.List;

public interface StudentDao extends DAO<Student, Long> {

    Long DELETED_STUDENT_ID = 1L;

    Student getByEmail(String email) throws NoResultException;

    List<Course> readAllByStudentId(Long studentId) throws NoResultException;
}
