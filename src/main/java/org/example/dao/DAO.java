package org.example.dao;

import java.util.List;

public interface DAO<T> {

    void create(T t);

    T read(Object id);

    void update(T t);

    void delete(Object id);

    List<T> readAll();
}
