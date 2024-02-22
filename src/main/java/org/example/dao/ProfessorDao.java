package org.example.dao;

import org.example.pojo.Professor;

import javax.persistence.NoResultException;

public interface ProfessorDao extends DAO<Professor, Long> {

    Professor read(String email)throws NoResultException;

    Professor getByEmail(String oldEmail);

    void deleteByEmail(String email);
}
