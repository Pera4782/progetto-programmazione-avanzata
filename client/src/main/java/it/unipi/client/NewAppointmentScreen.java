package it.unipi.client;

import it.unipi.client.model.Medico;
import it.unipi.client.util.RequestHandler;
import it.unipi.client.session.UserSession;
import it.unipi.client.model.Visita;
import it.unipi.client.model.requests.CreateVisitaRequest;
import it.unipi.client.model.responses.GetVisiteResponse;
import it.unipi.client.model.responses.Response;
import it.unipi.client.util.MessageHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import java.io.IOException;
import java.time.DayOfWeek;
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
    
    private MessageHandler messageHandler;
    
    public NewAppointmentScreen() {
    
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("newAppointmentScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException("Errore nel caricamento del file FXML", exception);
        }
        
        messageHandler = new MessageHandler(message, "success", "error");
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
    
    /**
     * @brief controlla la validita dei campi per la creazione delle visite
     * @param date data della visita null se è ordinaria   
     * @param time ora della visita
     * @param ordinaria se è ordinaria o meno
     * @return se i campi sono validi o meno
     * @throws Exception 
     */
    private boolean checkValidity(LocalDate date, LocalTime time, boolean ordinaria) throws Exception{
        
        GetVisiteResponse response = RequestHandler.GETRequest("visita/medico", GetVisiteResponse.class, 
                                                                        Integer.toString(UserSession.getSession().getLoggedMatricola()));
        
        if(response == null || response.getStatus() == GetVisiteResponse.Status.ERROR) throw new Exception();
        
        if(response.getVisite() == null) return true;
        
        Visita[] visite = response.getVisite();
        
        for(Visita visita: visite){
            
            if(visita.getData() != null && visita.getData().isBefore(LocalDate.now())) continue;
            
            
            if (ordinaria) {
                
                if(!visita.getOrdinaria() && (visita.getData().getDayOfWeek() == DayOfWeek.SATURDAY || visita.getData().getDayOfWeek() == DayOfWeek.SUNDAY))
                    continue;
                
                if (visita.getOra().equals(time)) return false;
            } else {
                if (visita.getOrdinaria() && 
                    date.getDayOfWeek() != DayOfWeek.SATURDAY && date.getDayOfWeek() != DayOfWeek.SUNDAY &&
                    visita.getOra().equals(time)) return false;
                
                if (!visita.getOrdinaria() && visita.getData() != null && visita.getData().equals(date) && visita.getOra().equals(time)) return false;
            }
        }
        return true;
    }
    
    @FXML
    public void createAppointment(){
        
        messageHandler.hideMessage();
        
        LocalDate date = dateField.getValue();
        String timeString = timeField.getValue();
        String type = typeField.getText();
        boolean ordinaria = optionOrdinaria.isSelected();
        
        if((date == null && !ordinaria) || timeString == null || type == null || type.isEmpty() || timeString.isEmpty()){
            messageHandler.showMessage("Compilare tutti i campi richiesti!", true);
            return;
        }
        
        LocalTime time = LocalTime.parse(timeString);
        
        createAppointmentButton.setDisable(true);
        
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call(){
                
                try{
                    
                    boolean valid = checkValidity(date, time, ordinaria);
                    
                    if(!valid){
                        Platform.runLater(() -> {
                            messageHandler.showMessage("Appuntamento già esistente per quell'ora", true);
                            createAppointmentButton.setDisable(false);
                        });
                        return null;
                    }
                    
                    CreateVisitaRequest createVisitaRequest = new CreateVisitaRequest((ordinaria)? null:date, time, type, 
                                                                                      (Medico) UserSession.getSession().getLoggedUtente(),
                                                                                       ordinaria);
                    Response response = RequestHandler.POSTRequest("visita/crea", createVisitaRequest, Response.class);
                    
                    if(response == null || response.isError()) throw new Exception();
                    
                    Platform.runLater(() -> {
                        createAppointmentButton.setDisable(false);
                        messageHandler.showMessage("Appuntamento creato con successo", false);
                    });
                    
                }catch(Exception e){
                    Platform.runLater(() -> {
                        messageHandler.showMessage("Errore nella creazione della visita", true);
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