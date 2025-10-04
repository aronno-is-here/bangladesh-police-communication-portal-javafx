package bdpolice.server.ws;

import bdpolice.server.entity.ChatRoom;
import bdpolice.server.entity.Message;
import bdpolice.server.entity.User;
import bdpolice.server.repo.ChatRoomRepo;
import bdpolice.server.repo.MessageRepo;
import bdpolice.server.repo.UserRepo;
import bdpolice.shared.dto.Models;
import org.springframework.messaging.handler.annotation.DestinationVariable;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.time.Instant;

@Controller
public class ChatWSController {
  private final SimpMessagingTemplate template;
  private final MessageRepo messages;
  private final ChatRoomRepo rooms;
  private final UserRepo users;

  public ChatWSController(SimpMessagingTemplate template, MessageRepo messages, ChatRoomRepo rooms, UserRepo users) {
    this.template = template; this.messages = messages; this.rooms = rooms; this.users = users;
  }

  @MessageMapping("chat.send.{roomId}")
  public void send(@DestinationVariable String roomId, @Payload Models.MessageSendRequest req) {
    ChatRoom room = rooms.findById(roomId).orElse(null);
    if (room == null) return;
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

    Models.MessageDTO dto = new Models.MessageDTO(m.getId(), room.getId(), sender != null ? sender.getId() : null,
        sender != null ? sender.getName() : "System", m.getType(), m.getContent(), m.getTimestamp(), m.getFileName(), m.getFileType());
    template.convertAndSend("/topic/rooms/" + roomId, dto);
  }
}
