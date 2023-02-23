package com.bsys.bms;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * DatabaseController is a singleton class that provides a connection to the database.
 * It contains methods to execute select, update, insert, and close queries.
 */
public class DatabaseController {
    private static final String JDBC_DRIVER = "com.mysql.cj.jdbc.Driver";
    private static final String DB_URL = "jdbc:mysql://localhost:3306/bookingsys";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "";

    private Connection connection = null;

    /**
     * Private constructor to initialize the JDBC driver.
     */
    DatabaseController() {
        try {
            Class.forName(JDBC_DRIVER);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Returns a singleton instance of the connection to the database.
     * If the connection has not been established yet, it establishes the connection.
     *
     * @return the connection to the database
     */
    public Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection(DB_URL, USERNAME, PASSWORD);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return connection;
    }

    /**
     * Executes a SELECT query and returns the result set.
     *
     * @param query the SELECT query to be executed
     * @return the result set of the query
     */
    public ResultSet executeSelectQuery(String query) {
        ResultSet resultSet = null;
        try {
            Statement statement = getConnection().createStatement();
            resultSet = statement.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return resultSet;
    }

    /**
     * Executes an UPDATE query and returns the number of rows affected.
     *
     * @param query the UPDATE query to be executed
     * @return the number of rows affected by the query
     */
    public synchronized int executeUpdateQuery(String query) {
        int rowsAffected = 0;
        try {
            Statement statement = getConnection().createStatement();
            rowsAffected = statement.executeUpdate(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return rowsAffected;
    }

    /**
     * Executes an insert query and returns the generated ID.
     *
     * @param query the insert query to be executed
     * @return the generated ID, or -1 if an error occurs
     */
    public synchronized int executeInsertQuery(String query) {
        int id = -1;
        try {
            // Create a statement from the connection
            Statement statement = getConnection().createStatement();
            // Execute the query with the option to return generated keys
            statement.executeUpdate(query, Statement.RETURN_GENERATED_KEYS);
            // Get the generated keys result set
            ResultSet resultSet = statement.getGeneratedKeys();
            // If there is a generated key, assign it to the 'id' variable
            if (resultSet.next()) {
                id = resultSet.getInt(1);
            }
        } catch (SQLException e) {
            // Print the stack trace of the exception
            e.printStackTrace();
        }
        return id;
    }

    /**
     * Closes the connection to the database.
     */
    public void closeConnection() {
        if (connection != null) {
            try {
                // Close the connection
                connection.close();
            } catch (SQLException e) {
                // Print the stack trace of the exception
                e.printStackTrace();
            }
        }
    }
}