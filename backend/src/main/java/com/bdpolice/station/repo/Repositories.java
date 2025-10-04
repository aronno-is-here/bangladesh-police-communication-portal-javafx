package com.bdpolice.station.repo;

import com.bdpolice.station.model.*;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

public interface UserRepo extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
    Optional<User> findByPoliceId(String policeId);
}

public interface ChatRoomRepo extends JpaRepository<ChatRoom, Long> {
    Optional<ChatRoom> findByName(String name);
    List<ChatRoom> findByStatus(RoomStatus status);
}

public interface MessageRepo extends JpaRepository<Message, Long> {
    List<Message> findByRoomIdOrderBySentAtAsc(Long roomId);

    @Modifying
    @Query("delete from Message m where m.sentAt < :cutoff")
    int deleteOlderThan(@Param("cutoff") Instant cutoff);
}

public interface FileRecordRepo extends JpaRepository<FileRecord, Long> {
    List<FileRecord> findByRoomId(Long roomId);
}

public interface CriminalRecordRepo extends JpaRepository<CriminalRecord, Long> {
    @Query("select cr from CriminalRecord cr where (:name is null or lower(cr.name) like lower(concat('%',:name,'%'))) " +
            "and (:nid is null or cr.nationalId = :nid) " +
            "and (:hometown is null or lower(cr.hometown) like lower(concat('%',:hometown,'%'))) " +
            "and (:gender is null or cr.gender = :gender)")
    List<CriminalRecord> search(@Param("name") String name,
                                @Param("nid") String nationalId,
                                @Param("hometown") String hometown,
                                @Param("gender") String gender);
}
