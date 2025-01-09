package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import com.teamtreehouse.techdegrees.exc.AppError;
import com.teamtreehouse.techdegrees.model.Todo;
import org.sql2o.Sql2o;
import java.util.HashMap;
import java.util.Map;
import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        Sql2o sql2o = new Sql2o("jdbc:h2:~/todoTable.db;INIT=RUNSCRIPT from 'classpath:db/init.sql'", "", "");
        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();
        staticFileLocation("/public");

        // POST Route to create a todo
        post("/api/v1/todos", "application/json", (req, res) -> {
            Todo todo = gson.fromJson(req.body(), Todo.class);
            todoDao.create(todo);
            res.status(201);

            return todo;
        }, gson::toJson);

        // GET Route to fetch all the todos in the list
        get("/api/v1/todos", "application/json", (req, res) ->
                todoDao.findAll(),
                gson::toJson);

        // GET Route to fetch a specific todo
        get("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Todo todo = todoDao.findById(id);
            if (todo == null) {
                throw new AppError(404, "Could not find the todo with this id: " + id);
            }
            return todo;
        }, gson::toJson);

        exception(AppError.class, (exc, req, res) -> {
            AppError err = (AppError) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatus());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatus());
            res.body(gson.toJson(jsonMap));
        });

        // PUT Route to update an existing todo in the list
        put("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Todo existingTodo = todoDao.findById(id);
            Todo updatedTodo = gson.fromJson(req.body(), Todo.class);
            if (existingTodo == null) {
                throw new AppError(404, "Could not find the todo with this id to update " + id);
            }
            existingTodo.setId(updatedTodo.getId());
            existingTodo.setName(updatedTodo.getName());
            existingTodo.setCompleted(updatedTodo.isCompleted());
            todoDao.update(updatedTodo);
            return updatedTodo;
        }, gson::toJson);

        exception(AppError.class, (exc, req, res) -> {
            AppError err = (AppError) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatus());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatus());
            res.body(gson.toJson(jsonMap));
        });

        // DELETE Route to delete a todo
        delete("/api/v1/todos/:id", "application/json", (req, res) -> {
            int id = Integer.parseInt(req.params("id"));
            Todo todo = todoDao.findById(id);
            if (todo == null) {
                throw new AppError(404, "Could not find the todo with this id to delete " + id);
            }
            todoDao.delete(todo);
            res.status(204);
            return "";
        });

        exception(AppError.class, (exc, req, res) -> {
            AppError err = (AppError) exc;
            Map<String, Object> jsonMap = new HashMap<>();
            jsonMap.put("status", err.getStatus());
            jsonMap.put("errorMessage", err.getMessage());
            res.type("application/json");
            res.status(err.getStatus());
            res.body(gson.toJson(jsonMap));
        });
    }
}
