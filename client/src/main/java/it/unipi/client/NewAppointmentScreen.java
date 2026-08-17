package it.unipi.client;

import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.Visita;
import it.unipi.client.model.requests.CreateVisitaRequest;
import it.unipi.client.model.responses.GetVisiteByMedicoResponse;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;

public class NewAppointmentScreen extends VBox {

    @FXML private TextField typeField;
    @FXML private DatePicker dateField;
    @FXML private ComboBox<String> timeField;

    @FXML private Label message;
    
    @FXML private Button createAppointmentButton;
    
    @FXML private RadioButton optionOrdinaria;
    @FXML private RadioButton optionSpeciale;
    
    private void showMessage(String msg){
        message.setText(msg);
        message.setVisible(true);
    }
    
    private void hideMessage(){
        message.setVisible(false);
    }
    
    public NewAppointmentScreen() {
    
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newAppointmentScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Errore nel caricamento del file FXML", exception);
        }
    }
    
    @FXML
    public void initialize(){
        dateField.setDisable(true);
    }
    
    
    @FXML
    public void tipoPrenotazioneChanged(ActionEvent event){
        
        RadioButton selectedRatio = (RadioButton) event.getSource();
        
        boolean disabled = selectedRatio == optionOrdinaria;
        
        dateField.setDisable(disabled);
    }
    
    private boolean checkValidity(LocalDate date, LocalTime time) throws Exception{
        
        GetVisiteByMedicoResponse response = RequestHandler.GETRequest("visita/medico", GetVisiteByMedicoResponse.class, 
                                                                        Integer.toString(DoctorsMenuController.loggedMedico.getMatricola()));
        
        if(response == null || response.getStatus() == GetVisiteByMedicoResponse.Status.ERROR) throw new Exception();
        
        if(response.getVisite() == null) return true;
        
        Visita[] visite = response.getVisite();
        for(Visita visita:visite)
            if(visita.getData().equals(date) && visita.getOra().equals(time)) return false;
        
        return true;
    }
    
    @FXML
    public void createAppointment(){
        
        hideMessage();
        
        LocalDate date = dateField.getValue();
        String timeString = timeField.getValue();
        String type = typeField.getText();
        boolean ordinaria = optionOrdinaria.isSelected();
        
        if((date == null && !ordinaria) || timeString == null || type == null || type.isEmpty() || timeString.isEmpty()){
            showMessage("Compilare tutti i campi richiesti!");
            return;
        }
        
        LocalTime time = LocalTime.parse(timeString);
        
        createAppointmentButton.setDisable(true);
        
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call(){
                
                try{
                    
                    boolean valid = checkValidity(date, time);
                    
                    if(!valid){
                        Platform.runLater(() -> {
                            showMessage("Appuntamento già esistente per quella data e ora");
                            createAppointmentButton.setDisable(false);
                        });
                        return null;
                    }
                    
                    CreateVisitaRequest createVisitaRequest = new CreateVisitaRequest(date, time, type, DoctorsMenuController.loggedMedico, ordinaria);
                    Boolean response = RequestHandler.POSTRequest("visita/crea", createVisitaRequest, Boolean.class);
                    
                    if(response == null || response == false){
                        Platform.runLater(() -> {
                            showMessage("Errore nella comunicazione con il server");
                            createAppointmentButton.setDisable(false);
                        });
                        return null;
                    }
                    
                    Platform.runLater(() -> {
                        createAppointmentButton.setDisable(false);
                    });
                    
                }catch(Exception e){
                    Platform.runLater(() -> {
                        showMessage("Errore nella creazione della visita");
                        createAppointmentButton.setDisable(false);
                    });
                    return null;
                }
                return null;
            }
        };
        
        new Thread(task).start();
    }
    
}