package it.unipi.client;

import it.unipi.client.model.Medico;
import it.unipi.client.model.Paziente;
import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.responses.FindDottoriResponse;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PatientsMenuController {

    private static Paziente loggedPaziente;
    
    @FXML private Label patientNameLable;
    
    @FXML private TextField doctorSearchBar;
    @FXML private ComboBox<String> specializationComboBox;
    @FXML private Button searchButton;

    @FXML
    private FlowPane doctorsList;
    
    
    @FXML
    public void initialize(){
        
        try{
            
            Paziente paziente = RequestHandler.GETRequest("paziente/info", Paziente.class, Integer.toString(LoginController.loggedMatricola));
            
            if(paziente == null){
                Platform.runLater(() -> {
                    try{
                        App.setRoot("login");
                    }catch(Exception e){
                        e.printStackTrace();
                        System.exit(1);
                    }
                });
            }
            
            loggedPaziente = paziente;
            
            patientNameLable.setText("Bentornato, " + loggedPaziente.getNome() + "!");
            
            specializationComboBox.getItems().addAll(
               "Qualsiasi",
               "Cardiologia", 
               "Dermatologia", 
               "Ginecologia", 
               "Medicina Generale", 
               "Neurologia", 
               "Ortopedia", 
               "Pediatria", 
               "Psichiatria"
            );
            
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        
        
        
    }
    
    
    @FXML
    public void showBookedAppointments(ActionEvent event) {
        System.out.println("Show booked appointments");
    }
    
    
    @FXML
    public void searchDoctors(){
        
        String cognomeMedico = doctorSearchBar.getText();
        String specializzazione = specializationComboBox.getValue();
        
        
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call(){
                
                
                try{

                    doctorsList.getChildren().clear();
                    
                    FindDottoriResponse response = RequestHandler.GETRequest("medico/find", FindDottoriResponse.class, cognomeMedico, specializzazione);
                    if(response.getStatus() == FindDottoriResponse.Status.ERROR) throw new Exception();

                    Medico[] medici = response.getMedici();
                    
                    for(Medico medico: medici){
                        DoctorCard dc = new DoctorCard(medico.getNome(), medico.getCognome(), medico.getSpecializzazione());
                        doctorsList.getChildren().add(dc);
                    }

                }catch(Exception e){

                }
                
                return null;
            }
            
        };
        
        
        
    }
    
}
