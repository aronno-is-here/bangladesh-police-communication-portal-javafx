package bdpolice.server.controller;

import bdpolice.server.entity.ChatRoom;
import bdpolice.server.entity.User;
import bdpolice.server.repo.ChatRoomRepo;
import bdpolice.server.repo.UserRepo;
import bdpolice.shared.dto.Models;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/rooms")
public class RoomController {
  private final ChatRoomRepo rooms;
  private final UserRepo users;

  public RoomController(ChatRoomRepo rooms, UserRepo users) {
    this.rooms = rooms; this.users = users;
  }

  @PostMapping
  public ResponseEntity<?> create(@RequestBody Models.ChatRoomCreateRequest req) {
    ChatRoom r = new ChatRoom();
    r.setName(req.name());
    Set<User> members = req.officerIds() != null ? req.officerIds().stream()
        .map(id -> users.findById(id).orElse(null))
        .filter(u -> u != null)
        .collect(Collectors.toSet()) : Set.of();
    r.setMembers(members);
    rooms.save(r);
    return ResponseEntity.ok(new Models.ChatRoomResponse(r.getId(), r.getName(), r.getStatus(), r.getCreatedAt(), r.getLastActivity()));
  }

  @GetMapping
  public List<Models.ChatRoomResponse> list() {
    return rooms.findAll().stream()
        .map(r -> new Models.ChatRoomResponse(r.getId(), r.getName(), r.getStatus(), r.getCreatedAt(), r.getLastActivity()))
        .toList();
  }

  @PostMapping("/{id}/complete")
  public ResponseEntity<?> markCompleted(@PathVariable String id) {
    return rooms.findById(id).map(r -> {
      r.setStatus(Models.RoomStatus.COMPLETED);
      r.setLastActivity(Instant.now());
      rooms.save(r);
      return ResponseEntity.ok().build();
    }).orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<?> delete(@PathVariable String id) {
    if (rooms.existsById(id)) { rooms.deleteById(id); return ResponseEntity.ok().build(); }
    return ResponseEntity.notFound().build();
  }
}
