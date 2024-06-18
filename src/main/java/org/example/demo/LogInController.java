package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.models.User;
import org.example.demo.services.UserService;

import java.io.IOException;
import java.util.List;

public class LogInController {

    private UserService userService = new UserService();

    @FXML
    private TextField username_field;

    @FXML
    private TextField password_field;

    @FXML
    public void onBtnClk(ActionEvent event) {
        try {
            String username = username_field.getText();
            String password = password_field.getText();
            boolean passed = userService.login(username, password);

            if (passed) {
                Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/demo/main_menu.fxml"));
                Scene scene = new Scene(fxmlLoader.load(), 800, 600);
                stage.setTitle("Main Menu");
                stage.setScene(scene);
                stage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Invalid Credentials");
                alert.setContentText("Please enter a valid username and password");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void onSignUpTextClk(ActionEvent event) {
        try {
            Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add_user.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);
            stage.setTitle("Sign Up");
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
