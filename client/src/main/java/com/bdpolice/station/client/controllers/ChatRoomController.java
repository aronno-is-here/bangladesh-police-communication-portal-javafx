package com.bdpolice.station.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

public class ChatRoomController {
    @FXML private Label roomTitle;
    @FXML private VBox messagesBox;
    @FXML private TextField messageField;
    @FXML private ListView<String> participantsList;
    @FXML private ListView<String> filesList;

    private boolean inCall = false;

    @FXML public void initialize() {
        roomTitle.setText("Operation Sunrise");
    }

    @FXML public void goBack() { messageField.getScene().getWindow().hide(); }

    @FXML public void toggleCall() {
        inCall = !inCall;
        // TODO: broadcast call state via WebSocket
    }

    @FXML public void openMap() {
        // TODO: show map dialog WebView (Leaflet)
    }

    @FXML public void attachImage() {}
    @FXML public void attachVideo() {}
    @FXML public void attachFile() {}

    @FXML public void toggleRecord() {}

    @FXML public void sendMessage() {
        String text = messageField.getText();
        if (text == null || text.isBlank()) return;
        Label bubble = new Label(text);
        bubble.setStyle("-fx-background-color: #1e40af; -fx-text-fill: white; -fx-padding: 8 12 8 12; -fx-background-radius: 10;");
        messagesBox.getChildren().add(bubble);
        messageField.clear();
        // TODO: post to backend and auto-delete after 10 minutes handled server-side
    }

    @FXML public void verifyFilesAccess() {
        // TODO: prompt for Police ID and then load files list
    }
}
