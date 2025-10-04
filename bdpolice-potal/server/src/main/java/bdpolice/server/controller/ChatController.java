package bdpolice.server.controller;

import bdpolice.server.entity.ChatRoom;
import bdpolice.server.entity.Message;
import bdpolice.server.entity.User;
import bdpolice.server.repo.ChatRoomRepo;
import bdpolice.server.repo.MessageRepo;
import bdpolice.server.repo.UserRepo;
import bdpolice.shared.dto.Models;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/chat")
public class ChatController {
  private final MessageRepo messages;
  private final ChatRoomRepo rooms;
  private final UserRepo users;

  public ChatController(MessageRepo messages, ChatRoomRepo rooms, UserRepo users) {
    this.messages = messages; this.rooms = rooms; this.users = users;
  }

  @GetMapping("/{roomId}")
  public List<Models.MessageDTO> list(@PathVariable String roomId) {
    return messages.findByRoomIdOrderByTimestampAsc(roomId).stream()
        .map(m -> new Models.MessageDTO(m.getId(), m.getRoom().getId(), m.getSender().getId(), m.getSender().getName(), m.getType(), m.getContent(), m.getTimestamp(), m.getFileName(), m.getFileType()))
        .toList();
  }

  @PostMapping
  public ResponseEntity<?> send(@RequestBody Models.MessageSendRequest req) {
    ChatRoom room = rooms.findById(req.roomId()).orElse(null);
    if (room == null) return ResponseEntity.notFound().build();
    // demo: pick first member as sender if not provided; replace with auth
    User sender = room.getMembers().stream().findFirst().orElse(null);
    Message m = new Message();
    m.setRoom(room);
    m.setSender(sender);
    m.setType(req.type());
    m.setContent(req.content());
    m.setFileName(req.fileName());
    m.setFileType(req.fileType());
    m.setTimestamp(Instant.now());
    messages.save(m);
    room.setLastActivity(Instant.now());
    rooms.save(room);
    return ResponseEntity.ok().build();
  }
}
