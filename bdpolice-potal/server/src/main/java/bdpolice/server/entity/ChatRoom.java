package bdpolice.server.entity;

import bdpolice.shared.dto.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "chat_rooms")
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
