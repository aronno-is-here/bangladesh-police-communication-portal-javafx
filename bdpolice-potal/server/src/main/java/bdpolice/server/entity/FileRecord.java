package bdpolice.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "file_records")
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
