package it.unipi.client;

import it.unipi.client.model.Visita;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.function.Consumer;

public class AppointmentCard extends VBox {

    private Visita visita;
    
    @FXML private Label doctorNameLabel;
    @FXML private Label specializationLabel;
    @FXML private Label statusBadge;
    @FXML private Label dateTimeLabel;
    @FXML private Label typeLabel;
    @FXML private Button actionButton;
    
    private Consumer<Visita> annullaPrenotazioneClicked;

    public AppointmentCard(Visita visita) {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("appointmentCard.fxml"));
        loader.setRoot(this);
        loader.setController(this);

        try {
            loader.load();
        } catch (IOException e) {
            throw new RuntimeException("Impossibile caricare appointmentCard.fxml", e);
        }
        
        this.visita = visita;
        
        doctorNameLabel.setText("Dott. " + visita.getMedico().getNome() + " " + visita.getMedico().getCognome());
        specializationLabel.setText(visita.getMedico().getSpecializzazione());
        
        LocalDateTime dateTime = LocalDateTime.of(visita.getData(), visita.getOra());
        dateTimeLabel.setText(visita.getData().toString() + " " + visita.getOra().toString());
        typeLabel.setText(visita.getTipo());
        
        if(dateTime.isBefore(LocalDateTime.now())) {
            statusBadge.getStyleClass().add("status-completed");
            statusBadge.setText("Completato");
            actionButton.setVisible(false);
        }else {
            statusBadge.getStyleClass().add("status-scheduled");
            statusBadge.setText("Da Fare");
            actionButton.setText("Annulla");
        }
        
    }

    public void setAnnullaPrenotazioneClicked(Consumer<Visita> annullaPrenotazioneClicked) {
        this.annullaPrenotazioneClicked = annullaPrenotazioneClicked;
    }

    
    @FXML
    private void annullaPrenotazione() {
        
        if(annullaPrenotazioneClicked != null) annullaPrenotazioneClicked.accept(this.visita); 
    }
}