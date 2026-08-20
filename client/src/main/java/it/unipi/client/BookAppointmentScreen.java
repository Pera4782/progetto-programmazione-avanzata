package it.unipi.client;

import it.unipi.client.model.Medico;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;

public class BookAppointmentScreen extends VBox {

    private Medico medico;
    
    @FXML private Label avatarText;
    @FXML private Label nameLabel;
    @FXML private Label specializationLabel;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ListView<String> timeSlotsList;
    
    private Runnable goBackButtonClicked;

    public BookAppointmentScreen(Medico medico) {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bookAppointmentScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Errore durante il caricamento del file FXML bookAppointmentScreen.fxml", exception);
        }
        
        this.medico = medico;
        
        String inizialeNome = medico.getNome().substring(0,1).toUpperCase();
        String inizialeCognome = medico.getCognome().substring(0,1).toUpperCase();
        
        avatarText.setText(inizialeNome + inizialeCognome);
        nameLabel.setText("Dr. " + medico.getNome() + " " + medico.getCognome());
        specializationLabel.setText(medico.getSpecializzazione());
        
    }

    public void setGoBackButtonClicked(Runnable goBackButtonClicked) {
        this.goBackButtonClicked = goBackButtonClicked;
    }
    
    @FXML
    private void goBack() {
        if(goBackButtonClicked != null) goBackButtonClicked.run();
    }

    @FXML
    private void confirmBooking() {
        LocalDate selectedDate = appointmentDatePicker.getValue();
        String selectedTime = timeSlotsList.getSelectionModel().getSelectedItem();
        
    }

    
    @FXML
    public void showAvailableTimes(){
        System.out.println("DATA SELEZIONATA");
    }
    
    public void setDoctorDetails(String name, String specialization, String initials) {
        
    }

    
}
