package org.example.db;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbConnection {

    public static final String URL = "";
    public static final String USERNAME = "";
    public static final String PASSWORD = "";

    static {
        try {
            Class.forName("");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USERNAME, PASSWORD);
    }


}
