package bdpolice.client.controller;

import javafx.event.ActionEvent;
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
  public void onAdminLogin(ActionEvent e) throws Exception {
    if (!adminUsername.getText().isBlank() && !adminPassword.getText().isBlank()) {
      goToAdmin();
    }
  }

  @FXML
  public void onOfficerLogin(ActionEvent e) throws Exception {
    if (!officerId.getText().isBlank() && !officerPassword.getText().isBlank()) {
      goToOfficer();
    }
  }

  private void goToAdmin() throws Exception {
    Stage stage = (Stage) adminUsername.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("/bdpolice/client/view/AdminDashboard.fxml"));
    stage.setScene(new Scene(root, 1500, 900));
  }

  private void goToOfficer() throws Exception {
    Stage stage = (Stage) officerId.getScene().getWindow();
    Parent root = FXMLLoader.load(getClass().getResource("/bdpolice/client/view/OfficerDashboard.fxml"));
    stage.setScene(new Scene(root, 1500, 900));
  }
}
