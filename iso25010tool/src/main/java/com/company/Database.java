package com.company;

import java.sql.*;

public class Database {
    private static Database ourInstance = new Database();

    private Connection c;
    private Statement stmt;

    public static Database getInstance() {
        return ourInstance;
    }

    private Database() {
    }

    private boolean connect(){
        try {
            Class.forName("org.postgresql.Driver");
            c = DriverManager
                    .getConnection("jdbc:postgresql://localhost:5432/thesis",
                            "postgres", "postgres");

        } catch (Exception e) {
            e.printStackTrace();
            System.err.println(e.getClass().getName()+": "+e.getMessage());
            System.exit(0);
        }
        return true;
    }

    public void executeUpdate(String sql){
        if (connect()){
            try {
                stmt= c.createStatement();
                stmt.executeUpdate(sql);
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public ResultSet executeQuery(String sql){
        ResultSet rs = null;
        if (connect()){
            try {
                stmt= c.createStatement();
                rs = stmt.executeQuery(sql);
                c.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return rs;
    }

    public int getProjectId(String project){
        int id=0;
        String sql = "SELECT id from projects where name='"+project+"';";
        ResultSet resultSet=executeQuery(sql);
        try {
            if (resultSet.next())id=resultSet.getInt(1);
            else {
                executeUpdate("INSERT INTO projects(name) VALUES ('" + project + "')");
                resultSet =executeQuery(sql);
                resultSet.next();
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return id;
    }
}
