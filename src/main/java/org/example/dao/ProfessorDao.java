package org.example.dao;

import org.example.pojo.Professor;

import javax.persistence.NoResultException;

public interface ProfessorDao extends DAO<Professor, Long> {

    Professor getByEmail(String email) throws NoResultException;
}
