package org.example.dao;

import org.example.pojo.Student;

import javax.persistence.NoResultException;

public interface StudentDao extends DAO<Student,Long> {

    Student read(String email)throws NoResultException;
}
