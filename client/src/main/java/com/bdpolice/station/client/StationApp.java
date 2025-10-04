package com.bdpolice.station.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class StationApp extends Application {
    public static void main(String[] args) { launch(args); }

    @Override
    public void start(Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/Landing.fxml"));
        Parent root = loader.load();
        Scene scene = new Scene(root, 1500, 900);
        scene.getStylesheets().add(getClass().getResource("/css/app.css").toExternalForm());
        stage.setTitle("Bangladesh Police - Station Portal");
        stage.setScene(scene);
        stage.show();
    }
}
