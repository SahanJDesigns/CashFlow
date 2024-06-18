package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.models.RoomCategory;
import org.example.demo.services.RoomCategoryService;

import java.io.IOException;

public class AddCategoryController {
    private final RoomCategoryService roomCategoryService = new RoomCategoryService();
    @FXML
    private TextField category_name;
    @FXML
    private TextField category_details;
    @FXML
    public void onSubmitButtonClick(ActionEvent event) throws IOException {
        String name = category_name.getText();
        String details = category_details.getText();
        RoomCategory roomCategory = new RoomCategory(name,details);
        roomCategoryService.addRoomCategory(roomCategory);
        toMainMenu(event);
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
