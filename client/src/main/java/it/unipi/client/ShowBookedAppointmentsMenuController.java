package it.unipi.client;

import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.UserSession;
import it.unipi.client.model.Visita;
import it.unipi.client.model.responses.GetVisiteResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;

public class ShowBookedAppointmentsMenuController {

    @FXML private ComboBox<String> statusFilterComboBox;
    @FXML private FlowPane appointmentsContainer;
    @FXML private Button filterButton;
    
    @FXML private Label statusRichiestaLabel;
    
    private void showErrorMessage(String msg){
        
        hideErrorMessage();
        statusRichiestaLabel.setText(msg);
        statusRichiestaLabel.setVisible(true);
    }
    
    private void hideErrorMessage(){
        statusRichiestaLabel.setText("");
        statusRichiestaLabel.setVisible(false);
    }

    @FXML
    public void initialize() {
        statusFilterComboBox.getItems().addAll("Tutti", "Programmato", "Completato");
        statusFilterComboBox.getSelectionModel().selectFirst();
    }

    
    /**
     * @brief callback da passare alla card degli appuntamenti
     * @param visita visita da annullare
     */
    private void annullaPrenotazione(Visita visita){
    
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call(){
            
                try{
                    
                    Integer response = RequestHandler.POSTRequest("visita/annulla/prenotazione", visita, Integer.class);
                    
                    if(response == null) throw new Exception();
                    
                    Platform.runLater(() -> {appointmentsContainer.getChildren().clear();});
                    
                }catch(Exception e){
                    Platform.runLater(() -> {showErrorMessage("Errore nella comunicazione con il server");});
                    return null;
                }
                
                return null;
            }
        };
        
        new Thread(task).start();
        
    }
    
    /**
     * @brief funzione per il filtraggio delle visite
     * @param visite visite da filtrare
     * @param selectedStatus status per cui filtrare
     * @return array di visite filtrate
     */
    private static Visita[] selectAppointments(Visita[] visite, String selectedStatus){
        
        if(selectedStatus.equals("Tutti")) return visite;
        
        ArrayList<Visita> appointments = new ArrayList<>(Arrays.asList(visite));
        
        if(selectedStatus.equals("Programmato")) 
            appointments.removeIf(v -> LocalDateTime.of(v.getData(), v.getOra()).isBefore(LocalDateTime.now()));
        else
            appointments.removeIf(v -> LocalDateTime.of(v.getData(), v.getOra()).isAfter(LocalDateTime.now()));
        
        return appointments.toArray(new Visita[0]);
    }
    
    
    /**
     * @brief funzione di ricerca degli appuntamenti
     */
    @FXML
    private void filterAppointments() {
        String selectedStatus = statusFilterComboBox.getValue();
        if (selectedStatus == null) return;
        
        appointmentsContainer.getChildren().clear();
        filterButton.setDisable(true);
        hideErrorMessage();
        
        Task<Void> task = new Task<Void>(){
            
            @Override
            public Void call(){
                
                try{
                
                    GetVisiteResponse response = RequestHandler.GETRequest("visita/paziente", GetVisiteResponse.class, 
                                                                           Integer.toString(UserSession.getSession().getLoggedUtente().getMatricola()));
                    
                    if(response == null || response.getStatus() == GetVisiteResponse.Status.ERROR) throw new Exception();
                    
                    Visita[] visite = response.getVisite();
                    
                    Visita[] filteredAppointments = selectAppointments(visite, selectedStatus);
                    
                    Platform.runLater(() -> {
                        
                        AppointmentCard[] ac = new AppointmentCard[filteredAppointments.length];
                        for(int i = 0; i < filteredAppointments.length; ++i) {
                            ac[i] = new AppointmentCard(filteredAppointments[i]);
                            ac[i].setAnnullaPrenotazioneClicked(v -> annullaPrenotazione(v));
                        }
                        appointmentsContainer.getChildren().addAll(ac);
                        filterButton.setDisable(false);
                    });
                    
                }catch(Exception e){
                    Platform.runLater(() -> {
                        showErrorMessage("Errore nella comunicazione con il server");
                    });
                    return  null;
                }
                return null;
            }
        
        };
        
        new Thread(task).start();
    }


    @FXML
    private void backToPatientMenu() {
        
        try{
            App.setRoot("patientsMenu");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }

    
    @FXML
    private void logout() {
        try{
            UserSession.getSession().clearSession();
            App.setRoot("login");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
}