package com.bdpolice.station.controller;

import com.bdpolice.station.model.*;
import com.bdpolice.station.repo.*;
import com.bdpolice.station.service.Services;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.util.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class Controllers {
    private final Services services;
    private final ChatRoomRepo roomRepo;
    private final UserRepo userRepo;
    private final MessageRepo messageRepo;
    private final FileRecordRepo fileRepo;

    /* Auth (simplified) */
    @PostMapping("/public/login/admin")
    public Map<String, Object> adminLogin(@RequestParam String username, @RequestParam String password) {
        // In real app, validate via Spring Security or JWT. Here allow through for demo.
        Map<String, Object> res = new HashMap<>();
        res.put("role", "ADMIN");
        res.put("username", username);
        return res;
    }

    @PostMapping("/public/login/officer")
    public Map<String, Object> officerLogin(@RequestParam String policeId, @RequestParam String password) {
        Map<String, Object> res = new HashMap<>();
        res.put("role", "OFFICER");
        res.put("policeId", policeId);
        return res;
    }

    /* Rooms */
    @GetMapping("/rooms")
    public List<ChatRoom> listRooms(@RequestParam(required = false) RoomStatus status) {
        return status == null ? roomRepo.findAll() : roomRepo.findByStatus(status);
    }

    @PostMapping("/rooms")
    public ChatRoom createRoom(@RequestParam String name, @RequestBody List<Long> officerIds) {
        return services.createRoom(name, officerIds);
    }

    @PostMapping("/rooms/{roomId}/assign")
    public void assign(@PathVariable Long roomId, @RequestBody List<Long> officerIds) { services.assignOfficers(roomId, officerIds); }

    @DeleteMapping("/rooms/{roomId}")
    public void delete(@PathVariable Long roomId) { services.deleteRoom(roomId); }

    @PostMapping("/rooms/{roomId}/complete")
    public void complete(@PathVariable Long roomId) { services.markCompleted(roomId); }

    /* Messages */
    @GetMapping("/rooms/{roomId}/messages")
    public List<Message> messages(@PathVariable Long roomId) { return messageRepo.findByRoomIdOrderBySentAtAsc(roomId); }

    @PostMapping("/rooms/{roomId}/messages")
    public Message send(@PathVariable Long roomId, @RequestParam Long senderId, @RequestParam String content) {
        return services.postText(roomId, senderId, content);
    }

    /* Files */
    @GetMapping("/rooms/{roomId}/files")
    public List<FileRecord> files(@PathVariable Long roomId) { return fileRepo.findByRoomId(roomId); }

    @PostMapping(value = "/rooms/{roomId}/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Transactional
    public FileRecord upload(@PathVariable Long roomId,
                             @RequestParam Long uploaderId,
                             @RequestParam("file") MultipartFile file,
                             @RequestParam(required = false) String description,
                             @RequestParam(defaultValue = "document") String type) throws IOException {
        return services.uploadFile(roomId, uploaderId, file, description, type);
    }

    @GetMapping("/files/{roomId}/{path}")
    public ResponseEntity<byte[]> download(@PathVariable Long roomId, @PathVariable String path) throws IOException {
        Path file = Path.of("uploads", String.valueOf(roomId), path);
        byte[] bytes = Files.readAllBytes(file);
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + path)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(bytes);
    }

    /* Records */
    @GetMapping("/records/search")
    public List<CriminalRecord> search(@RequestParam(required = false) String name,
                                       @RequestParam(required = false) String nationalId,
                                       @RequestParam(required = false) String hometown,
                                       @RequestParam(required = false) String gender) {
        return services.searchRecords(name, nationalId, hometown, gender);
    }
}
