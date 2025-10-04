package bdpolice.shared.dto;

import java.time.Instant;
import java.util.List;

public class Models {
  public enum Role { ADMIN, OFFICER }
  public enum RoomStatus { ACTIVE, COMPLETED }
  public enum MessageType { TEXT, FILE, AUDIO, SYSTEM }

  public record LoginRequest(String username, String password) {}
  public record OfficerLoginRequest(String policeId, String password) {}
  public record LoginResponse(String token, String userId, String name, Role role, String policeId) {}

  public record ChatRoomCreateRequest(String name, List<String> officerIds) {}
  public record ChatRoomResponse(String id, String name, RoomStatus status, Instant createdAt, Instant lastActivity) {}
  public record OfficerSummary(String id, String name, String policeId, String photoUrl, String status) {}

  public record MessageSendRequest(String roomId, MessageType type, String content, String fileName, String fileType) {}
  public record MessageDTO(String id, String roomId, String senderId, String senderName, MessageType type, String content, Instant timestamp, String fileName, String fileType) {}

  public record CriminalRecordFilter(String name, Integer age, String gender, String hometown, String nationalId, String birthDate) {}

  public record FileRecordDTO(String id, String roomId, String roomName, String uploadedBy, Instant uploadDate, String fileName, String fileType, long sizeBytes, String description, String downloadUrl) {}
}
