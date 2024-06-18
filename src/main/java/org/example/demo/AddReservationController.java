package org.example.demo;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.models.Customer;
import org.example.demo.models.Reservation;
import org.example.demo.models.Room;
import org.example.demo.services.CustomerService;
import org.example.demo.services.ReservationService;
import org.example.demo.services.RoomService;
import javafx.event.ActionEvent;

import java.io.IOException;
import java.time.LocalDate;

public class AddReservationController {
    private CustomerService customerService = new CustomerService();
    private RoomService roomService = new RoomService();
    private ReservationService reservationService = new ReservationService();
    @FXML
    private TextField customer_field;
    @FXML
    private TextField room_field;
    @FXML
    private TextField package_field;
    @FXML
    private DatePicker reservation_date_field;
    @FXML
    public void onBtnClk(ActionEvent event) throws IOException {
        int customerId = Integer.parseInt(customer_field.getText());
        int roomId = Integer.parseInt(room_field.getText());

        Customer customer = customerService.getCustomer(customerId);
        Room room = roomService.getRoom(roomId);

        String packageType = package_field.getText();
        LocalDate reservationDate = reservation_date_field.getValue();
        LocalDate checkInDate = LocalDate.now();
        Reservation reservation = new Reservation(customer,room,packageType,reservationDate,checkInDate,2000);
        reservationService.addReservation(reservation);
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
