package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import org.example.demo.models.Room;
import org.example.demo.models.RoomCategory;
import org.example.demo.services.RoomCategoryService;
import org.example.demo.services.RoomService;

import java.io.IOException;

public class AddRoomController {

    private RoomCategoryService roomCategoryService = new RoomCategoryService();
    private RoomService roomService = new RoomService();

    @FXML
    private TextField room_number_field;

    @FXML
    private TextField categoryID_field;

    @FXML
    private TextField room_cost_field;

    @FXML
    public void submitBtnClick(ActionEvent event) throws IOException {
        String roomNum = room_number_field.getText();
        String categoryId = categoryID_field.getText();
        String roomCost = room_cost_field.getText();

        try {
            int categoryIdInt = Integer.parseInt(categoryId);
            RoomCategory category = roomCategoryService.getRoomCategory(categoryIdInt);
            if(category != null) {
                Room room = new Room(roomNum, category, roomCost, "Empty");
                roomService.addRoom(room);
                toMainMenu(event);
            } else {
                showAlert("Error", "Invalid categoryID", AlertType.ERROR);
            }
        } catch (NumberFormatException e) {
            showAlert("Error", "Category ID must be numbers", AlertType.ERROR);
        }
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
    private void showAlert(String title, String message, AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
