package bdpolice.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "criminal_records")
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
