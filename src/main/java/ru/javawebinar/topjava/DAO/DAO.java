package ru.javawebinar.topjava.DAO;

public interface DAO<T> {
    void add(T obj);

    T getById(Integer id);

    void update(T obj);

    void delete(Integer id);
}
