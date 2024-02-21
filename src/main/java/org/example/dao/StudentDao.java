package org.example.dao;

import org.example.pojo.Student;

public interface StudentDao extends DAO<Student,Long> {

    Student read(String name, String surname);
}
