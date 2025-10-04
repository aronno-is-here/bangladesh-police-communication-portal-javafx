package com.bdpolice.station.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class AdminController {
    @FXML private StackPane contentRoot;

    @FXML public void initialize() { showHome(); }

    @FXML public void showHome() { load("/fxml/admin/Home.fxml"); }
    @FXML public void showRooms() { load("/fxml/admin/Rooms.fxml"); }
    @FXML public void showFiles() { load("/fxml/admin/Files.fxml"); }
    @FXML public void showRecords() { load("/fxml/admin/Records.fxml"); }
    @FXML public void showSettings() { load("/fxml/admin/Settings.fxml"); }
    @FXML public void logout() { contentRoot.getScene().getWindow().hide(); }

    private void load(String path) {
        try {
            Node node = FXMLLoader.load(getClass().getResource(path));
            contentRoot.getChildren().setAll(node);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
