package com.teamtreehouse.techdegrees.dao;

import com.teamtreehouse.techdegrees.exc.DaoException;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;
import java.util.List;

public class Sql2oTodoDao implements TodoDao{
    private final Sql2o sql2o;

    public Sql2oTodoDao(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public List<Todo> findAll() throws DaoException {
        try(Connection connection = sql2o.open()) {
            return connection.createQuery("SELECT * FROM todoTable")
                    .executeAndFetch(Todo.class);
        } catch(Sql2oException ex) {
            ex.printStackTrace();
            throw new DaoException(ex, "Problem finding all tasks");
        }
    }

    @Override
    public Todo findById(int id) throws DaoException {
        String findByIdSql = "SELECT * FROM todoTable WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            return connection.createQuery(findByIdSql)
                    .addParameter("id", id)
                    .executeAndFetchFirst(Todo.class);
        } catch (Sql2oException ex) {
            ex.printStackTrace();
            throw new DaoException(ex, "Problem finding task by ID");
        }
    }

    @Override
    public void create(Todo todo) throws DaoException {
        String insertSql = "INSERT INTO todoTable(name, isCompleted) VALUES (:name, :isCompleted)";
        try (Connection connection = sql2o.open()) {
            int id = (int) connection.createQuery(insertSql)
                    .addParameter("name", todo.getName())
                    .addParameter("isCompleted", todo.isCompleted())
                    .executeUpdate()
                    .getKey();
            todo.setId(id);
        } catch (Sql2oException ex) {
            ex.printStackTrace();
            throw new DaoException(ex, "Problem creating task");
        }
    }

    @Override
    public void update(Todo todo) throws DaoException {
        String updateSql = "UPDATE todoTable SET name = :name, isCompleted = :isCompleted WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            connection.createQuery(updateSql)
                    .bind(todo)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            ex.printStackTrace();
            throw new DaoException(ex, "Problem updating task");
        }
    }


    @Override
    public void delete(Todo todo) throws DaoException {
        String deleteSql = "DELETE FROM todoTable WHERE id = :id";
        try (Connection connection = sql2o.open()) {
            connection.createQuery(deleteSql)
                    .bind(todo)
                    .executeUpdate();
        } catch (Sql2oException ex) {
            ex.printStackTrace();
            throw new DaoException(ex, "Problem deleting task");
        }

    }
}
