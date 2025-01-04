package com.teamtreehouse.techdegrees;

import com.google.gson.Gson;
import com.teamtreehouse.techdegrees.dao.Sql2oTodoDao;
import com.teamtreehouse.techdegrees.dao.TodoDao;
import org.sql2o.Connection;
import org.sql2o.Sql2o;
import org.sql2o.Sql2oException;

import static spark.Spark.*;

public class App {

    public static void main(String[] args) {
        Sql2o sql2o = new Sql2o("jdbc:h2:~/todoTable.db;INIT=RUNSCRIPT from 'bin/main/resources/:db.init.sql'", "", "");
        System.out.println(System.getProperty("java.class.path"));

        TodoDao todoDao = new Sql2oTodoDao(sql2o);
        Gson gson = new Gson();
        staticFileLocation("/public");

        get("/blah", (req, res) -> "Hello!");

    }

}
