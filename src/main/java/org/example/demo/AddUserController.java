package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.models.User;
import org.example.demo.services.UserService;

import java.io.IOException;

public class AddUserController {
    UserService userService = new UserService();
    @FXML
    private TextField username_field;
    @FXML
    private TextField role_field;
    @FXML
    private TextField password_field;
    @FXML
    private TextField confirm_password_field;

    @FXML
    public void onBtnClick(ActionEvent event) throws IOException {
        String username = username_field.getText();
        String role = role_field.getText();
        String password = password_field.getText();
        String confirmPassword = confirm_password_field.getText();
        if(confirmPassword.equals(password)){
            User user = new User(username,password,role);
            userService.addUser(user);
            toMainMenu(event);
        }else{
            showAlert("Alert","New Password And Confirm Password Does Not Match", Alert.AlertType.INFORMATION);
        }
    }
    private void showAlert(String title, String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
    @FXML
    private void toMainMenu(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("main_menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
}

