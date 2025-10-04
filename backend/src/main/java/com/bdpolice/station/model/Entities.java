package com.bdpolice.station.model;

import jakarta.persistence.*;
import lombok.*;
import java.time.Instant;
import java.util.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "users")
public class User {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true, nullable = false)
    private String username; // for admin or officer login name
    @Column(unique = true)
    private String policeId; // for officer
    private String passwordHash;
    @Enumerated(EnumType.STRING)
    private Role role; // ADMIN or OFFICER
    private String name;
    private String photoUrl;
    private boolean active = true;
}

enum Role { ADMIN, OFFICER }

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "chat_rooms")
public class ChatRoom {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Enumerated(EnumType.STRING)
    private RoomStatus status = RoomStatus.ACTIVE; // ACTIVE/COMPLETED
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "room_users",
            joinColumns = @JoinColumn(name = "room_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private Set<User> officers = new HashSet<>();
    private Instant createdAt = Instant.now();
}

enum RoomStatus { ACTIVE, COMPLETED }

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "messages")
public class Message {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private ChatRoom room;
    @ManyToOne(optional = false)
    private User sender;
    private Instant sentAt = Instant.now();
    @Enumerated(EnumType.STRING)
    private MessageType type = MessageType.TEXT;
    @Column(length = 4000)
    private String content; // text or file path / metadata
    private String fileName;
    private String fileType;
}

enum MessageType { TEXT, FILE, AUDIO, SYSTEM }

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "files")
public class FileRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private ChatRoom room;
    @ManyToOne(optional = false)
    private User uploadedBy;
    private String fileName;
    private String fileType; // image/video/document/audio
    private String relativePath; // on disk under uploads
    private long sizeBytes;
    private Instant uploadedAt = Instant.now();
    @Column(length = 1000)
    private String description;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "criminal_records")
public class CriminalRecord {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Integer age;
    private String gender;
    private String hometown;
    @Column(unique = true)
    private String nationalId;
    private Instant birthDate; // store as Instant (UTC); client formats
    private String photoUrl;
    @OneToMany(mappedBy = "record", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Crime> crimes = new ArrayList<>();
    @Column(length = 2000)
    private String description;
}

@Getter @Setter @NoArgsConstructor @AllArgsConstructor
@Entity
@Table(name = "crimes")
public class Crime {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(optional = false)
    private CriminalRecord record;
    private String type;
    private Instant date;
    private String location;
    private String status; // Active / Resolved / Under Investigation
}
