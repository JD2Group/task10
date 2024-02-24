package org.example.dao;

import org.example.pojo.Student;

import javax.persistence.NoResultException;

public interface StudentDao extends DAO<Student, Long> {

    Long DELETED_STUDENT_ID = 1L;

    Student getByEmail(String email) throws NoResultException;
}
