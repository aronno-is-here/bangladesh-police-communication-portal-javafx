package bdpolice.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainApp extends Application {
  @Override
  public void start(Stage stage) throws Exception {
    Parent root = FXMLLoader.load(getClass().getResource("/bdpolice/client/view/Landing.fxml"));
    Scene scene = new Scene(root, 1500, 900);
    stage.setTitle("Bangladesh Police - Station Portal");
    stage.setScene(scene);
    stage.show();
  }
}
