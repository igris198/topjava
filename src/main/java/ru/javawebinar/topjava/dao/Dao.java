package ru.javawebinar.topjava.dao;

import java.util.List;

public interface Dao<T> {
    T add(T obj);

    T getById(int id);

    T update(T obj);

    void delete(int id);

    List<T> getAll();
}
