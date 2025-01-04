package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;

import java.util.List;

public interface TodoDao {
    List<Todo> findAll();
    Todo findById(int id);
    void create(Todo todo) throws DaoException;
    void update(Todo todo) throws DaoException;
    void delete(Todo todo) throws DaoException;
}
