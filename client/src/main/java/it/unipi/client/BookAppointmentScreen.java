package it.unipi.client;

import it.unipi.client.model.Medico;
import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.Visita;
import it.unipi.client.model.responses.GetVisiteResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import javafx.application.Platform;
import javafx.concurrent.Task;

public class BookAppointmentScreen extends VBox {

    private Medico medico;
    
    @FXML private Label avatarText;
    @FXML private Label nameLabel;
    @FXML private Label specializationLabel;
    @FXML private DatePicker appointmentDatePicker;
    @FXML private ListView<String> timeSlotsList;
    
    @FXML private Label statusLabel;
    
    private Runnable goBackButtonClicked;
    
    private void showMessage(String msg, boolean error){
        
        hideMessage();
        
        String styleClass = (error)? "error" : "success";
        statusLabel.getStyleClass().add(styleClass);
        statusLabel.setText(msg);
        statusLabel.setVisible(true);
    }
    
    private void hideMessage(){
        
        statusLabel.getStyleClass().removeAll("error", "success");
        statusLabel.setText("");
        statusLabel.setVisible(false);
    }
       
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
        
        LocalDate date = appointmentDatePicker.getValue();
        if(date.isBefore(LocalDate.now())) return;
        appointmentDatePicker.setDisable(true);
        
        timeSlotsList.getItems().clear();
        
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call(){
                
                try{
                    
                    GetVisiteResponse response = RequestHandler.GETRequest("visita/data", GetVisiteResponse.class, date.toString());
                    
                    if(response.getStatus() == GetVisiteResponse.Status.ERROR) throw new Exception();
                    
                    Visita[] visite = response.getVisite();
                    
                }catch(Exception e){
                    showMessage("Errore nella comunicazione con il server", true);
                    return null;
                }
                
                
                Platform.runLater(() -> {appointmentDatePicker.setDisable(false);});
                
                return null;
            }
        
        };
        
        
        new Thread(task).start();
    }
}
