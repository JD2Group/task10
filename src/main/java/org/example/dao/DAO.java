package org.example.dao;

import java.sql.SQLException;
import java.util.List;

public interface DAO<T,R> {

    T create(T t);

    T read(R id);

    void update(T t);

    void delete(R id);

    List<T> readAll();
}
