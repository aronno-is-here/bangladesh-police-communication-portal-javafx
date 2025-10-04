package com.bangladeshpolice.controller;

import com.bangladeshpolice.DatabaseManager;
import com.bangladeshpolice.PoliceManagementSystem;
import com.bangladeshpolice.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.scene.paint.Color;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller for the Officer Dashboard
 */
public class OfficerDashboardController implements Initializable {

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
    @FXML private Button recordsBtn;
    @FXML private Button mapBtn;
    @FXML private Button logoutBtn;

    // Dashboard content
    @FXML private Circle welcomeAvatarCircle;
    @FXML private Label welcomeLabel;
    @FXML private Card activeMissionsCard;
    @FXML private Card filesUploadedCard;
    @FXML private Card teamMembersCard;
    @FXML private Label activeMissionsValueLabel;
    @FXML private Label filesUploadedValueLabel;
    @FXML private Label teamMembersValueLabel;
    @FXML private VBox missionsListVBox;

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
        recordsBtn.setOnAction(e -> showRecordsView());
        mapBtn.setOnAction(e -> showMapView());
        logoutBtn.setOnAction(e -> handleLogout());
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
        recordsBtn.setStyle(defaultStyle);
        mapBtn.setStyle(defaultStyle);

        // Highlight current view
        Button currentBtn = switch (currentView) {
            case "chat-rooms" -> chatRoomsBtn;
            case "records" -> recordsBtn;
            case "map" -> mapBtn;
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
        pageTitleLabel.setText("My Chat Rooms");
        updateNavigationHighlight();

        try {
            // For demo, we'll show a simple chat rooms view
            showAlert("Chat Rooms", "Chat rooms functionality is available in the full implementation.");
        } catch (Exception e) {
            // Handle navigation to chat rooms
        }
    }

    private void showRecordsView() {
        currentView = "records";
        pageTitleLabel.setText("Criminal Records");
        updateNavigationHighlight();

        try {
            PoliceManagementSystem.switchScene("/fxml/CriminalRecords.fxml");
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load Criminal Records view.");
        }
    }

    private void showMapView() {
        currentView = "map";
        pageTitleLabel.setText("Map Tracking");
        updateNavigationHighlight();

        try {
            PoliceManagementSystem.switchScene("/fxml/PoliceMap.fxml");
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load Map view.");
        }
    }

    private void loadDashboardData() {
        // Load officer-specific data
        if (currentUser != null) {
            welcomeLabel.setText("Welcome back, " + currentUser.getName() + "!");
            activeMissionsValueLabel.setText("2");
            filesUploadedValueLabel.setText("8");
            teamMembersValueLabel.setText("12");

            // Load assigned missions
            loadAssignedMissions();
        }
    }

    private void loadAssignedMissions() {
        missionsListVBox.getChildren().clear();

        // Sample mission items
        VBox mission1 = createMissionItem("Operation Sunrise", "active", "5 min ago", "4 members");
        VBox mission2 = createMissionItem("Patrol Route 7", "active", "12 min ago", "2 members");
        VBox mission3 = createMissionItem("Traffic Control", "completed", "1 hour ago", "6 members");

        missionsListVBox.getChildren().addAll(mission1, mission2, mission3);
    }

    private VBox createMissionItem(String name, String status, String lastActivity, String members) {
        VBox item = new VBox(5);
        item.getStyleClass().add("mission-item");
        item.setStyle("-fx-background-color: #f8fafc; -fx-padding: 15px; -fx-background-radius: 8px; -fx-cursor: hand;");

        // Add click handler for entering room
        item.setOnMouseClicked(e -> enterMissionRoom(name));

        Label nameLabel = new Label(name);
        nameLabel.setStyle("-fx-font-weight: bold; -fx-font-size: 16px;");

        HBox statusBox = new HBox(10);
        Label statusLabel = new Label(status);
        statusLabel.setStyle("-fx-background-color: " +
                           ("active".equals(status) ? "rgba(16, 185, 129, 0.1)" : "rgba(107, 114, 128, 0.1)") +
                           "; -fx-text-fill: " +
                           ("active".equals(status) ? "#10b981" : "#6b7280") +
                           "; -fx-padding: 4px 8px; -fx-background-radius: 12px; -fx-font-size: 12px;");

        Label activityLabel = new Label("Last activity: " + lastActivity);
        activityLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        Label membersLabel = new Label(members);
        membersLabel.setStyle("-fx-font-size: 12px; -fx-text-fill: #64748b;");

        statusBox.getChildren().addAll(statusLabel, activityLabel, membersLabel);

        item.getChildren().addAll(nameLabel, statusBox);
        return item;
    }

    private void enterMissionRoom(String roomName) {
        // In a real implementation, this would navigate to the chat room
        showAlert("Enter Room", "Entering chat room: " + roomName);
    }

    private void updateDashboardContent() {
        // Update dashboard-specific content
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