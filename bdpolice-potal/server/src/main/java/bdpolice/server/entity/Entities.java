package bdpolice.server.entity;

import bdpolice.shared.dto.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity @Table(name = "users")
@Getter @Setter
public class User {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String name;
  @Enumerated(EnumType.STRING)
  private Models.Role role;
  @Column(unique = true)
  private String username; // for admin
  @Column(unique = true)
  private String policeId; // for officer
  private String passwordHash;
  private String photoUrl;
  private String status; // online/offline/busy
}

@Entity @Table(name = "chat_rooms")
@Getter @Setter
public class ChatRoom {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String name;
  @Enumerated(EnumType.STRING)
  private Models.RoomStatus status = Models.RoomStatus.ACTIVE;
  private Instant createdAt = Instant.now();
  private Instant lastActivity = Instant.now();

  @ManyToMany
  @JoinTable(name = "room_members",
    joinColumns = @JoinColumn(name = "room_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id"))
  private Set<User> members = new HashSet<>();
}

@Entity @Table(name = "messages")
@Getter @Setter
public class Message {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  @ManyToOne(fetch = FetchType.LAZY)
  private ChatRoom room;
  @ManyToOne(fetch = FetchType.LAZY)
  private User sender;
  @Enumerated(EnumType.STRING)
  private Models.MessageType type;
  @Column(length = 4000)
  private String content;
  private String fileName;
  private String fileType;
  private Instant timestamp = Instant.now();
}

@Entity @Table(name = "file_records")
@Getter @Setter
public class FileRecord {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  @ManyToOne(fetch = FetchType.LAZY)
  private ChatRoom room;
  @ManyToOne(fetch = FetchType.LAZY)
  private User uploadedBy;
  private Instant uploadDate = Instant.now();
  private String fileName;
  private String fileType;
  private long sizeBytes;
  @Column(length = 2000)
  private String description;
  private String path;
}

@Entity @Table(name = "criminal_records")
@Getter @Setter
public class CriminalRecord {
  @Id @GeneratedValue(strategy = GenerationType.UUID)
  private String id;
  private String name;
  private Integer age;
  private String gender;
  private String hometown;
  @Column(unique = true)
  private String nationalId;
  private Instant birthDate; // store as Instant start-of-day UTC
  private String photoUrl;
  @Column(length = 4000)
  private String description;
  private String status; // Active/Resolved/Under Investigation
}
