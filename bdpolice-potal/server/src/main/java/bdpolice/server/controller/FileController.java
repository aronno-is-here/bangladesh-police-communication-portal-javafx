package bdpolice.server.controller;

import bdpolice.server.entity.ChatRoom;
import bdpolice.server.entity.FileRecord;
import bdpolice.server.entity.User;
import bdpolice.server.repo.ChatRoomRepo;
import bdpolice.server.repo.FileRecordRepo;
import bdpolice.server.repo.UserRepo;
import bdpolice.shared.dto.Models;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Instant;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileController {
  private final FileRecordRepo filesRepo;
  private final ChatRoomRepo rooms;
  private final UserRepo users;

  @Value("${app.upload-dir}")
  private String uploadDir;

  public FileController(FileRecordRepo filesRepo, ChatRoomRepo rooms, UserRepo users) {
    this.filesRepo = filesRepo; this.rooms = rooms; this.users = users;
  }

  @GetMapping
  public List<Models.FileRecordDTO> list(@RequestParam(required = false) String roomId) {
    return filesRepo.findByRoomIdNullable(roomId).stream().map(f -> new Models.FileRecordDTO(
        f.getId(), f.getRoom().getId(), f.getRoom().getName(), f.getUploadedBy().getName(), f.getUploadDate(),
        f.getFileName(), f.getFileType(), f.getSizeBytes(), f.getDescription(), "/api/files/" + f.getId() + "/download"
    )).toList();
  }

  @PostMapping(path = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<?> upload(@RequestParam("roomId") String roomId,
                                  @RequestParam("uploadedBy") String uploadedBy,
                                  @RequestParam("file") MultipartFile file,
                                  @RequestParam(value = "description", required = false) String description) throws IOException {
    ChatRoom room = rooms.findById(roomId).orElse(null);
    User user = users.findById(uploadedBy).orElse(null);
    if (room == null || user == null) return ResponseEntity.badRequest().build();

    Path dir = Paths.get(uploadDir, roomId);
    Files.createDirectories(dir);
    String filename = Instant.now().toEpochMilli() + "_" + StringUtils.cleanPath(file.getOriginalFilename());
    Path dest = dir.resolve(filename);
    Files.copy(file.getInputStream(), dest);

    FileRecord rec = new FileRecord();
    rec.setRoom(room);
    rec.setUploadedBy(user);
    rec.setFileName(filename);
    rec.setFileType(file.getContentType());
    rec.setSizeBytes(file.getSize());
    rec.setDescription(description);
    rec.setPath(dest.toString());
    filesRepo.save(rec);

    return ResponseEntity.ok().build();
  }

  @GetMapping("/{id}/download")
  public ResponseEntity<byte[]> download(@PathVariable String id) throws IOException {
    FileRecord rec = filesRepo.findById(id).orElse(null);
    if (rec == null) return ResponseEntity.notFound().build();
    byte[] bytes = Files.readAllBytes(Paths.get(rec.getPath()));
    return ResponseEntity.ok()
        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + rec.getFileName())
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(bytes);
  }
}
