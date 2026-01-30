package it.unipi.client;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class PatientRegisterController {

    
    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private HBox accediButton;
    
    @FXML
    private Label errorMessage;

    @FXML
    private Button hoCapitoButton;
    
    @FXML
    private Button registerButton;

    private void showMessage(String msg){
        errorMessage.setText(msg);
        errorMessage.setVisible(true);
    }
    
    private void hideMessage(){
        errorMessage.setVisible(false);
    }
    
    @FXML
    void handleRegister(ActionEvent event) {
        
        hideMessage();
        
        String nome = nameField.getText();
        String cognome = surnameField.getText();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if(nome.isEmpty() || cognome.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()){
            showMessage("Compilare tutti i campi!");
            return;
        }
        
        if(!password.equals(confirmPassword)){
            showMessage("Le password digitate non coincidono!");
            return;
        }
        
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@! .=])[A-Za-z\\d@! .=]{8,}$";
        
        if(!password.matches(passwordRegex)){
            showMessage("La password deve essere lunga almeno 8 caratteri, deve contenere solo caratteri alfanumerici e almeno una lettera maiuscola, un numero e uno trai seguenti caratteri: ! @ .");
            return;
        }
       
        registerButton.setDisable(true);
        
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call() {
                
                try{
                    
                    Paziente paziente = new Paziente(0, password, nome, cognome);
                    RequestHandler rh = new RequestHandler();
                  
                    int returnState = rh.POSTRequest("register/paziente", paziente, Integer.class);
                    
                    Platform.runLater(new Runnable(){
                    
                        @Override public void run() {
                            switch(returnState){
                        
                                case -1:
                                    showMessage("Compilare tutti i campi!");
                                    registerButton.setDisable(false);
                                    return;
                                case -2:
                                    showMessage("La password deve essere lunga almeno 8 caratteri, deve contenere solo caratteri alfanumerici e almeno una lettera maiuscola, un numero e uno trai seguenti caratteri: ! @ .");
                                    registerButton.setDisable(false);
                                    return;
                                default:
                                    accediButton.setVisible(false);
                                    showMessage("IMPORTANTE!!! La tua matricola è " + returnState + " assicurati di non perderla, una volta uscito da questa pagina non sarà più possibile recuperarla");
                                    hoCapitoButton.setVisible(true);
                            }
                        }
                    
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
    void switchToLoginPage(ActionEvent event) {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento della pagina di login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}