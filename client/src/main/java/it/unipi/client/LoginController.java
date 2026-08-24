package it.unipi.client;

import it.unipi.client.model.requests.LoginRequest;
import it.unipi.client.util.RequestHandler;
import it.unipi.client.session.UserSession;
import it.unipi.client.model.responses.LoginResponse;
import it.unipi.client.util.MessageHandler;
import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

public class LoginController {

    
    public static boolean isDoctor;
    
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
    private Label message;
    
    private MessageHandler messageHandler;
    
    @FXML
    void initialize(){
        isDoctor = false;
        messageHandler = new MessageHandler(message, null, "error");
    }
    
    /**
     * @brief funzione associata all'evento della pressione del bottone accedi
     */
    @FXML
    void handleLogin() {
        
        messageHandler.hideMessage();
        
        //controlli degli input
        if(matricolaField.getText().isEmpty() || passwordField.getText().isEmpty()){
            messageHandler.showMessage("Compilare tutti i campi richiesti", true);
            return;
        }
        
        int matricola;
        try {
            matricola = Integer.parseInt(matricolaField.getText());
        } catch (NumberFormatException e) {
            messageHandler.showMessage("La matricola deve essere un numero!", true);
            return;
        }

        String password = passwordField.getText();
        
        if(doctorRadio.isSelected()) isDoctor = true;
        else isDoctor = false; 
        
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call() {
                
                try{
                    
                    LoginRequest lr = new LoginRequest(matricola, password, isDoctor);
                    
                    LoginResponse result = RequestHandler.POSTRequest("account/login", lr, LoginResponse.class);
                    
                    Platform.runLater(() -> {
                    
                        if(result == null) {
                            messageHandler.showMessage("Errore nella comunicazione con il server", true);
                            loginButton.setDisable(false);
                            return;
                        }
                        
                        if (result.getStatus() == null) {
                             messageHandler.showMessage("Risposta non valida dal server", true);
                             loginButton.setDisable(false);
                             return;
                        }

                        switch(result.getStatus()){
                            
                            case MATRICOLANOTFOUND:
                                loginButton.setDisable(false);
                                messageHandler.showMessage("Matricola non esistente", true);
                                return;
                            
                            case WRONGPASSWORD:
                                loginButton.setDisable(false);
                                messageHandler.showMessage("Password errata", true);
                                return;
                              
                            case SUCCESS:
                                try{
                                    UserSession session = UserSession.getSession();
                                    session.setLoggedMatricola(matricola);
                                    
                                    if(isDoctor) App.setRoot("doctorsMenu");
                                    else App.setRoot("patientsMenu");
                                }catch(IOException e){
                                    e.printStackTrace();
                                    // Don't exit, just show error
                                    messageHandler.showMessage("Errore nel caricamento del menu", true);
                                    loginButton.setDisable(false);
                                }
                                break;
                            default:
                                messageHandler.showMessage("Errore sconosciuto", true);
                                loginButton.setDisable(false);
                        }
                        
                    });  
                }catch(Exception e){
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        messageHandler.showMessage("Errore durante il login", true);
                        loginButton.setDisable(false);
                    });
                }
                
                return null;
            }
        };
        
        new Thread(task).start();
        
    }


    @FXML
    void inizializzaDB() {
        
    }

    /**
     * @brief funzione associata alla pressione del bottone registrati
     */
    @FXML
    void handleRegister() {
        try {

            if(doctorRadio.isSelected()) isDoctor = true;
            else if(patientRadio.isSelected()) isDoctor = false;
            
            App.setRoot("register");
            
        } catch (IOException e) {
            System.err.println("Errore nel caricamento della pagina di registrazione: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
