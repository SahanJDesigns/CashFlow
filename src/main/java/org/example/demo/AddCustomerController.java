package org.example.demo;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.demo.models.Customer;
import org.example.demo.services.CustomerService;

import java.io.IOException;

public class AddCustomerController {
    private final CustomerService customerService = new CustomerService();
    @FXML
    private TextField name_field;
    @FXML
    private TextField email_field;
    @FXML
    private TextField telephone_field;
    @FXML
    private TextField address_field;
    @FXML
    public void onBtnClick(ActionEvent event) throws IOException {
        String name = name_field.getText();
        String email = email_field.getText();
        String tel = telephone_field.getText();
        String address = address_field.getText();
        Customer customer = new Customer(name,email,tel,address);
        customerService.addCustomer(customer);
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
