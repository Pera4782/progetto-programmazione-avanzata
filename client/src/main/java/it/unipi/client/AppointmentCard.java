package it.unipi.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class AppointmentCard extends VBox {

    @FXML private Label doctorNameLabel;
    @FXML private Label specializationLabel;
    @FXML private Label statusBadge;
    @FXML private Label dateTimeLabel;
    @FXML private Button actionButton;
    
    private Runnable actionButtonClicked;

    public AppointmentCard() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("AppointmentCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare AppointmentCard.fxml", e);
        }
    }

    public void setActionButton(Button actionButton) {
        this.actionButton = actionButton;
    }
    @FXML
    private void handleAction(ActionEvent event) {
        if(actionButtonClicked != null) actionButtonClicked.run();
    }
}