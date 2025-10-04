package com.bdpolice.station.client.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class OfficerController {
    @FXML private StackPane contentRoot;

    @FXML public void initialize() { showHome(); }

    @FXML public void showHome() { load("/fxml/officer/Home.fxml"); }
    @FXML public void showRooms() { load("/fxml/officer/Rooms.fxml"); }
    @FXML public void showRecords() { load("/fxml/admin/Records.fxml"); }
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
