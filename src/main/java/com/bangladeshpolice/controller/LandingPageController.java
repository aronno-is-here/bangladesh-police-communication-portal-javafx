package com.bangladeshpolice.controller;

import com.bangladeshpolice.DatabaseManager;
import com.bangladeshpolice.PoliceManagementSystem;
import com.bangladeshpolice.model.User;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

/**
 * Controller for the Landing Page (Login Screen)
 */
public class LandingPageController implements Initializable {

    @FXML private TabPane loginTabPane;
    @FXML private TextField adminUsernameField;
    @FXML private PasswordField adminPasswordField;
    @FXML private Button adminLoginBtn;
    @FXML private TextField officerPoliceIdField;
    @FXML private PasswordField officerPasswordField;
    @FXML private Button officerLoginBtn;
    @FXML private Label titleLabel;
    @FXML private Label subtitleLabel;

    private DatabaseManager dbManager;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dbManager = DatabaseManager.getInstance();

        // Set up event handlers
        setupEventHandlers();

        // Set up input validation
        setupInputValidation();

        // Add keyboard navigation
        setupKeyboardNavigation();
    }

    private void setupEventHandlers() {
        adminLoginBtn.setOnAction(e -> handleAdminLogin());
        officerLoginBtn.setOnAction(e -> handleOfficerLogin());
    }

    private void setupInputValidation() {
        // Admin login validation
        adminUsernameField.textProperty().addListener((obs, oldVal, newVal) -> {
            adminLoginBtn.setDisable(newVal.trim().isEmpty() || adminPasswordField.getText().trim().isEmpty());
        });

        adminPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            adminLoginBtn.setDisable(newVal.trim().isEmpty() || adminUsernameField.getText().trim().isEmpty());
        });

        // Officer login validation
        officerPoliceIdField.textProperty().addListener((obs, oldVal, newVal) -> {
            officerLoginBtn.setDisable(newVal.trim().isEmpty() || officerPasswordField.getText().trim().isEmpty());
        });

        officerPasswordField.textProperty().addListener((obs, oldVal, newVal) -> {
            officerLoginBtn.setDisable(newVal.trim().isEmpty() || officerPoliceIdField.getText().trim().isEmpty());
        });
    }

    private void setupKeyboardNavigation() {
        // Enter key support for login
        adminPasswordField.setOnKeyPressed(this::handleEnterKey);
        officerPasswordField.setOnKeyPressed(this::handleEnterKey);
    }

    private void handleEnterKey(KeyEvent event) {
        if (event.getCode() == KeyCode.ENTER) {
            if (event.getSource() == adminPasswordField) {
                handleAdminLogin();
            } else if (event.getSource() == officerPasswordField) {
                handleOfficerLogin();
            }
        }
    }

    @FXML
    private void handleAdminLogin() {
        String username = adminUsernameField.getText().trim();
        String password = adminPasswordField.getText();

        if (username.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both username and password.");
            return;
        }

        try {
            if (dbManager.authenticateUser(username, password)) {
                User user = dbManager.getUserByUsername(username);
                if (user != null) {
                    dbManager.updateLastLogin(user.getId());

                    // Navigate to admin dashboard
                    PoliceManagementSystem.switchScene("/fxml/AdminDashboard.fxml");

                    // Clear fields
                    clearAdminFields();
                } else {
                    showAlert("Login Failed", "User not found or account is inactive.");
                }
            } else {
                showAlert("Login Failed", "Invalid username or password.");
            }
        } catch (SQLException | IOException e) {
            showAlert("Connection Error", "Unable to connect to the database. Please try again.");
            e.printStackTrace();
        }
    }

    @FXML
    private void handleOfficerLogin() {
        String policeId = officerPoliceIdField.getText().trim();
        String password = officerPasswordField.getText();

        if (policeId.isEmpty() || password.isEmpty()) {
            showAlert("Login Error", "Please enter both Police ID and password.");
            return;
        }

        try {
            // For demo purposes, use a simple authentication
            // In production, you'd validate against the database
            if (isValidOfficerCredentials(policeId, password)) {
                User user = new User("officer-1", "Officer Rahman", "rahman", "officer");
                user.setPoliceId(policeId);

                // Navigate to officer dashboard
                PoliceManagementSystem.switchScene("/fxml/OfficerDashboard.fxml");

                // Clear fields
                clearOfficerFields();
            } else {
                showAlert("Login Failed", "Invalid Police ID or password.");
            }
        } catch (IOException e) {
            showAlert("Navigation Error", "Unable to load the dashboard.");
            e.printStackTrace();
        }
    }

    private boolean isValidOfficerCredentials(String policeId, String password) {
        // Demo validation - in production, check against database
        return policeId.matches("BD\\d{3}") && !password.isEmpty();
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void clearAdminFields() {
        adminUsernameField.clear();
        adminPasswordField.clear();
        adminLoginBtn.setDisable(true);
    }

    private void clearOfficerFields() {
        officerPoliceIdField.clear();
        officerPasswordField.clear();
        officerLoginBtn.setDisable(true);
    }
}