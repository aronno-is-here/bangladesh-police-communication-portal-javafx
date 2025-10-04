package bdpolice.server.controller;

import bdpolice.server.entity.User;
import bdpolice.server.repo.UserRepo;
import bdpolice.shared.dto.Models;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/auth")
public class AuthController {
  private final UserRepo users;
  public AuthController(UserRepo users) { this.users = users; }

  @PostMapping("/admin/login")
  public ResponseEntity<?> adminLogin(@RequestBody Models.LoginRequest req) {
    return users.findByUsername(req.username())
        .map(u -> ResponseEntity.ok(new Models.LoginResponse("dev-token", u.getId(), u.getName(), u.getRole(), u.getPoliceId())))
        .orElse(ResponseEntity.status(401).body(Map.of("error", "invalid credentials")));
  }

  @PostMapping("/officer/login")
  public ResponseEntity<?> officerLogin(@RequestBody Models.OfficerLoginRequest req) {
    return users.findByPoliceId(req.policeId())
        .map(u -> ResponseEntity.ok(new Models.LoginResponse("dev-token", u.getId(), u.getName(), u.getRole(), u.getPoliceId())))
        .orElse(ResponseEntity.status(401).body(Map.of("error", "invalid credentials")));
  }
}
