package org.example.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T> {

    void create(T t) throws SQLException;

    T read(Object id) throws SQLException;

    void update(T t) throws SQLException;

    void delete(Object id) throws SQLException;

    List<T> readAll() throws SQLException;
}
