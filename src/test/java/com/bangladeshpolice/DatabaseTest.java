package com.bangladeshpolice;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * Database integration tests for Bangladesh Police Management System
 */
public class DatabaseTest {

    private DatabaseManager dbManager;

    @BeforeEach
    public void setUp() {
        dbManager = DatabaseManager.getInstance();
    }

    @AfterEach
    public void tearDown() {
        // Clean up test data if needed
    }

    @Test
    public void testDatabaseConnection() {
        Connection connection = dbManager.getConnection();
        assertNotNull(connection, "Database connection should not be null");
    }

    @Test
    public void testDatabaseInitialization() {
        try {
            dbManager.initializeDatabase();

            // Check if tables were created
            Connection conn = dbManager.getConnection();

            // Test users table
            var usersResult = conn.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='users'");
            assertTrue(usersResult.next(), "Users table should exist");

            // Test chat_rooms table
            var roomsResult = conn.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='chat_rooms'");
            assertTrue(roomsResult.next(), "Chat rooms table should exist");

            // Test messages table
            var messagesResult = conn.createStatement().executeQuery("SELECT name FROM sqlite_master WHERE type='table' AND name='messages'");
            assertTrue(messagesResult.next(), "Messages table should exist");

        } catch (SQLException e) {
            fail("Database initialization failed: " + e.getMessage());
        }
    }

    @Test
    public void testSampleDataLoading() {
        try {
            SampleDataLoader.loadSampleData();

            // Check if sample users were created
            var userResult = dbManager.getConnection().createStatement()
                .executeQuery("SELECT COUNT(*) as count FROM users");
            assertTrue(userResult.next(), "Should have users in database");
            assertTrue(userResult.getInt("count") > 0, "Should have at least one user");

        } catch (SQLException e) {
            fail("Sample data loading failed: " + e.getMessage());
        }
    }

    @Test
    public void testUserAuthentication() {
        try {
            // Test admin authentication
            boolean adminAuth = dbManager.authenticateUser("admin", "admin123");
            assertTrue(adminAuth, "Admin authentication should succeed");

            // Test invalid credentials
            boolean invalidAuth = dbManager.authenticateUser("admin", "wrongpassword");
            assertFalse(invalidAuth, "Invalid credentials should fail authentication");

        } catch (SQLException e) {
            fail("Authentication test failed: " + e.getMessage());
        }
    }
}