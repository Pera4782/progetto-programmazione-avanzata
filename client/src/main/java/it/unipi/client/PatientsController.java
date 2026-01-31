package it.unipi.client;

import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.event.ActionEvent;

public class PatientsController {

    @FXML
    private TextField searchDoctor;

    @FXML
    private ComboBox<?> specializationCombo;

    @FXML
    private FlowPane doctorsGallery;

    @FXML
    void handleShowBooked(ActionEvent event) {
        System.out.println("Show booked appointments");
    }

}
