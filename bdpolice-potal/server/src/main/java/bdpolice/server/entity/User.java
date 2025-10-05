package bdpolice.server.entity;

import bdpolice.shared.dto.Models;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "users")
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
