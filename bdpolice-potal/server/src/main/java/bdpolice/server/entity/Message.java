package bdpolice.server.entity;

import bdpolice.shared.dto.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "messages")
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
