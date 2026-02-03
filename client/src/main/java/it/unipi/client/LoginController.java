package it.unipi.client;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;

import javafx.event.ActionEvent;
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
        
        int matricola = Integer.parseInt(matricolaField.getText());
        String password = passwordField.getText();
        
        if(doctorRadio.isSelected()) isDoctor = true;
        
        loginButton.setDisable(true);
        
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call() {
                
                try{
                    
                    LoginRequest lr = new LoginRequest(matricola, password, isDoctor);
                    
                    Integer result = RequestHandler.POSTRequest("account/login", lr, Integer.class);
                    
                    Platform.runLater(() -> {
                    
                        if(result == null) {
                            showMessage("Errore nella comunicazione con il server riprovare più tardi.");
                            loginButton.setDisable(false);
                            return;
                        }
                    
                        switch(result){
                            
                            case 0:
                                loginButton.setDisable(false);
                                showMessage("Matricola non esistente!");
                                return;
                            
                            case 1:
                                loginButton.setDisable(false);
                                showMessage("La password inserita non è corretta!");
                                return;
                              
                            default:
                                try{
                                    loggedMatricola = matricola; 
                                    if(isDoctor) App.setRoot("doctorsMenu");
                                    else App.setRoot("patientsMenu");
                                }catch(IOException e){
                                    e.printStackTrace();
                                    System.exit(1);
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
