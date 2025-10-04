package com.bangladeshpolice.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Police File model class representing uploaded files in the system
 */
public class PoliceFile {
    public enum FileType {
        IMAGE("image"),
        VIDEO("video"),
        DOCUMENT("document"),
        AUDIO("audio"),
        OTHER("other");

        private final String value;

        FileType(String value) {
            this.value = value;
        }

        public String getValue() { return value; }

        public static FileType fromString(String value) {
            for (FileType type : values()) {
                if (type.value.equals(value)) {
                    return type;
                }
            }
            return OTHER;
        }

        @Override
        public String toString() { return value; }
    }

    private StringProperty id = new SimpleStringProperty();
    private StringProperty fileName = new SimpleStringProperty();
    private StringProperty filePath = new SimpleStringProperty();
    private LongProperty fileSize = new SimpleLongProperty();
    private ObjectProperty<FileType> fileType = new SimpleObjectProperty<>(FileType.OTHER);
    private StringProperty uploadedBy = new SimpleStringProperty();
    private StringProperty roomId = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private ObjectProperty<LocalDateTime> uploadedAt = new SimpleObjectProperty<>();
    private BooleanProperty isDeleted = new SimpleBooleanProperty(false);

    // Related objects (loaded on demand)
    private User uploader;
    private ChatRoom room;

    // Constructors
    public PoliceFile() {}

    public PoliceFile(String fileName, String filePath, long fileSize, FileType fileType, String uploadedBy) {
        this.fileName.set(fileName);
        this.filePath.set(filePath);
        this.fileSize.set(fileSize);
        this.fileType.set(fileType);
        this.uploadedBy.set(uploadedBy);
        this.uploadedAt.set(LocalDateTime.now());
    }

    // Property getters for JavaFX
    public StringProperty idProperty() { return id; }
    public StringProperty fileNameProperty() { return fileName; }
    public StringProperty filePathProperty() { return filePath; }
    public LongProperty fileSizeProperty() { return fileSize; }
    public ObjectProperty<FileType> fileTypeProperty() { return fileType; }
    public StringProperty uploadedByProperty() { return uploadedBy; }
    public StringProperty roomIdProperty() { return roomId; }
    public StringProperty descriptionProperty() { return description; }
    public ObjectProperty<LocalDateTime> uploadedAtProperty() { return uploadedAt; }
    public BooleanProperty isDeletedProperty() { return isDeleted; }

    // Getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getFileName() { return fileName.get(); }
    public void setFileName(String fileName) { this.fileName.set(fileName); }

    public String getFilePath() { return filePath.get(); }
    public void setFilePath(String filePath) { this.filePath.set(filePath); }

    public Long getFileSize() { return fileSize.get(); }
    public void setFileSize(Long fileSize) { this.fileSize.set(fileSize); }

    public FileType getFileType() { return fileType.get(); }
    public void setFileType(FileType fileType) { this.fileType.set(fileType); }

    public String getUploadedBy() { return uploadedBy.get(); }
    public void setUploadedBy(String uploadedBy) { this.uploadedBy.set(uploadedBy); }

    public String getRoomId() { return roomId.get(); }
    public void setRoomId(String roomId) { this.roomId.set(roomId); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public LocalDateTime getUploadedAt() { return uploadedAt.get(); }
    public void setUploadedAt(LocalDateTime uploadedAt) { this.uploadedAt.set(uploadedAt); }

    public boolean isDeleted() { return isDeleted.get(); }
    public void setDeleted(boolean deleted) { this.isDeleted.set(deleted); }

    // Related object getters/setters
    public User getUploader() { return uploader; }
    public void setUploader(User uploader) { this.uploader = uploader; }

    public ChatRoom getRoom() { return room; }
    public void setRoom(ChatRoom room) { this.room = room; }

    // Utility methods
    public String getFormattedFileSize() {
        long size = getFileSize();
        if (size < 1024) return size + " B";
        if (size < 1024 * 1024) return String.format("%.1f KB", size / 1024.0);
        if (size < 1024 * 1024 * 1024) return String.format("%.1f MB", size / (1024.0 * 1024.0));
        return String.format("%.1f GB", size / (1024.0 * 1024.0 * 1024.0));
    }

    public String getFormattedUploadDate() {
        if (getUploadedAt() == null) return "";
        return getUploadedAt().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, yyyy HH:mm"));
    }

    public boolean isImage() {
        return FileType.IMAGE.equals(getFileType());
    }

    public boolean isVideo() {
        return FileType.VIDEO.equals(getFileType());
    }

    public boolean isDocument() {
        return FileType.DOCUMENT.equals(getFileType());
    }

    public boolean isAudio() {
        return FileType.AUDIO.equals(getFileType());
    }

    public String getFileExtension() {
        String fileName = getFileName();
        if (fileName == null || !fileName.contains(".")) return "";
        return fileName.substring(fileName.lastIndexOf(".") + 1).toLowerCase();
    }

    @Override
    public String toString() {
        return String.format("PoliceFile{id='%s', name='%s', type='%s', size=%s}",
                           getId(), getFileName(), getFileType(), getFormattedFileSize());
    }
}