package it.unipi.client;

import java.io.IOException;
import javafx.concurrent.Task;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class LoginController {

    @FXML
    private TextField matricolaField;
    
    @FXML
    private PasswordField passwordField;

    
    @FXML
    private RadioButton doctorRadio;
    
    @FXML
    private RadioButton patientRadio;

    @FXML
    private Button loginButton;

    
    @FXML
    void handleLogin(ActionEvent event) {
        
        
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call() {
                
                try{
                    
                    RequestHandler handler = new RequestHandler();
                    Paziente p = handler.GETRequest("register/paziente", Paziente.class, "ciao");

                    System.out.println(p.getMatricola());
                
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
    void handleInitialize(ActionEvent event) {
        System.out.println("Bottone inizializza premuto");
    }


    @FXML
    void handleRegister(ActionEvent event) {
        try {

            if(doctorRadio.isSelected()) App.setRoot("doctorRegister");
            else if(patientRadio.isSelected()) App.setRoot("patientRegister");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento della pagina di registrazione: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
