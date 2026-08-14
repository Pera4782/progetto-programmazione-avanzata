package it.unipi.client;

import it.unipi.client.model.Medico;
import it.unipi.client.model.RequestHandler;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;


public class DoctorsMenuController {
    
    @FXML
    private VBox mainContainer;
    
    private BookedAppointmentScreen bookedAppointmentScreen = new BookedAppointmentScreen();
    private NewAppointmentScreen newAppointmentScreen = new NewAppointmentScreen();
    private ShowPatientsScreen showPatientsScreen = new ShowPatientsScreen();
   
    
    @FXML
    private Label doctorNameLabel;
    @FXML
    private Label pageTitle;
    
    @FXML
    private Button navButton0;
    @FXML
    private Button navButton1;
    @FXML
    private Button navButton2;
    
    private Button[] navButtons;
    private int activeNavButton = 0;
    
    
    public static Medico loggedMedico;
    
    
    @FXML
    void initialize(){
        
        //riempimento del nome del dottore
        try{
            Medico medico = RequestHandler.GETRequest("medico/info", Medico.class, Integer.toString(LoginController.loggedMatricola));
            
            if(medico == null){
                
                Platform.runLater(() -> {
                    try{
                        App.setRoot("login");
                    }catch(Exception e){
                        e.printStackTrace();
                        System.exit(1);
                    }
                });
                return;
            }
            
            loggedMedico = medico;
            
            doctorNameLabel.setText("Dr. " + medico.getNome() + " " + medico.getCognome());
            
            mainContainer.getChildren().setAll(bookedAppointmentScreen);
            
            //inizializzazione dei bottoni di navigazione
            navButton0.setUserData(0);
            navButton1.setUserData(1);
            navButton2.setUserData(2);
            
            activeNavButton = 0;
            
            navButtons = new Button[] {navButton0, navButton1, navButton2};
            
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    @FXML
    void logout(){
        try{
            App.setRoot("login");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        } 
    }
    
    
    
    /**
     * @brief funzione associata all'evento di click sui bottoni di navigazione
     * @param e evento
     */
    @FXML
    void switchScreen(ActionEvent e){
        Button clicked = (Button) e.getSource();
        int index = (Integer) clicked.getUserData();
        
        if(index == activeNavButton) return;
        mainContainer.getChildren().clear();
        
        navButtons[activeNavButton].getStyleClass().remove("nav-button-active");
        clicked.getStyleClass().add("nav-button-active");
        
        activeNavButton = index;
        
        switch(activeNavButton){
            
            case 0: 
                pageTitle.setText("Prossimi Appuntamenti");
                mainContainer.getChildren().setAll(bookedAppointmentScreen);
                break;
            case 1:
                pageTitle.setText("Nuovo Appuntamento");
                mainContainer.getChildren().setAll(newAppointmentScreen);
                break;
            case 2:
                pageTitle.setText("Pazienti Visitati");
                mainContainer.getChildren().setAll(showPatientsScreen);
                break;
            
        }
        
    }
    
    @FXML
    void reload(){
        try{
            App.setRoot("doctorsMenu");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
