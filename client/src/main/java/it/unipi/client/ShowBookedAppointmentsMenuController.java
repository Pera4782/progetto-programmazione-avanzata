package it.unipi.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.FlowPane;

public class ShowBookedAppointmentsMenuController {

    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private Button filterButton;
    @FXML private Button logoutButton;
    @FXML private FlowPane appointmentsContainer;

    

    @FXML
    public void initialize() {
        // Inizializza i filtri nella ComboBox
        statusFilterComboBox.getItems().addAll("Tutti", "Programmato", "Completato");
        statusFilterComboBox.getSelectionModel().selectFirst();
    }

    
    @FXML
    private void filterAppointments() {
        String selectedStatus = statusFilterComboBox.getValue();
        if (selectedStatus != null) {
            renderAppointments(selectedStatus);
        }
    }


    @FXML
    private void backToPatientMenu() {
        // Gestisci la transizione alla schermata del menu paziente
    }

    
    @FXML
    private void logout(ActionEvent event) {
        // Gestisci la logica di logout
    }

    
    private void renderAppointments(String statusFilter) {
        appointmentsContainer.getChildren().clear();
    }
}