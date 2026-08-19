package it.unipi.client;

import it.unipi.client.model.Medico;
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
    @FXML private Medico medico;
    
    private Runnable bookButtonClicked;
    
    public DoctorCard(Medico medico) {
        
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("doctorCard.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Errore nel caricamento del file FXML doctorCard", exception);
        }
        
        this.medico = medico;
        
        String inizialeNome = medico.getNome().substring(0,1).toUpperCase();
        String inizialeCognome = medico.getCognome().substring(0,1).toUpperCase();
        
        avatarText.setText(inizialeNome + inizialeCognome);
        doctorNameLabel.setText("Dr. " + medico.getNome() + " " + medico.getCognome());
        specializationLabel.setText(medico.getSpecializzazione());
        
    }
    
    @FXML
    public void bookAppointment(ActionEvent e){
        if(bookButtonClicked != null) bookButtonClicked.run();
    }

    public void setBookButtonClicked(Runnable bookButtonClicked) {
        this.bookButtonClicked = bookButtonClicked;
    }
    
}
