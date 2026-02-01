package it.unipi.client;

import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Label;


public class DoctorsMenuController {
    
    @FXML
    private Label doctorNameLabel;
    
    @FXML
    void initialize(){
        
        try{
            
            RequestHandler rh = new RequestHandler();
            String[] nomeCognome = rh.GETRequest("get/nomeCognomeDottore", String[].class, Integer.toString(LoginController.loggedMatricola));
            
            if(nomeCognome == null){
                
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
            
            doctorNameLabel.setText("Dr. " + nomeCognome[0] + " " + nomeCognome[1]);
            
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
    
}
