package com.bangladeshpolice;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import java.io.IOException;

/**
 * Bangladesh Police Management System - Main Application Class
 * A comprehensive police station management system with real-time communication,
 * file sharing, criminal records management, and officer tracking capabilities.
 */
public class PoliceManagementSystem extends Application {

    private static Stage primaryStage;
    private static Scene mainScene;

    @Override
    public void start(Stage stage) throws IOException {
        primaryStage = stage;

        // Set application properties
        stage.setTitle("Bangladesh Police - Station Portal");
        stage.setMinWidth(1200);
        stage.setMinHeight(800);
        stage.initStyle(StageStyle.DECORATED);

        // Load main scene
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/fxml/LandingPage.fxml"));
        Parent root = loader.load();
        mainScene = new Scene(root, 1500, 900);

        // Apply global styles
        mainScene.getStylesheets().add(getClass().getResource("/css/styles.css").toExternalForm());

        stage.setScene(mainScene);
        stage.show();

        // Initialize database and sample data
        DatabaseManager.getInstance().initializeDatabase();
        SampleDataLoader.loadSampleData();
    }

    public static void switchScene(String fxmlPath) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(PoliceManagementSystem.class.getResource(fxmlPath));
        Parent root = loader.load();
        mainScene.setRoot(root);
    }

    public static Stage getPrimaryStage() {
        return primaryStage;
    }

    public static void main(String[] args) {
        launch(args);
    }
}