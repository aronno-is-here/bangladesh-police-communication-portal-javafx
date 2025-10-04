package com.bangladeshpolice.model;

import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Chat Room model class representing a mission communication room
 */
public class ChatRoom {
    private StringProperty id = new SimpleStringProperty();
    private StringProperty name = new SimpleStringProperty();
    private StringProperty description = new SimpleStringProperty();
    private StringProperty status = new SimpleStringProperty("active");
    private StringProperty createdBy = new SimpleStringProperty();
    private ObjectProperty<LocalDateTime> createdAt = new SimpleObjectProperty<>();
    private ObjectProperty<LocalDateTime> completedAt = new SimpleObjectProperty<>();

    // Related collections
    private ObservableList<User> participants = FXCollections.observableArrayList();
    private ObservableList<Message> messages = FXCollections.observableArrayList();
    private ObservableList<PoliceFile> files = FXCollections.observableArrayList();

    // Constructors
    public ChatRoom() {}

    public ChatRoom(String id, String name, String description, String createdBy) {
        this.id.set(id);
        this.name.set(name);
        this.description.set(description);
        this.createdBy.set(createdBy);
        this.createdAt.set(LocalDateTime.now());
    }

    // Property getters for JavaFX
    public StringProperty idProperty() { return id; }
    public StringProperty nameProperty() { return name; }
    public StringProperty descriptionProperty() { return description; }
    public StringProperty statusProperty() { return status; }
    public StringProperty createdByProperty() { return createdBy; }
    public ObjectProperty<LocalDateTime> createdAtProperty() { return createdAt; }
    public ObjectProperty<LocalDateTime> completedAtProperty() { return completedAt; }

    // Getters and setters
    public String getId() { return id.get(); }
    public void setId(String id) { this.id.set(id); }

    public String getName() { return name.get(); }
    public void setName(String name) { this.name.set(name); }

    public String getDescription() { return description.get(); }
    public void setDescription(String description) { this.description.set(description); }

    public String getStatus() { return status.get(); }
    public void setStatus(String status) { this.status.set(status); }

    public String getCreatedBy() { return createdBy.get(); }
    public void setCreatedBy(String createdBy) { this.createdBy.set(createdBy); }

    public LocalDateTime getCreatedAt() { return createdAt.get(); }
    public void setCreatedAt(LocalDateTime createdAt) { this.createdAt.set(createdAt); }

    public LocalDateTime getCompletedAt() { return completedAt.get(); }
    public void setCompletedAt(LocalDateTime completedAt) { this.completedAt.set(completedAt); }

    // Collection getters
    public ObservableList<User> getParticipants() { return participants; }
    public void setParticipants(List<User> participants) {
        this.participants.clear();
        if (participants != null) {
            this.participants.addAll(participants);
        }
    }

    public ObservableList<Message> getMessages() { return messages; }
    public void setMessages(List<Message> messages) {
        this.messages.clear();
        if (messages != null) {
            this.messages.addAll(messages);
        }
    }

    public ObservableList<PoliceFile> getFiles() { return files; }
    public void setFiles(List<PoliceFile> files) {
        this.files.clear();
        if (files != null) {
            this.files.addAll(files);
        }
    }

    // Utility methods
    public boolean isActive() {
        return "active".equals(getStatus());
    }

    public boolean isCompleted() {
        return "completed".equals(getStatus());
    }

    public void addParticipant(User user) {
        if (!participants.contains(user)) {
            participants.add(user);
        }
    }

    public void removeParticipant(User user) {
        participants.remove(user);
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public void addFile(PoliceFile file) {
        files.add(file);
    }

    public int getParticipantCount() {
        return participants.size();
    }

    public int getMessageCount() {
        return messages.size();
    }

    public int getFileCount() {
        return files.size();
    }

    // Get the most recent message for display
    public Message getLastMessage() {
        if (messages.isEmpty()) {
            return null;
        }
        return messages.get(messages.size() - 1);
    }

    // Get the last activity time (most recent message or file upload)
    public LocalDateTime getLastActivity() {
        LocalDateTime lastMessage = messages.stream()
            .map(Message::getCreatedAt)
            .max(LocalDateTime::compareTo)
            .orElse(null);

        LocalDateTime lastFile = files.stream()
            .map(PoliceFile::getUploadedAt)
            .max(LocalDateTime::compareTo)
            .orElse(null);

        if (lastMessage != null && lastFile != null) {
            return lastMessage.isAfter(lastFile) ? lastMessage : lastFile;
        }
        return lastMessage != null ? lastMessage : lastFile;
    }

    @Override
    public String toString() {
        return String.format("ChatRoom{id='%s', name='%s', status='%s', participants=%d}",
                           getId(), getName(), getStatus(), getParticipantCount());
    }
}