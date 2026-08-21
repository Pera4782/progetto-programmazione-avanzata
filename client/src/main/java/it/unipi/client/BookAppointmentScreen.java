package it.unipi.client;

import it.unipi.client.model.Medico;
import it.unipi.client.model.Paziente;
import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.UserSession;
import it.unipi.client.model.Visita;
import it.unipi.client.model.requests.BookAppointmentRequest;
import it.unipi.client.model.responses.GetVisiteResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
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
    
    
    /**
     * @brief funzione per la rimozione degli appuntamenti già prenotati
     * @param unfilteredVisits array di visite
     * @return ArrayList di visite non prenotate
     */
    private ArrayList<Visita> removeBookedAppointments(Visita[] unfilteredVisits){
    
        ArrayList<Visita> visite = new ArrayList<>(Arrays.asList(unfilteredVisits));
        HashMap<LocalTime, Integer> timeMap = new HashMap<>();

        for(int i = 0; i < visite.size(); ++i){
            if(timeMap.containsKey(visite.get(i).getOra())) 
                timeMap.put(visite.get(i).getOra(), timeMap.get(visite.get(i).getOra()) + 1);
            else
                timeMap.put(visite.get(i).getOra(), 1);
        }

        visite.removeIf(v -> {
            if(timeMap.get(v.getOra()) > 1 || v.getPaziente() != null) return true;
            return false;
        });
                    
        return visite;
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

    /**
     * @brief funzione chiamata quando viene selezionato un orario nel date picker
     */
    @FXML
    public void showAvailableTimes(){
        
        timeSlotsList.getItems().clear();
        
        LocalDate date = appointmentDatePicker.getValue();
        if(date.isBefore(LocalDate.now())) return;
        appointmentDatePicker.setDisable(true);
        
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call(){
                
                try{
                    
                    GetVisiteResponse response = RequestHandler.GETRequest("visita/data", GetVisiteResponse.class, date.toString());
                    
                    if(response.getStatus() == GetVisiteResponse.Status.ERROR) throw new Exception();
                    
                    ArrayList<Visita> visite = removeBookedAppointments(response.getVisite());
                    
                    ArrayList<String> orari = new ArrayList<>(visite.size());
                    visite.forEach(v -> orari.add(v.getOra().toString() + " " + v.getTipo()));
                    
                    if (visite.isEmpty()) {
                        Platform.runLater(() -> {appointmentDatePicker.setDisable(false);});
                        return null;
                    }
                    
                    Platform.runLater(() -> timeSlotsList.getItems().addAll(orari));
                    
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
    
    
    @FXML
    private void confirmBooking() {
        
        hideMessage();
        
        LocalDate selectedDate = appointmentDatePicker.getValue();
        String selectedSlot = timeSlotsList.getSelectionModel().getSelectedItem();
        
        if(selectedDate == null || selectedSlot == null){
            showMessage("Selezionare data e ora", true);
            return;
        }
        
        LocalTime selectedTime = LocalTime.parse(selectedSlot.split(" ")[0]);
        
        Task<Void> task = new Task<Void>() {
            @Override
            protected Void call() {
                
                try{
                    BookAppointmentRequest body = new BookAppointmentRequest(selectedDate, selectedTime, 
                                                                            (Paziente) UserSession.getSession().getLoggedUtente());
                    Integer response = RequestHandler.POSTRequest("visita/prenota", body, Integer.class);
                
                    if(response == null) throw new Exception();
                    
                    
                    Platform.runLater(() -> {
                        showMessage("Appuntamento prenotato con successo", false);
                    });
                    
                    
                }catch(Exception e){
                    Platform.runLater(() -> {
                        showMessage("Errore nella comunicazione con il server", true);
                    });
                    return null;
                }
                
                return null;
            }
        };
        
        new Thread(task).start();
        
    }
}
