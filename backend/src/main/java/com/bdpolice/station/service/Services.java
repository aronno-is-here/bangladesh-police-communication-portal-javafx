package com.bdpolice.station.service;

import com.bdpolice.station.model.*;
import com.bdpolice.station.repo.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.time.Instant;
import java.util.*;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class Services {
    private final UserRepo userRepo;
    private final ChatRoomRepo chatRoomRepo;
    private final MessageRepo messageRepo;
    private final FileRecordRepo fileRecordRepo;
    private final CriminalRecordRepo criminalRecordRepo;
    private final SimpMessagingTemplate messagingTemplate;

    @Value("${app.uploads-dir:uploads}")
    private String uploadsDir;

    @Value("${app.message-ttl-seconds:600}")
    private long messageTtlSeconds;

    /* Users */
    public Optional<User> findOfficerByPoliceId(String policeId) { return userRepo.findByPoliceId(policeId); }

    /* Rooms */
    public ChatRoom createRoom(String name, List<Long> officerIds) {
        ChatRoom room = new ChatRoom();
        room.setName(name);
        room.setStatus(RoomStatus.ACTIVE);
        Set<User> officers = new HashSet<>(userRepo.findAllById(officerIds));
        room.setOfficers(officers);
        return chatRoomRepo.save(room);
    }

    public void assignOfficers(Long roomId, List<Long> officerIds) {
        ChatRoom room = chatRoomRepo.findById(roomId).orElseThrow();
        room.getOfficers().addAll(userRepo.findAllById(officerIds));
        chatRoomRepo.save(room);
    }

    public void removeOfficer(Long roomId, Long officerId) {
        ChatRoom room = chatRoomRepo.findById(roomId).orElseThrow();
        room.getOfficers().removeIf(u -> Objects.equals(u.getId(), officerId));
        chatRoomRepo.save(room);
    }

    public void markCompleted(Long roomId) {
        ChatRoom room = chatRoomRepo.findById(roomId).orElseThrow();
        room.setStatus(RoomStatus.COMPLETED);
        chatRoomRepo.save(room);
    }

    public void deleteRoom(Long roomId) { chatRoomRepo.deleteById(roomId); }

    /* Messages */
    @Transactional
    public Message postText(Long roomId, Long senderId, String content) {
        ChatRoom room = chatRoomRepo.findById(roomId).orElseThrow();
        User sender = userRepo.findById(senderId).orElseThrow();
        Message m = new Message();
        m.setRoom(room);
        m.setSender(sender);
        m.setContent(content);
        m.setType(MessageType.TEXT);
        m = messageRepo.save(m);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, m.getId());
        return m;
    }

    @Transactional
    public FileRecord uploadFile(Long roomId, Long uploaderId, MultipartFile file, String description, String type) throws IOException {
        ChatRoom room = chatRoomRepo.findById(roomId).orElseThrow();
        User uploader = userRepo.findById(uploaderId).orElseThrow();
        Path base = Paths.get(uploadsDir, String.valueOf(roomId));
        Files.createDirectories(base);
        Path dest = base.resolve(UUID.randomUUID() + "_" + file.getOriginalFilename());
        Files.copy(file.getInputStream(), dest, StandardCopyOption.REPLACE_EXISTING);

        FileRecord fr = new FileRecord();
        fr.setRoom(room);
        fr.setUploadedBy(uploader);
        fr.setFileName(file.getOriginalFilename());
        fr.setFileType(type);
        fr.setRelativePath(base.relativize(dest).toString());
        fr.setSizeBytes(file.getSize());
        fr.setDescription(description);
        fr = fileRecordRepo.save(fr);

        Message m = new Message();
        m.setRoom(room);
        m.setSender(uploader);
        m.setType(MessageType.FILE);
        m.setContent(fr.getRelativePath());
        m.setFileName(fr.getFileName());
        m.setFileType(fr.getFileType());
        messageRepo.save(m);
        messagingTemplate.convertAndSend("/topic/rooms/" + roomId, "file:" + fr.getId());
        return fr;
    }

    /* Auto-delete old messages (except records) */
    @Scheduled(fixedDelay = 60_000)
    @Transactional
    public void cleanupOldMessages() {
        Instant cutoff = Instant.now().minusSeconds(messageTtlSeconds);
        messageRepo.deleteOlderThan(cutoff);
    }

    /* Records */
    public List<CriminalRecord> searchRecords(String name, String nid, String hometown, String gender) {
        return criminalRecordRepo.search(blankToNull(name), blankToNull(nid), blankToNull(hometown), blankToNull(gender));
    }

    private String blankToNull(String s) { return (s == null || s.isBlank()) ? null : s; }
}
