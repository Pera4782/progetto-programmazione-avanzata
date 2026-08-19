package it.unipi.client;

import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class DoctorCard extends VBox{
    
    @FXML private Label avatarText;
    @FXML private Label doctorNameLabel;
    @FXML private Label specializationLabel;
    @FXML private Button bookButton;
    
    public DoctorCard(String nome, String cognome, String specializzazione) {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Errore nel caricamento del file FXML DoctorCard", exception);
        }
        
        String inizialeNome = nome.substring(0,1).toUpperCase();
        String inizialeCognome = cognome.substring(0,1).toUpperCase();
        
        avatarText.setText(inizialeNome + inizialeCognome);
        doctorNameLabel.setText("Dr. " + nome + " " + cognome);
        specializationLabel.setText(specializzazione);
        
    }
    
    
    @FXML
    public void bookAppointment(ActionEvent e){
        
    }
    
}
