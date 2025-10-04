package bdpolice.client.controller;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class AdminController {
  @FXML private StackPane contentRoot;

  @FXML
  public void initialize() throws Exception {
    showHome();
  }

  public void showHome() throws Exception {
    setCenter(load("/bdpolice/client/view/admin/Home.fxml"));
  }
  public void showRooms() throws Exception {
    setCenter(load("/bdpolice/client/view/admin/Rooms.fxml"));
  }
  public void showOfficers() throws Exception {
    setCenter(load("/bdpolice/client/view/admin/Officers.fxml"));
  }
  public void showFiles() throws Exception {
    setCenter(load("/bdpolice/client/view/admin/Files.fxml"));
  }
  public void showRecords() throws Exception {
    setCenter(load("/bdpolice/client/view/common/CriminalRecords.fxml"));
  }
  public void showSettings() throws Exception {
    setCenter(load("/bdpolice/client/view/admin/Settings.fxml"));
  }
  public void logout() throws Exception {
    contentRoot.getScene().getWindow().hide();
  }

  private Node load(String path) throws Exception {
    return FXMLLoader.load(getClass().getResource(path));
  }
  private void setCenter(Node node) {
    contentRoot.getChildren().setAll(node);
  }
}
