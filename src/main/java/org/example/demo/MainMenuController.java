package org.example.demo;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.example.demo.models.Reservation;
import org.example.demo.services.ReservationService;

import java.io.IOException;

public class MainMenuController {

    private ReservationService reservationService = new ReservationService();

    @FXML
    private TableView<Reservation> tableView;
    @FXML
    private TableColumn<Reservation, String> reservationID;
    @FXML
    private TableColumn<Reservation, String> customerName;
    @FXML
    private TableColumn<Reservation, String> roomNumber;
    @FXML
    private TableColumn<Reservation, String> reservationDate;
    @FXML
    private TableColumn<Reservation, String> amount;
    @FXML
    private TableColumn<Reservation, String> checkInDate;
    @FXML
    private TableColumn<Reservation, Void> actionCol;

    private ObservableList<Reservation> data = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        // Initialize table columns
        reservationID.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getIdAsString()));
        customerName.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCustomer().getName()));
        roomNumber.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getRoom().getRoomNumber()));
        reservationDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getReservationDate().toString()));
        amount.setCellValueFactory(cellData -> new SimpleStringProperty(String.valueOf(cellData.getValue().getAmount())));
        checkInDate.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getCheckInDate().toString()));

        // Setup action column
        setupActionColumn();

        // Load reservations
        loadReservations();
    }

    private void setupActionColumn() {
        Callback<TableColumn<Reservation, Void>, TableCell<Reservation, Void>> cellFactory = new Callback<>() {
            @Override
            public TableCell<Reservation, Void> call(final TableColumn<Reservation, Void> param) {
                return new TableCell<>() {
                    private final Button btn = new Button("Remove");

                    {
                        btn.setOnAction(event -> {
                            Reservation reservation = getTableView().getItems().get(getIndex());
                            reservationService.deleteReservation(reservation.getId()); // Example: Delete reservation from service
                            getTableView().getItems().remove(reservation);
                            System.out.println("Removed reservation: " + reservation.getIdAsString());
                        });
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            setGraphic(btn);
                        }
                    }
                };
            }
        };

        actionCol.setCellFactory(cellFactory);
    }

    private void loadReservations() {
        // Example: Fetch reservations from service
        data.addAll(reservationService.getAllReservations());
        tableView.setItems(data);
    }

    @FXML
    private void toAddNewReservation(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add_reservation.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void toAddNewCategory(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add_category.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }
    @FXML
    private void toAddNewRoom(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((javafx.scene.Node) event.getSource()).getScene().getWindow();
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add_room.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600);
        stage.setTitle("Main Menu");
        stage.setScene(scene);
        stage.show();
    }

}
