package com.bdpolice.station.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RoomsController {
    @FXML private TextField searchField;
    @FXML private TableView<?> roomsTable;

    @FXML public void initialize() {
        // TODO: load rooms from backend
    }

    @FXML public void openCreateDialog() {
        // TODO: show dialog to create room and assign officers with search and photo
    }
}
