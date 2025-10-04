package com.bdpolice.station.client.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.*;

public class RecordsController {
    @FXML private TextField searchField;
    @FXML private ChoiceBox<String> genderChoice;
    @FXML private ChoiceBox<String> statusChoice;
    @FXML private TableView<?> recordsTable;

    @FXML public void initialize() {
        genderChoice.getSelectionModel().selectFirst();
        statusChoice.getSelectionModel().selectFirst();
    }

    @FXML public void clearFilters() {
        searchField.clear();
        genderChoice.getSelectionModel().selectFirst();
        statusChoice.getSelectionModel().selectFirst();
    }
}
