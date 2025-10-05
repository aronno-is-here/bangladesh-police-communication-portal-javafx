package bdpolice.server.repo;

import bdpolice.server.entity.*;
import bdpolice.shared.dto.Models;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, String> {
  Optional<User> findByUsername(String username);
  Optional<User> findByPoliceId(String policeId);
}

public interface ChatRoomRepo extends JpaRepository<ChatRoom, String> {
  @Query("select r from ChatRoom r join r.members m where m.id = :userId")
  List<ChatRoom> findByMemberId(String userId);
}

public interface MessageRepo extends JpaRepository<Message, String> {
  List<Message> findByRoomIdOrderByTimestampAsc(String roomId);
  long deleteByTimestampBefore(Instant cutoff);
}

public interface FileRecordRepo extends JpaRepository<FileRecord, String> {
  @Query("select f from FileRecord f where (:roomId is null or f.room.id = :roomId)")
  List<FileRecord> findByRoomIdNullable(String roomId);
}

public interface CriminalRecordRepo extends JpaRepository<CriminalRecord, String> {
  @Query("select c from CriminalRecord c where " +
      "(:name is null or lower(c.name) like lower(concat('%', :name, '%'))) and " +
      "(:gender is null or c.gender = :gender) and " +
      "(:hometown is null or lower(c.hometown) like lower(concat('%', :hometown, '%'))) and " +
      "(:nationalId is null or c.nationalId = :nationalId)")
  List<CriminalRecord> search(String name, String gender, String hometown, String nationalId);
}
