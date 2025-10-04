package com.bdpolice.station.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class LandingController {
    @FXML private TextField adminUsername;
    @FXML private PasswordField adminPassword;
    @FXML private TextField officerId;
    @FXML private PasswordField officerPassword;

    @FXML
    public void onAdminLogin() {
        if (!isBlank(adminUsername.getText()) && !isBlank(adminPassword.getText())) {
            navigateToAdmin();
        }
    }

    @FXML
    public void onOfficerLogin() {
        if (!isBlank(officerId.getText()) && !isBlank(officerPassword.getText())) {
            navigateToOfficer();
        }
    }

    private void navigateToAdmin() {
        try {
            Stage stage = (Stage) adminUsername.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/AdminShell.fxml"));
            stage.setScene(new Scene(root, 1500, 900));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void navigateToOfficer() {
        try {
            Stage stage = (Stage) officerId.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/fxml/OfficerShell.fxml"));
            stage.setScene(new Scene(root, 1500, 900));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
}
