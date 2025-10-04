package com.bangladeshpolice.controller;

import com.bangladeshpolice.DatabaseManager;
import com.bangladeshpolice.PoliceManagementSystem;
import com.bangladeshpolice.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for the Admin Dashboard
 */
public class AdminDashboardController implements Initializable {

    @FXML private VBox sidebarVBox;
    @FXML private Label userNameLabel;
    @FXML private Label userPoliceIdLabel;
    @FXML private Label userRoleLabel;
    @FXML private Circle userAvatarCircle;
    @FXML private Label pageTitleLabel;
    @FXML private VBox contentVBox;
    @FXML private ScrollPane contentScrollPane;

    // Navigation buttons
    @FXML private Button homeBtn;
    @FXML private Button chatRoomsBtn;
    @FXML private Button officersBtn;
    @FXML private Button filesBtn;
    @FXML private Button recordsBtn;
    @FXML private Button settingsBtn;
    @FXML private Button logoutBtn;

    // Dashboard content
    @FXML private Button createRoomBtn;
    @FXML private Card activeRoomsCard;
    @FXML private Card totalOfficersCard;
    @FXML private Card pendingReportsCard;
    @FXML private Card completedMissionsCard;
    @FXML private Label activeRoomsValueLabel;
    @FXML private Label activeRoomsChangeLabel;
    @FXML private Label totalOfficersValueLabel;
    @FXML private Label totalOfficersOnlineLabel;
    @FXML private Label pendingReportsValueLabel;
    @FXML private Label completedMissionsValueLabel;
    @FXML private TextField roomNameField;
    @FXML private Button createRoomSubmitBtn;
    @FXML private Button addOfficersBtn;
    @FXML private VBox activityListVBox;

    private DatabaseManager dbManager;
    private User currentUser;
    private String currentView = "home";

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();

        // Set up event handlers
        setupEventHandlers();

        // Load initial data
        loadDashboardData();

        // Set up navigation
        setupNavigation();

        // Initialize dashboard content
        showDashboardView();
    }

    private void setupEventHandlers() {
        homeBtn.setOnAction(e -> showDashboardView());
        chatRoomsBtn.setOnAction(e -> showChatRoomsView());
        officersBtn.setOnAction(e -> showOfficersView());
        filesBtn.setOnAction(e -> showFilesView());
        recordsBtn.setOnAction(e -> showRecordsView());
        settingsBtn.setOnAction(e -> showSettingsView());
        logoutBtn.setOnAction(e -> handleLogout());

        createRoomBtn.setOnAction(e -> showCreateRoomDialog());
        createRoomSubmitBtn.setOnAction(e -> handleCreateRoom());
        addOfficersBtn.setOnAction(e -> showAddOfficersDialog());
    }

    private void setupNavigation() {
        // Highlight current navigation item
        updateNavigationHighlight();
    }

    private void updateNavigationHighlight() {
        // Reset all button styles
        String defaultStyle = "-fx-background-color: transparent; -fx-text-fill: #e5e7eb; -fx-alignment: center-left;";

        homeBtn.setStyle(defaultStyle);
        chatRoomsBtn.setStyle(defaultStyle);
        officersBtn.setStyle(defaultStyle);
        filesBtn.setStyle(defaultStyle);
        recordsBtn.setStyle(defaultStyle);
        settingsBtn.setStyle(defaultStyle);

        // Highlight current view
        Button currentBtn = switch (currentView) {
            case "chat-rooms" -> chatRoomsBtn;
            case "officers" -> officersBtn;
            case "files" -> filesBtn;
            case "records" -> recordsBtn;
            case "settings" -> settingsBtn;
            default -> homeBtn;
        };

        currentBtn.setStyle("-fx-background-color: #1e40af; -fx-text-fill: white; -fx-alignment: center-left;");
    }

    private void showDashboardView() {
        currentView = "home";
        pageTitleLabel.setText("Dashboard");
        updateNavigationHighlight();
        loadDashboardData();
        updateDashboardContent();
    }

    private void showChatRoomsView() {
        currentView = "chat-rooms";
        pageTitleLabel.setText("Chat Rooms");
        updateNavigationHighlight();
        loadChatRoomsContent();
    }

    private void showOfficersView() {
        currentView = "officers";
        pageTitleLabel.setText("Manage Officers");
        updateNavigationHighlight();
        loadOfficersContent();
    }

    private void showFilesView() {
        currentView = "files";
        pageTitleLabel.setText("Files");
        updateNavigationHighlight();
        loadFilesContent();
    }

    private void showRecordsView() {
        currentView = "records";
        pageTitleLabel.setText("Criminal Records");
        updateNavigationHighlight();
        loadRecordsContent();
    }

    private void showSettingsView() {
        currentView = "settings";
        pageTitleLabel.setText("Settings");
        updateNavigationHighlight();
        loadSettingsContent();
    }

    private void loadDashboardData() {
        try {
            // Load statistics from database
            activeRoomsValueLabel.setText("12");
            activeRoomsChangeLabel.setText("+2 from yesterday");
            totalOfficersValueLabel.setText("45");
            totalOfficersOnlineLabel.setText("3 online now");
            pendingReportsValueLabel.setText("8");
            completedMissionsValueLabel.setText("156");

            // Load recent activity
            loadRecentActivity();

        } catch (Exception e) {
            System.err.println("Error loading dashboard data: " + e.getMessage());
        }
    }

    private void loadRecentActivity() {
        activityListVBox.getChildren().clear();

        // Sample activity items
        VBox activity1 = createActivityItem("Officer Rahman joined Operation Delta", "2 minutes ago", "active");
        VBox activity2 = createActivityItem("New evidence uploaded to Room Alpha", "15 minutes ago", "file");
        VBox activity3 = createActivityItem("Mission Beta marked as completed", "1 hour ago", "complete");

        activityListVBox.getChildren().addAll(activity1, activity2, activity3);
    }

    private VBox createActivityItem(String message, String time, String type) {
        VBox item = new VBox(5);
        item.getStyleClass().add("activity-item");
        item.setStyle("-fx-background-color: #f8fafc; -fx-padding: 12px; -fx-background-radius: 8px;");

        Label messageLabel = new Label(message);
        messageLabel.getStyleClass().add("activity-message");
        messageLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 14px;");

        Label timeLabel = new Label(time);
        timeLabel.getStyleClass().add("activity-time");
        timeLabel.setStyle("-fx-text-fill: #64748b; -fx-font-size: 12px;");

        Label typeLabel = new Label(type);
        typeLabel.getStyleClass().add("activity-type");
        if ("active".equals(type)) {
            typeLabel.setStyle("-fx-background-color: rgba(34, 197, 94, 0.1); -fx-text-fill: #22c55e; -fx-padding: 2px 8px; -fx-background-radius: 12px; -fx-font-size: 11px;");
        } else if ("file".equals(type)) {
            typeLabel.setStyle("-fx-background-color: rgba(59, 130, 246, 0.1); -fx-text-fill: #3b82f6; -fx-padding: 2px 8px; -fx-background-radius: 12px; -fx-font-size: 11px;");
        } else if ("complete".equals(type)) {
            typeLabel.setStyle("-fx-background-color: rgba(16, 185, 129, 0.1); -fx-text-fill: #10b981; -fx-padding: 2px 8px; -fx-background-radius: 12px; -fx-font-size: 11px;");
        }

        item.getChildren().addAll(messageLabel, timeLabel, typeLabel);
        return item;
    }

    private void updateDashboardContent() {
        // Show dashboard-specific content
        // This would typically load different content based on the current state
    }

    private void loadChatRoomsContent() {
        try {
            // Switch to chat rooms view
            PoliceManagementSystem.switchScene("/fxml/ChatRoomManager.fxml");
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load Chat Rooms view.");
        }
    }

    private void loadOfficersContent() {
        try {
            // Switch to officers view
            PoliceManagementSystem.switchScene("/fxml/OfficerManager.fxml");
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load Officers view.");
        }
    }

    private void loadFilesContent() {
        try {
            // Switch to files view
            PoliceManagementSystem.switchScene("/fxml/FileManager.fxml");
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load Files view.");
        }
    }

    private void loadRecordsContent() {
        try {
            // Switch to records view
            PoliceManagementSystem.switchScene("/fxml/CriminalRecords.fxml");
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load Criminal Records view.");
        }
    }

    private void loadSettingsContent() {
        // Show settings content in current view
        pageTitleLabel.setText("Settings");
        showAlert("Settings", "Settings panel is under development.");
    }

    private void showCreateRoomDialog() {
        // Show create room dialog
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle("Create New Chat Room");
        dialog.setHeaderText("Enter the details for the new mission room");

        // Create dialog content
        VBox content = new VBox(15);
        content.setPrefWidth(400);

        TextField roomNameField = new TextField();
        roomNameField.setPromptText("Enter room name (e.g., Operation Sunrise)");

        TextArea descriptionField = new TextArea();
        descriptionField.setPromptText("Enter room description (optional)");
        descriptionField.setPrefRowCount(3);

        content.getChildren().addAll(
            new Label("Room Name:"),
            roomNameField,
            new Label("Description:"),
            descriptionField
        );

        dialog.getDialogPane().setContent(content);
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Handle dialog result
        dialog.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                String roomName = roomNameField.getText().trim();
                if (!roomName.isEmpty()) {
                    handleCreateRoomFromDialog(roomName, descriptionField.getText().trim());
                } else {
                    showAlert("Input Error", "Room name is required.");
                }
            }
        });
    }

    private void handleCreateRoomFromDialog(String roomName, String description) {
        try {
            // Create room in database
            String roomId = dbManager.createChatRoom(roomName, description, currentUser != null ? currentUser.getId() : "admin-1");

            // Refresh dashboard
            loadDashboardData();
            showAlert("Success", "Chat room '" + roomName + "' created successfully!");

        } catch (SQLException e) {
            showAlert("Database Error", "Failed to create chat room: " + e.getMessage());
        }
    }

    @FXML
    private void handleCreateRoom() {
        String roomName = roomNameField.getText().trim();
        if (roomName.isEmpty()) {
            showAlert("Input Error", "Please enter a room name.");
            return;
        }

        handleCreateRoomFromDialog(roomName, "");
        roomNameField.clear();
    }

    private void showAddOfficersDialog() {
        showAlert("Add Officers", "Officer assignment feature is under development.");
    }

    private void handleLogout() {
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Logout Confirmation");
        confirmation.setHeaderText("Are you sure you want to logout?");
        confirmation.setContentText("You will be returned to the login screen.");

        confirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                try {
                    PoliceManagementSystem.switchScene("/fxml/LandingPage.fxml");
                } catch (IOException e) {
                    showAlert("Logout Error", "Unable to return to login screen.");
                }
            }
        });
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
        updateUserInfo();
    }

    private void updateUserInfo() {
        if (currentUser != null) {
            userNameLabel.setText(currentUser.getName());
            userRoleLabel.setText(currentUser.getRole().toUpperCase());
            if (currentUser.getPoliceId() != null) {
                userPoliceIdLabel.setText("ID: " + currentUser.getPoliceId());
            }
        }
    }
}