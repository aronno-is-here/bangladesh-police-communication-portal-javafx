package com.bangladeshpolice;

import com.bangladeshpolice.model.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalDateTime;

/**
 * Sample Data Loader for populating the database with demo data
 */
public class SampleDataLoader {

    public static void loadSampleData() {
        try {
            createSampleUsers();
            createSampleChatRooms();
            createSampleMessages();
            createSampleCriminalRecords();
            createSampleFiles();
            createSampleOfficerLocations();

            System.out.println("Sample data loaded successfully");

        } catch (SQLException e) {
            System.err.println("Error loading sample data: " + e.getMessage());
        }
    }

    private static void createSampleUsers() throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();

        // Admin user
        if (!userExists("admin")) {
            insertUser("admin-1", "System Administrator", "admin", "admin123", "admin", null, null, null, null, null, null);
        }

        // Officer users
        if (!userExists("officer-1")) {
            insertUser("officer-1", "Officer Rahman", "rahman", "officer123", "officer", "BD001",
                      "rahman@bdpolice.gov.bd", "+880-1234567890", "Criminal Investigation", "Inspector", null);
        }

        if (!userExists("officer-2")) {
            insertUser("officer-2", "Officer Khan", "khan", "officer123", "officer", "BD002",
                      "khan@bdpolice.gov.bd", "+880-1234567891", "Traffic Control", "Sub-Inspector", null);
        }

        if (!userExists("officer-3")) {
            insertUser("officer-3", "Officer Ali", "ali", "officer123", "officer", "BD003",
                      "ali@bdpolice.gov.bd", "+880-1234567892", "Patrol Unit", "Constable", null);
        }
    }

    private static boolean userExists(String username) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = "SELECT COUNT(*) FROM users WHERE username = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        }
    }

    private static void insertUser(String id, String name, String username, String password,
                                 String role, String policeId, String email, String phone,
                                 String department, String rank, String profilePhoto) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = """
            INSERT INTO users (id, name, username, password_hash, role, police_id, email, phone, department, rank, profile_photo_url)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, username);
            pstmt.setString(4, hashPassword(password));
            pstmt.setString(5, role);
            pstmt.setString(6, policeId);
            pstmt.setString(7, email);
            pstmt.setString(8, phone);
            pstmt.setString(9, department);
            pstmt.setString(10, rank);
            pstmt.setString(11, profilePhoto);
            pstmt.executeUpdate();
        }
    }

    private static void createSampleChatRooms() throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();

        // Operation Sunrise
        if (!roomExists("room-1")) {
            insertChatRoom("room-1", "Operation Sunrise", "Drug bust operation in Gulshan area", "admin-1");
            addRoomParticipant("room-1", "officer-1");
            addRoomParticipant("room-1", "officer-2");
            addRoomParticipant("room-1", "officer-3");
        }

        // Patrol Route 7
        if (!roomExists("room-2")) {
            insertChatRoom("room-2", "Patrol Route 7", "Regular patrol monitoring", "admin-1");
            addRoomParticipant("room-2", "officer-2");
            addRoomParticipant("room-2", "officer-3");
        }

        // Traffic Control
        if (!roomExists("room-3")) {
            insertChatRoom("room-3", "Traffic Control", "Traffic management during rush hour", "admin-1");
            addRoomParticipant("room-3", "officer-1");
            addRoomParticipant("room-3", "officer-2");
            markRoomCompleted("room-3");
        }
    }

    private static boolean roomExists(String roomId) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = "SELECT COUNT(*) FROM chat_rooms WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, roomId);
            ResultSet rs = pstmt.executeQuery();
            return rs.getInt(1) > 0;
        }
    }

    private static void insertChatRoom(String id, String name, String description, String createdBy) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = "INSERT INTO chat_rooms (id, name, description, created_by) VALUES (?, ?, ?, ?)";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, description);
            pstmt.setString(4, createdBy);
            pstmt.executeUpdate();
        }
    }

    private static void addRoomParticipant(String roomId, String userId) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = "INSERT INTO room_participants (room_id, user_id) VALUES (?, ?)";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, roomId);
            pstmt.setString(2, userId);
            pstmt.executeUpdate();
        }
    }

    private static void markRoomCompleted(String roomId) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = "UPDATE chat_rooms SET status = 'completed', completed_at = CURRENT_TIMESTAMP WHERE id = ?";
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, roomId);
            pstmt.executeUpdate();
        }
    }

    private static void createSampleMessages() throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();

        // Sample messages for Operation Sunrise
        insertMessage("msg-1", "room-1", "admin-1", Message.MessageType.SYSTEM,
                     "Room created. Mission briefing will start in 5 minutes.");
        insertMessage("msg-2", "room-1", "officer-2", Message.MessageType.TEXT,
                     "Roger that. Awaiting instructions.");
        insertMessage("msg-3", "room-1", "admin-1", Message.MessageType.FILE,
                     "Evidence photos uploaded.", "evidence_001.jpg", "files/evidence_001.jpg", 2457600L);
        insertMessage("msg-4", "room-1", "officer-1", Message.MessageType.TEXT,
                     "Photos received. Proceeding to location.");
        insertMessage("msg-5", "room-1", "officer-3", Message.MessageType.AUDIO,
                     "Voice message recorded", null, null, 1250000L);
    }

    private static void insertMessage(String id, String roomId, String senderId, Message.MessageType type,
                                    String content, String fileName, String filePath, Long fileSize) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = """
            INSERT INTO messages (id, room_id, sender_id, message_type, content, file_name, file_path, file_size)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, roomId);
            pstmt.setString(3, senderId);
            pstmt.setString(4, type.getValue());
            pstmt.setString(5, content);
            pstmt.setString(6, fileName);
            pstmt.setString(7, filePath);
            pstmt.setLong(8, fileSize != null ? fileSize : 0);
            pstmt.executeUpdate();
        }
    }

    private static void createSampleCriminalRecords() throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();

        // Sample criminal records
        insertCriminalRecord("record-1", "Ahmed Hassan", 32, "Male", "Dhaka", "1234567890",
                           LocalDate.of(1992, 3, 15), null, "officer-1");
        insertCrime("record-1", "Theft", LocalDate.of(2024, 1, 15), "Gulshan, Dhaka", "under_investigation", "Theft case");

        insertCriminalRecord("record-2", "Fatima Khan", 28, "Female", "Chittagong", "0987654321",
                           LocalDate.of(1996, 7, 22), null, "officer-2");
        insertCrime("record-2", "Fraud", LocalDate.of(2024, 2, 10), "Chittagong", "active", "Financial fraud case");
    }

    private static void insertCriminalRecord(String id, String name, int age, String gender, String hometown,
                                           String nationalId, LocalDate birthDate, String photoUrl, String createdBy) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = """
            INSERT INTO criminal_records (id, name, age, gender, hometown, national_id, birth_date, photo_url, created_by)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setInt(3, age);
            pstmt.setString(4, gender);
            pstmt.setString(5, hometown);
            pstmt.setString(6, nationalId);
            pstmt.setDate(7, Date.valueOf(birthDate));
            pstmt.setString(8, photoUrl);
            pstmt.setString(9, createdBy);
            pstmt.executeUpdate();
        }
    }

    private static void insertCrime(String recordId, String crimeType, LocalDate crimeDate, String location,
                                  String status, String description) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = """
            INSERT INTO crimes (record_id, crime_type, crime_date, location, status, description)
            VALUES (?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, recordId);
            pstmt.setString(2, crimeType);
            pstmt.setDate(3, Date.valueOf(crimeDate));
            pstmt.setString(4, location);
            pstmt.setString(5, status);
            pstmt.setString(6, description);
            pstmt.executeUpdate();
        }
    }

    private static void createSampleFiles() throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();

        // Sample files
        insertFile("file-1", "evidence_001.jpg", "files/evidence_001.jpg", 2457600L, "image", "admin-1", "room-1", "Crime scene evidence photo");
        insertFile("file-2", "suspect_interview.mp4", "files/suspect_interview.mp4", 145200000L, "video", "officer-2", "room-1", "Suspect interrogation recording");
        insertFile("file-3", "incident_report.pdf", "files/incident_report.pdf", 819200L, "document", "admin-1", "room-2", "Initial incident report");
    }

    private static void insertFile(String id, String fileName, String filePath, long fileSize, String fileType,
                                 String uploadedBy, String roomId, String description) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = """
            INSERT INTO files (id, file_name, file_path, file_size, file_type, uploaded_by, room_id, description)
            VALUES (?, ?, ?, ?, ?, ?, ?, ?)
            """;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, fileName);
            pstmt.setString(3, filePath);
            pstmt.setLong(4, fileSize);
            pstmt.setString(5, fileType);
            pstmt.setString(6, uploadedBy);
            pstmt.setString(7, roomId);
            pstmt.setString(8, description);
            pstmt.executeUpdate();
        }
    }

    private static void createSampleOfficerLocations() throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();

        // Sample officer locations in Dhaka area
        insertOfficerLocation("officer-1", 23.8103, 90.4125, 5.0, "room-1"); // Gulshan area
        insertOfficerLocation("officer-2", 23.7808, 90.4209, 8.0, "room-1"); // Banani area
        insertOfficerLocation("officer-3", 23.7516, 90.3936, 12.0, "room-2"); // Dhanmondi area
    }

    private static void insertOfficerLocation(String userId, double latitude, double longitude,
                                            double accuracy, String roomId) throws SQLException {
        DatabaseManager db = DatabaseManager.getInstance();
        String sql = """
            INSERT INTO officer_locations (user_id, latitude, longitude, accuracy, room_id)
            VALUES (?, ?, ?, ?, ?)
            """;
        try (PreparedStatement pstmt = db.getConnection().prepareStatement(sql)) {
            pstmt.setString(1, userId);
            pstmt.setDouble(2, latitude);
            pstmt.setDouble(3, longitude);
            pstmt.setDouble(4, accuracy);
            pstmt.setString(5, roomId);
            pstmt.executeUpdate();
        }
    }

    private static String hashPassword(String password) {
        return Integer.toString(password.hashCode());
    }
}