package org.example.dao;

import javax.persistence.EntityNotFoundException;
import java.util.List;

public interface DAO<T, R> {

    T create(T t);

    T read(R id) throws EntityNotFoundException;

    T update(T t);

    void delete(R id) throws EntityNotFoundException;

    List<T> readAll();

    void closeManager();
}
