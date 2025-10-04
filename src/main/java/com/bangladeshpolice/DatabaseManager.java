package com.bangladeshpolice;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * Database Manager for Bangladesh Police Management System
 * Handles all database operations including user management, chat rooms, messages, files, and criminal records
 */
public class DatabaseManager {
    private static DatabaseManager instance;
    private Connection connection;
    private static final String DB_URL = "jdbc:sqlite:police_management.db";

    private DatabaseManager() {
        try {
            connection = DriverManager.getConnection(DB_URL);
            connection.setAutoCommit(true);
        } catch (SQLException e) {
            System.err.println("Failed to connect to database: " + e.getMessage());
        }
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    public void initializeDatabase() {
        try {
            // Create Users table
            createUsersTable();

            // Create Chat Rooms table
            createChatRoomsTable();

            // Create Room Participants table
            createRoomParticipantsTable();

            // Create Messages table
            createMessagesTable();

            // Create Files table
            createFilesTable();

            // Create Criminal Records table
            createCriminalRecordsTable();

            // Create Officer Locations table
            createOfficerLocationsTable();

            // Create Audit Log table
            createAuditLogTable();

            System.out.println("Database initialized successfully");

        } catch (SQLException e) {
            System.err.println("Error initializing database: " + e.getMessage());
        }
    }

    private void createUsersTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS users (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                username TEXT UNIQUE NOT NULL,
                password_hash TEXT NOT NULL,
                role TEXT NOT NULL CHECK (role IN ('admin', 'officer')),
                police_id TEXT,
                email TEXT,
                phone TEXT,
                department TEXT,
                rank TEXT,
                profile_photo_url TEXT,
                is_active BOOLEAN DEFAULT 1,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                last_login DATETIME
            )
            """;
        executeUpdate(sql);
    }

    private void createChatRoomsTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS chat_rooms (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                description TEXT,
                status TEXT DEFAULT 'active' CHECK (status IN ('active', 'completed')),
                created_by TEXT NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                completed_at DATETIME,
                FOREIGN KEY (created_by) REFERENCES users(id)
            )
            """;
        executeUpdate(sql);
    }

    private void createRoomParticipantsTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS room_participants (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                room_id TEXT NOT NULL,
                user_id TEXT NOT NULL,
                joined_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                is_active BOOLEAN DEFAULT 1,
                FOREIGN KEY (room_id) REFERENCES chat_rooms(id) ON DELETE CASCADE,
                FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
                UNIQUE(room_id, user_id)
            )
            """;
        executeUpdate(sql);
    }

    private void createMessagesTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS messages (
                id TEXT PRIMARY KEY,
                room_id TEXT NOT NULL,
                sender_id TEXT NOT NULL,
                message_type TEXT DEFAULT 'text' CHECK (message_type IN ('text', 'file', 'audio', 'video', 'system')),
                content TEXT,
                file_name TEXT,
                file_path TEXT,
                file_size INTEGER,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                expires_at DATETIME,
                is_deleted BOOLEAN DEFAULT 0,
                FOREIGN KEY (room_id) REFERENCES chat_rooms(id) ON DELETE CASCADE,
                FOREIGN KEY (sender_id) REFERENCES users(id)
            )
            """;
        executeUpdate(sql);
    }

    private void createFilesTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS files (
                id TEXT PRIMARY KEY,
                file_name TEXT NOT NULL,
                file_path TEXT NOT NULL,
                file_size INTEGER NOT NULL,
                file_type TEXT NOT NULL,
                uploaded_by TEXT NOT NULL,
                room_id TEXT,
                description TEXT,
                uploaded_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                is_deleted BOOLEAN DEFAULT 0,
                FOREIGN KEY (uploaded_by) REFERENCES users(id),
                FOREIGN KEY (room_id) REFERENCES chat_rooms(id) ON DELETE SET NULL
            )
            """;
        executeUpdate(sql);
    }

    private void createCriminalRecordsTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS criminal_records (
                id TEXT PRIMARY KEY,
                name TEXT NOT NULL,
                age INTEGER NOT NULL,
                gender TEXT CHECK (gender IN ('Male', 'Female')),
                hometown TEXT,
                national_id TEXT NOT NULL,
                birth_date DATE,
                photo_url TEXT,
                created_by TEXT NOT NULL,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                updated_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (created_by) REFERENCES users(id)
            )
            """;
        executeUpdate(sql);

        // Create crimes table for multiple crimes per record
        String crimesSql = """
            CREATE TABLE IF NOT EXISTS crimes (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                record_id TEXT NOT NULL,
                crime_type TEXT NOT NULL,
                crime_date DATE,
                location TEXT,
                status TEXT DEFAULT 'active' CHECK (status IN ('active', 'resolved', 'under_investigation')),
                description TEXT,
                created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (record_id) REFERENCES criminal_records(id) ON DELETE CASCADE
            )
            """;
        executeUpdate(crimesSql);
    }

    private void createOfficerLocationsTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS officer_locations (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id TEXT NOT NULL,
                latitude REAL NOT NULL,
                longitude REAL NOT NULL,
                accuracy REAL,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                room_id TEXT,
                FOREIGN KEY (user_id) REFERENCES users(id),
                FOREIGN KEY (room_id) REFERENCES chat_rooms(id) ON DELETE SET NULL
            )
            """;
        executeUpdate(sql);
    }

    private void createAuditLogTable() throws SQLException {
        String sql = """
            CREATE TABLE IF NOT EXISTS audit_log (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                user_id TEXT,
                action TEXT NOT NULL,
                entity_type TEXT,
                entity_id TEXT,
                details TEXT,
                ip_address TEXT,
                timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
                FOREIGN KEY (user_id) REFERENCES users(id)
            )
            """;
        executeUpdate(sql);
    }

    private void executeUpdate(String sql) throws SQLException {
        try (Statement stmt = connection.createStatement()) {
            stmt.execute(sql);
        }
    }

    // User Management Methods
    public boolean authenticateUser(String username, String password) throws SQLException {
        String sql = "SELECT COUNT(*) FROM users WHERE username = ? AND password_hash = ? AND is_active = 1";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            pstmt.setString(2, hashPassword(password));
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        }
    }

    public User getUserByUsername(String username) throws SQLException {
        String sql = """
            SELECT id, name, username, role, police_id, profile_photo_url
            FROM users WHERE username = ? AND is_active = 1
            """;
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId(rs.getString("id"));
                user.setName(rs.getString("name"));
                user.setUsername(rs.getString("username"));
                user.setRole(rs.getString("role"));
                user.setPoliceId(rs.getString("police_id"));
                user.setProfilePhoto(rs.getString("profile_photo_url"));
                return user;
            }
        }
        return null;
    }

    private String hashPassword(String password) {
        // Simple hash for demo - in production use BCrypt or similar
        return Integer.toString(password.hashCode());
    }

    public void updateLastLogin(String userId) throws SQLException {
        String sql = "UPDATE users SET last_login = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.executeUpdate();
        }
    }

    // Chat Room Methods
    public String createChatRoom(String name, String description, String createdBy) throws SQLException {
        String roomId = "room_" + System.currentTimeMillis();
        String sql = "INSERT INTO chat_rooms (id, name, description, created_by) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = connection.prepareStatement(sql)) {
            pstmt.setString(1, roomId);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setString(4, createdBy);
            pstmt.executeUpdate();
        }
        return roomId;
    }

    public void close() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            System.err.println("Error closing database connection: " + e.getMessage());
        }
    }
}