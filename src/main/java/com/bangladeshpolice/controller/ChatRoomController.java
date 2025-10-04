package com.bangladeshpolice.controller;

import com.bangladeshpolice.DatabaseManager;
import com.bangladeshpolice.PoliceManagementSystem;
import com.bangladeshpolice.model.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Controller for Chat Room functionality
 */
public class ChatRoomController implements Initializable {

    @FXML private Button backBtn;
    @FXML private Label roomNameLabel;
    @FXML private Label roomStatusLabel;
    @FXML private Label membersCountLabel;
    @FXML private Button callBtn;
    @FXML private Button mapBtn;
    @FXML private HBox callBannerHBox;
    @FXML private Label callDurationLabel;
    @FXML private Button endCallBtn;
    @FXML private ScrollPane messagesScrollPane;
    @FXML private VBox messagesVBox;
    @FXML private TextField messageInputField;
    @FXML private Button voiceBtn;
    @FXML private Button sendBtn;
    @FXML private Button imageUploadBtn;
    @FXML private Button videoUploadBtn;
    @FXML private Button fileUploadBtn;
    @FXML private HBox recordingIndicatorHBox;
    @FXML private Label recordingLabel;
    @FXML private TabPane sidebarTabPane;
    @FXML private VBox participantsVBox;
    @FXML private VBox filesVBox;

    private ChatRoom currentRoom;
    private User currentUser;
    private DatabaseManager dbManager;
    private ObservableList<Message> messages = FXCollections.observableArrayList();
    private ObservableList<User> participants = FXCollections.observableArrayList();
    private ObservableList<PoliceFile> files = FXCollections.observableArrayList();

    private boolean isRecording = false;
    private boolean isInCall = false;
    private Timer callTimer;
    private LocalDateTime callStartTime;
    private Timer autoDeleteTimer;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();

        // Set up event handlers
        setupEventHandlers();

        // Set up keyboard shortcuts
        setupKeyboardShortcuts();

        // Initialize auto-delete timer for messages
        setupAutoDeleteTimer();

        // Load sample data for demo
        loadSampleRoomData();
    }

    private void setupEventHandlers() {
        backBtn.setOnAction(e -> handleBack());
        callBtn.setOnAction(e -> handleCall());
        endCallBtn.setOnAction(e -> handleEndCall());
        mapBtn.setOnAction(e -> handleMap());
        sendBtn.setOnAction(e -> handleSendMessage());
        voiceBtn.setOnAction(e -> handleVoiceMessage());
        imageUploadBtn.setOnAction(e -> handleFileUpload("image"));
        videoUploadBtn.setOnAction(e -> handleFileUpload("video"));
        fileUploadBtn.setOnAction(e -> handleFileUpload("file"));
    }

    private void setupKeyboardShortcuts() {
        messageInputField.setOnKeyPressed(this::handleMessageKeyPress);
    }

    private void handleMessageKeyPress(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            handleSendMessage();
        }
    }

    private void setupAutoDeleteTimer() {
        autoDeleteTimer = new Timer(true);
        autoDeleteTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> checkForExpiredMessages());
            }
        }, 10000, 10000); // Check every 10 seconds
    }

    private void checkForExpiredMessages() {
        List<Message> expiredMessages = messages.stream()
            .filter(Message::shouldAutoDelete)
            .toList();

        if (!expiredMessages.isEmpty()) {
            Platform.runLater(() -> {
                messages.removeAll(expiredMessages);
                refreshMessagesDisplay();
            });
        }
    }

    private void loadSampleRoomData() {
        // Load sample room data for demonstration
        currentRoom = new ChatRoom("room-1", "Operation Sunrise", "Drug bust operation in Gulshan area", "admin-1");
        currentRoom.setStatus("active");

        // Add sample participants
        participants.add(new User("officer-1", "Officer Rahman", "rahman", "officer"));
        participants.add(new User("officer-2", "Officer Khan", "khan", "officer"));
        participants.add(new User("officer-3", "Officer Ali", "ali", "officer"));
        participants.add(new User("admin-1", "Admin", "admin", "admin"));

        // Add sample messages
        addSampleMessages();

        // Add sample files
        addSampleFiles();

        // Update UI
        updateRoomInfo();
        refreshParticipantsDisplay();
        refreshFilesDisplay();
        refreshMessagesDisplay();
    }

    private void addSampleMessages() {
        messages.add(new Message("msg-1", "room-1", "admin-1", "Room created. Mission briefing will start in 5 minutes."));
        messages.add(new Message("msg-2", "room-1", "officer-2", "Roger that. Awaiting instructions."));
        messages.add(new Message("msg-3", "room-1", "admin-1", "Evidence photos uploaded."));
        messages.add(new Message("msg-4", "room-1", "officer-1", "Photos received. Proceeding to location."));
    }

    private void addSampleFiles() {
        files.add(new PoliceFile("evidence_001.jpg", "files/evidence_001.jpg", 2457600L,
                                PoliceFile.FileType.IMAGE, "admin-1"));
        files.add(new PoliceFile("suspect_interview.mp4", "files/suspect_interview.mp4", 145200000L,
                                PoliceFile.FileType.VIDEO, "officer-2"));
        files.add(new PoliceFile("report.pdf", "files/report.pdf", 819200L,
                                PoliceFile.FileType.DOCUMENT, "officer-1"));
    }

    private void updateRoomInfo() {
        if (currentRoom != null) {
            roomNameLabel.setText(currentRoom.getName());
            roomStatusLabel.setText(currentRoom.getStatus());
            membersCountLabel.setText(currentRoom.getParticipantCount() + " members");
        }
    }

    private void refreshMessagesDisplay() {
        messagesVBox.getChildren().clear();

        for (Message message : messages) {
            VBox messageBubble = createMessageBubble(message);
            messagesVBox.getChildren().add(messageBubble);
        }

        // Scroll to bottom
        Platform.runLater(() -> {
            messagesScrollPane.setVvalue(1.0);
        });
    }

    private VBox createMessageBubble(Message message) {
        VBox bubble = new VBox(5);
        bubble.getStyleClass().add("message-bubble");

        boolean isCurrentUser = message.getSenderId().equals(currentUser != null ? currentUser.getId() : "");
        String bubbleStyle = isCurrentUser ? "message-bubble-user" : "message-bubble-other";

        if (Message.MessageType.SYSTEM.equals(message.getMessageType())) {
            bubbleStyle = "message-bubble-system";
        }

        bubble.setStyle("-fx-background-color: " +
                       (isCurrentUser ? "#1e40af" : Message.MessageType.SYSTEM.equals(message.getMessageType()) ? "#f3f4f6" : "white") +
                       "; -fx-text-fill: " +
                       (isCurrentUser || Message.MessageType.SYSTEM.equals(message.getMessageType()) ? "white" : "#1e293b") +
                       "; -fx-padding: 12px 16px; -fx-background-radius: 16px; -fx-max-width: 400px;");

        // Message content
        Label contentLabel = new Label(message.getContent());
        contentLabel.setWrapText(true);
        contentLabel.setStyle("-fx-font-size: 14px;");

        // File indicator for file messages
        if (Message.MessageType.FILE.equals(message.getMessageType()) && message.getFileName() != null) {
            HBox fileIndicator = new HBox(8);
            fileIndicator.setStyle("-fx-alignment: center-left; -fx-padding: 0 0 8 0;");

            ImageView fileIcon = new ImageView();
            fileIcon.setFitHeight(16);
            fileIcon.setFitWidth(16);

            if (message.getFileName().toLowerCase().endsWith(".jpg") ||
                message.getFileName().toLowerCase().endsWith(".png")) {
                fileIcon.setImage(new Image("/images/image-icon.png"));
            } else if (message.getFileName().toLowerCase().endsWith(".mp4") ||
                       message.getFileName().toLowerCase().endsWith(".avi")) {
                fileIcon.setImage(new Image("/images/video-icon.png"));
            } else {
                fileIcon.setImage(new Image("/images/file-icon.png"));
            }

            Label fileNameLabel = new Label(message.getFileName());
            fileNameLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: inherit;");

            fileIndicator.getChildren().addAll(fileIcon, fileNameLabel);
            bubble.getChildren().add(fileIndicator);
        }

        bubble.getChildren().add(contentLabel);

        // Timestamp and auto-delete info
        HBox metaBox = new HBox(10);
        metaBox.setStyle("-fx-alignment: center-left;");

        Label timeLabel = new Label(message.getFormattedTime());
        timeLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: " +
                          (isCurrentUser || Message.MessageType.SYSTEM.equals(message.getMessageType()) ?
                           "rgba(255,255,255,0.7)" : "#64748b") + ";");

        // Auto-delete indicator for officer messages
        if (isCurrentUser && currentUser != null && "officer".equals(currentUser.getRole()) &&
            Message.MessageType.TEXT.equals(message.getMessageType())) {
            Label deleteLabel = new Label("Auto-delete: 10 min");
            deleteLabel.setStyle("-fx-font-size: 10px; -fx-text-fill: rgba(255,255,255,0.5);");
            metaBox.getChildren().addAll(timeLabel, deleteLabel);
        } else {
            metaBox.getChildren().add(timeLabel);
        }

        bubble.getChildren().add(metaBox);

        return bubble;
    }

    private void refreshParticipantsDisplay() {
        participantsVBox.getChildren().clear();

        for (User participant : participants) {
            HBox participantItem = createParticipantItem(participant);
            participantsVBox.getChildren().add(participantItem);
        }
    }

    private HBox createParticipantItem(User participant) {
        HBox item = new HBox(12);
        item.setStyle("-fx-alignment: center-left; -fx-padding: 8px; -fx-background-radius: 8px;");
        item.getStyleClass().add("participant-item");

        // Avatar
        Circle avatar = new Circle(16);
        avatar.setFill(Color.web("#1e40af"));

        // Status indicator
        Circle statusIndicator = new Circle(4);
        statusIndicator.getStyleClass().add("status-indicator");

        String status = "online"; // Default for demo
        switch (status) {
            case "online" -> statusIndicator.setStyle("-fx-fill: #22c55e;");
            case "away" -> statusIndicator.setStyle("-fx-fill: #f59e0b;");
            case "offline" -> statusIndicator.setStyle("-fx-fill: #6b7280;");
        }

        // Name and ID
        VBox infoBox = new VBox(2);
        Label nameLabel = new Label(participant.getName());
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label idLabel = new Label("ID: " + participant.getPoliceId());
        idLabel.setStyle("-fx-font-size: 11px; -fx-text-fill: #64748b;");

        infoBox.getChildren().addAll(nameLabel, idLabel);

        item.getChildren().addAll(avatar, infoBox, statusIndicator);
        return item;
    }

    private void refreshFilesDisplay() {
        filesVBox.getChildren().clear();

        for (PoliceFile file : files) {
            VBox fileItem = createFileItem(file);
            filesVBox.getChildren().add(fileItem);
        }
    }

    private VBox createFileItem(PoliceFile file) {
        VBox item = new VBox(8);
        item.setStyle("-fx-background-color: #f8fafc; -fx-padding: 12px; -fx-background-radius: 8px;");

        // File info
        HBox fileInfo = new HBox(10);
        fileInfo.setStyle("-fx-alignment: center-left;");

        // File icon
        ImageView fileIcon = new ImageView();
        fileIcon.setFitHeight(20);
        fileIcon.setFitWidth(20);

        switch (file.getFileType()) {
            case IMAGE -> fileIcon.setImage(new Image("/images/image-icon.png"));
            case VIDEO -> fileIcon.setImage(new Image("/images/video-icon.png"));
            case DOCUMENT -> fileIcon.setImage(new Image("/images/file-icon.png"));
            case AUDIO -> fileIcon.setImage(new Image("/images/audio-icon.png"));
        }

        VBox detailsBox = new VBox(4);
        Label fileNameLabel = new Label(file.getFileName());
        fileNameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label fileSizeLabel = new Label(file.getFormattedFileSize() + " • " + file.getFormattedUploadDate());
        fileSizeLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        detailsBox.getChildren().addAll(fileNameLabel, fileSizeLabel);

        // Action buttons
        HBox actionsBox = new HBox(8);
        Button previewBtn = new Button("Preview");
        previewBtn.getStyleClass().add("button-secondary");
        previewBtn.setPrefHeight(30);

        Button downloadBtn = new Button("Download");
        downloadBtn.getStyleClass().add("button-primary");
        downloadBtn.setPrefHeight(30);

        actionsBox.getChildren().addAll(previewBtn, downloadBtn);

        fileInfo.getChildren().addAll(fileIcon, detailsBox);

        item.getChildren().addAll(fileInfo, actionsBox);
        return item;
    }

    @FXML
    private void handleBack() {
        try {
            // Return to dashboard based on user role
            if (currentUser != null && "admin".equals(currentUser.getRole())) {
                PoliceManagementSystem.switchScene("/fxml/AdminDashboard.fxml");
            } else {
                PoliceManagementSystem.switchScene("/fxml/OfficerDashboard.fxml");
            }
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to return to dashboard.");
        }
    }

    @FXML
    private void handleCall() {
        if (!isInCall) {
            startCall();
        } else {
            endCall();
        }
    }

    private void startCall() {
        isInCall = true;
        callStartTime = LocalDateTime.now();

        // Update UI
        callBtn.setText("End Call");
        callBtn.getStyleClass().remove("button-primary");
        callBtn.getStyleClass().add("button-danger");
        callBannerHBox.setVisible(true);

        // Start call timer
        callTimer = new Timer(true);
        callTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> updateCallDuration());
            }
        }, 1000, 1000);

        // Add call started message
        addSystemMessage("📞 Group call started by " + (currentUser != null ? currentUser.getName() : "User"));
    }

    @FXML
    private void handleEndCall() {
        endCall();
    }

    private void endCall() {
        isInCall = false;
        callStartTime = null;

        // Update UI
        callBtn.setText("📞 Call");
        callBtn.getStyleClass().remove("button-danger");
        callBtn.getStyleClass().add("button-primary");
        callBannerHBox.setVisible(false);

        // Stop call timer
        if (callTimer != null) {
            callTimer.cancel();
            callTimer = null;
        }

        // Add call ended message
        addSystemMessage("📞 Group call ended");
    }

    private void updateCallDuration() {
        if (callStartTime != null) {
            long seconds = java.time.Duration.between(callStartTime, LocalDateTime.now()).getSeconds();
            long minutes = seconds / 60;
            long remainingSeconds = seconds % 60;
            callDurationLabel.setText(String.format("Duration: %02d:%02d", minutes, remainingSeconds));
        }
    }

    @FXML
    private void handleMap() {
        // Show map for this room
        showAlert("Map Feature", "Interactive map feature is under development.");
    }

    @FXML
    private void handleSendMessage() {
        String content = messageInputField.getText().trim();
        if (content.isEmpty()) return;

        // Create new message
        String messageId = "msg_" + System.currentTimeMillis();
        Message message = new Message(messageId, currentRoom.getId(), currentUser.getId(), content);

        // Add to messages list
        messages.add(message);

        // Clear input field
        messageInputField.clear();

        // Refresh display
        refreshMessagesDisplay();
    }

    @FXML
    private void handleVoiceMessage() {
        if (!isRecording) {
            startRecording();
        } else {
            stopRecording();
        }
    }

    private void startRecording() {
        isRecording = true;
        voiceBtn.getStyleClass().add("recording");
        recordingIndicatorHBox.setVisible(true);

        // Simulate recording duration
        Timer recordingTimer = new Timer();
        recordingTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                Platform.runLater(() -> stopRecording());
            }
        }, 3000); // 3 seconds recording
    }

    private void stopRecording() {
        isRecording = false;
        voiceBtn.getStyleClass().remove("recording");
        recordingIndicatorHBox.setVisible(false);

        // Add voice message
        String messageId = "msg_" + System.currentTimeMillis();
        Message voiceMessage = new Message(messageId, currentRoom.getId(), currentUser.getId(), "Voice message recorded");
        voiceMessage.setMessageType(Message.MessageType.AUDIO);

        messages.add(voiceMessage);
        refreshMessagesDisplay();
    }

    @FXML
    private void handleFileUpload(String fileType) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select " + fileType + " file");

        // Set file filters based on type
        switch (fileType) {
            case "image" -> {
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Image Files", "*.png", "*.jpg", "*.jpeg", "*.gif")
                );
            }
            case "video" -> {
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("Video Files", "*.mp4", "*.avi", "*.mov")
                );
            }
            default -> {
                fileChooser.getExtensionFilters().add(
                    new FileChooser.ExtensionFilter("All Files", "*.*")
                );
            }
        }

        File selectedFile = fileChooser.showOpenDialog(new Stage());
        if (selectedFile != null) {
            uploadFile(selectedFile, fileType);
        }
    }

    private void uploadFile(File file, String fileType) {
        try {
            // In a real implementation, you would upload the file to a server
            // For demo, we'll just create a file record
            PoliceFile.FileType type = switch (fileType) {
                case "image" -> PoliceFile.FileType.IMAGE;
                case "video" -> PoliceFile.FileType.VIDEO;
                default -> PoliceFile.FileType.DOCUMENT;
            };

            PoliceFile policeFile = new PoliceFile(file.getName(), file.getAbsolutePath(),
                                                 file.length(), type, currentUser.getId());
            policeFile.setDescription("Uploaded via chat room");

            files.add(policeFile);

            // Add file message
            String messageId = "msg_" + System.currentTimeMillis();
            Message fileMessage = new Message(messageId, currentRoom.getId(), currentUser.getId(),
                                            "File uploaded: " + file.getName());
            fileMessage.setMessageType(Message.MessageType.FILE);
            fileMessage.setFileName(file.getName());

            messages.add(fileMessage);

            refreshFilesDisplay();
            refreshMessagesDisplay();

        } catch (Exception e) {
            showAlert("Upload Error", "Failed to upload file: " + e.getMessage());
        }
    }

    private void addSystemMessage(String content) {
        String messageId = "msg_" + System.currentTimeMillis();
        Message systemMessage = new Message(messageId, currentRoom.getId(), "system", content);
        systemMessage.setMessageType(Message.MessageType.SYSTEM);
        messages.add(systemMessage);
        refreshMessagesDisplay();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
    }

    public void setCurrentRoom(ChatRoom room) {
        this.currentRoom = room;
        updateRoomInfo();
    }

    @Override
    public void finalize() {
        if (autoDeleteTimer != null) {
            autoDeleteTimer.cancel();
        }
        if (callTimer != null) {
            callTimer.cancel();
        }
    }
}