package it.unipi.client;

import it.unipi.client.model.Medico;
import it.unipi.client.model.Paziente;
import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.UserSession;
import it.unipi.client.model.responses.FindDottoriResponse;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.FlowPane;
import javafx.event.ActionEvent;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Label;

public class PatientsMenuController {
    
    @FXML private Label patientNameLable;
    
    @FXML private TextField doctorSearchBar;
    @FXML private ComboBox<String> specializationComboBox;
    @FXML private Button searchButton;

    @FXML
    private FlowPane mainContainer;
    
    private DoctorCard[] doctorCards;
    
    @FXML
    public void initialize(){
        
        try{
            
            Paziente paziente = RequestHandler.GETRequest("paziente/info", Paziente.class, Integer.toString(UserSession.getSession().getLoggedMatricola()));
            
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
            
            UserSession.getSession().setLoggedUtente(paziente);
            
            patientNameLable.setText("Bentornato, " + UserSession.getSession().getLoggedUtente().getNome() + "!");
            
            specializationComboBox.getItems().addAll(
               "Qualsiasi",
               "Cardiologia", 
               "Dermatologia", 
               "Ginecologia", 
               "Neurologia", 
               "Ortopedia", 
               "Pediatria", 
               "Psichiatria"
            );
            
            specializationComboBox.setValue("Qualsiasi");
            
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        
        
        
    }
    
    
    @FXML
    public void showBookedAppointments(ActionEvent event) {
        
        try{
            App.setRoot("showBookedAppointmentsMenu");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        
    }
    
    
    /**
     * @brief funzione per la ricerca di medici nella pagina principale del paziente
     */
    @FXML
    public void searchDoctors(){
        
        String cognomeMedico = doctorSearchBar.getText();
        String specializzazione = specializationComboBox.getValue();
        
        if(cognomeMedico.isEmpty() && specializzazione == null) return;
        
        mainContainer.getChildren().clear();
        
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call(){
                try{
                    
                    FindDottoriResponse response = RequestHandler.GETRequest("medico/find", FindDottoriResponse.class, cognomeMedico, specializzazione);
                    if(response.getStatus() == FindDottoriResponse.Status.ERROR) throw new Exception();

                    Medico[] medici = response.getMedici();
                    
                    doctorCards = new DoctorCard[medici.length];
                    
                    for(int i = 0; i < medici.length; ++i){
                        DoctorCard dc = new DoctorCard(medici[i]);
                        doctorCards[i] = dc;
                        
                        dc.setBookButtonClicked(() -> {
                            
                            searchButton.setDisable(true);
                            mainContainer.getChildren().clear();
                            mainContainer.setAlignment(Pos.CENTER);
                            
                            BookAppointmentScreen bas = new BookAppointmentScreen(dc.getMedico());
                            
                            bas.setGoBackButtonClicked(() -> {
                                mainContainer.getChildren().clear();
                                mainContainer.setAlignment(Pos.TOP_LEFT);
                                searchButton.setDisable(false);
                            });
                            mainContainer.getChildren().add(bas);
                        });
                    }

                    Platform.runLater(() -> {
                        mainContainer.getChildren().addAll(doctorCards);
                    });
                    
                }catch(Exception e){
                    e.printStackTrace();
                    System.exit(1);
                }
                
                return null;
            }
            
        };
        
        new Thread(task).start();
        
    }
 
    @FXML
    public void logout(){
        
        try{
            UserSession.getSession().clearSession();
            App.setRoot("login");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
        
    }
    
}
