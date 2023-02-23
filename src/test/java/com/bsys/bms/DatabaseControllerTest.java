package com.bsys.bms;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class DatabaseControllerTest {
    private DatabaseController dbController;

    @BeforeEach
    public void setUp() {
        dbController = new DatabaseController();
    }

    @AfterEach
    public void tearDown() {
        dbController.closeConnection();
    }

    @Test
    public void testExecuteSelectQuery() throws SQLException {
        // Given
        String query = "SELECT * FROM booking WHERE id = 1";

        // When
        ResultSet resultSet = dbController.executeSelectQuery(query);

        // Then
        assertNotNull(resultSet);
        assertTrue(resultSet.next());
    }
}