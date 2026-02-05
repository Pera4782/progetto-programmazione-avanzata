package it.unipi.client;

import it.unipi.client.model.requests.LoginRequest;
import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.responses.LoginResponse;
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
    
    public static int loggedMatricola;
    
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

    private void showMessage(String msg){
        message.setText(msg);
        message.setVisible(true);
    }
    
    private void hideMessage(){
        message.setVisible(false);
    }
    
    @FXML
    void initialize(){
        isDoctor = false;
        loggedMatricola = 0;
    }
    
    /**
     * @brief funzione associata all'evento della pressione del bottone accedi
     */
    @FXML
    void handleLogin() {
        
        hideMessage();
        
        //controlli degli input
        if(matricolaField.getText().isEmpty() || passwordField.getText().isEmpty()){
            showMessage("Compilare tutti i campi richiesti!");
            return;
        }
        
        int matricola;
        try {
            matricola = Integer.parseInt(matricolaField.getText());
        } catch (NumberFormatException e) {
            showMessage("La matricola deve essere un numero!");
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
                            showMessage("Errore nella comunicazione con il server riprovare più tardi.");
                            loginButton.setDisable(false);
                            return;
                        }
                        
                        if (result.getStatus() == null) {
                             showMessage("Risposta non valida dal server.");
                             loginButton.setDisable(false);
                             return;
                        }

                        switch(result.getStatus()){
                            
                            case MATRICOLANOTFOUND:
                                loginButton.setDisable(false);
                                showMessage("Matricola non esistente!");
                                return;
                            
                            case WRONGPASSWORD:
                                loginButton.setDisable(false);
                                showMessage("La password inserita non è corretta!");
                                return;
                              
                            case SUCCESS:
                                try{
                                    loggedMatricola = matricola; 
                                    if(isDoctor) App.setRoot("doctorsMenu");
                                    else App.setRoot("patientsMenu");
                                }catch(IOException e){
                                    e.printStackTrace();
                                    // Don't exit, just show error
                                    showMessage("Errore caricamento menu.");
                                    loginButton.setDisable(false);
                                }
                                break;
                            default:
                                showMessage("Errore sconosciuto.");
                                loginButton.setDisable(false);
                        }
                        
                    });  
                }catch(Exception e){
                    e.printStackTrace();
                    Platform.runLater(() -> {
                        showMessage("Errore durante il login: " + e.getMessage());
                        loginButton.setDisable(false);
                    });
                }
                
                return null;
            }
        };
        
        new Thread(task).start();
        
    }


    @FXML
    void handleInitialize() {
        System.out.println("Bottone inizializza premuto");
        //TODO
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
