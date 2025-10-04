package com.bangladeshpolice.model;

import javafx.beans.property.*;
import java.time.LocalDateTime;

/**
 * Message model class representing chat messages in the system
 */
public class Message {
    public enum MessageType {
        TEXT("text"),
        FILE("file"),
        AUDIO("audio"),
        VIDEO("video"),
        SYSTEM("system");

        private final String value;

        MessageType(String value) {
            this.value = value;
        }

        public String getValue() { return value; }

        @Override
        public String toString() { return value; }
    }

    private StringProperty id = new SimpleStringProperty();
    private StringProperty roomId = new SimpleStringProperty();
    private StringProperty senderId = new SimpleStringProperty();
    private ObjectProperty<MessageType> messageType = new SimpleObjectProperty<>(MessageType.TEXT);
    private StringProperty content = new SimpleStringProperty();
    private StringProperty fileName = new SimpleStringProperty();
    private StringProperty filePath = new SimpleStringProperty();
    private LongProperty fileSize = new SimpleLongProperty();
    private ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> expiresAt = new SimpleObjectProperty<>();
    private BooleanProperty isDeleted = new SimpleBooleanProperty(false);

    // Related objects (not stored in DB, loaded on demand)
    private User sender;
    private ChatRoom room;

    // Constructors
    public Message() {}

    public Message(String id, String roomId, String senderId, String content) {
        this.id.set(id);
        this.roomId.set(roomId);
        this.senderId.set(senderId);
        this.content.set(content);
        this.createdAt.set(LocalDateTime.now());
        this.expiresAt.set(LocalDateTime.now().plusMinutes(10)); // Auto-delete after 10 minutes
    }

    // Property getters for JavaFX
    public StringProperty idProperty() { return id; }
    public StringProperty roomIdProperty() { return roomId; }
    public StringProperty senderIdProperty() { return senderId; }
    public ObjectProperty<MessageType> messageTypeProperty() { return messageType; }
    public StringProperty contentProperty() { return content; }
    public StringProperty fileNameProperty() { return fileName; }
    public StringProperty filePathProperty() { return filePath; }
    public LongProperty fileSizeProperty() { return fileSize; }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public ObjectProperty<LocalDateTime> expiresAtProperty() { return expiresAt; }
    public BooleanProperty isDeletedProperty() { return isDeleted; }

    // Getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getRoomId() { return roomId.get(); }
    public void setRoomId(String roomId) { this.roomId.set(roomId); }

    public String getSenderId() { return senderId.get(); }
    public void setSenderId(String senderId) { this.senderId.set(senderId); }

    public MessageType getMessageType() { return messageType.get(); }
    public void setMessageType(MessageType messageType) { this.messageType.set(messageType); }

    public String getContent() { return content.get(); }
    public void setContent(String content) { this.content.set(content); }

    public String getFileName() { return fileName.get(); }
    public void setFileName(String fileName) { this.fileName.set(fileName); }

    public String getFilePath() { return filePath.get(); }
    public void setFilePath(String filePath) { this.filePath.set(filePath); }

    public Long getFileSize() { return fileSize.get(); }
    public void setFileSize(Long fileSize) { this.fileSize.set(fileSize); }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    public LocalDateTime getExpiresAt() { return expiresAt.get(); }
    public void setExpiresAt(LocalDateTime expiresAt) { this.expiresAt.set(expiresAt); }

    public boolean isDeleted() { return isDeleted.get(); }
    public void setDeleted(boolean deleted) { this.isDeleted.set(deleted); }

    // Related object getters/setters
    public User getSender() { return sender; }
    public void setSender(User sender) { this.sender = sender; }

    public ChatRoom getRoom() { return room; }
    public void setRoom(ChatRoom room) { this.room = room; }

    // Utility methods
    public boolean isTextMessage() {
        return MessageType.TEXT.equals(getMessageType());
    }

    public boolean isFileMessage() {
        return MessageType.FILE.equals(getMessageType());
    }

    public boolean isAudioMessage() {
        return MessageType.AUDIO.equals(getMessageType());
    }

    public boolean isVideoMessage() {
        return MessageType.VIDEO.equals(getMessageType());
    }

    public boolean isSystemMessage() {
        return MessageType.SYSTEM.equals(getMessageType());
    }

    public boolean isExpired() {
        return getExpiresAt() != null && LocalDateTime.now().isAfter(getExpiresAt());
    }

    public boolean shouldAutoDelete() {
        return isTextMessage() && isExpired();
    }

    public String getFormattedTime() {
        if (getCreatedAt() == null) return "";
        return getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }

    public String getFormattedDateTime() {
        if (getCreatedAt() == null) return "";
        return getCreatedAt().format(java.time.format.DateTimeFormatter.ofPattern("MMM dd, HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("Message{id='%s', type='%s', content='%s'}",
                           getId(), getMessageType(), getContent());
    }
}