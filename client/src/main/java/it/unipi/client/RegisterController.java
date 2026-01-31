package it.unipi.client;

import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;

public class RegisterController {

    @FXML
    private Label title;
    
    @FXML
    private TextField nameField;

    @FXML
    private TextField surnameField;

    @FXML
    private Label specializationLabel;
    
    @FXML
    private ComboBox<String> specializationComboBox;

    @FXML
    private PasswordField passwordField;

    @FXML
    private PasswordField confirmPasswordField;
    
    @FXML
    private Button registerButton;

    @FXML
    private HBox accediButton;
    
    @FXML
    private Label message;

    @FXML
    private Button hoCapitoButton;

    private void showMessage(String msg){
        message.setText(msg);
        message.setVisible(true);
    }
    
    private void hideMessage(){
        message.setVisible(false);
    }

    @FXML
    public void initialize(){
        
        if(!LoginController.isDoctor){
            specializationLabel.setVisible(false);
            specializationLabel.setManaged(false);
            specializationComboBox.setVisible(false);
            specializationComboBox.setManaged(false);
            title.setText("Creazione Account Paziente");
        } else {
            specializationComboBox.getItems().addAll(
               "Cardiologia", 
               "Dermatologia", 
               "Ginecologia", 
               "Medicina Generale", 
               "Neurologia", 
               "Ortopedia", 
               "Pediatria", 
               "Psichiatria"
            );
        }
        
    }

    @FXML
    void handleRegister(ActionEvent event) {
        
        hideMessage();
        if(hoCapitoButton != null) hoCapitoButton.setVisible(false);

        String nome = nameField.getText();
        String cognome = surnameField.getText();
        String specializzazione = specializationComboBox.getValue();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if(nome.isEmpty() || cognome.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
           || ((specializzazione == null || specializzazione.isEmpty()) && LoginController.isDoctor)){
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
                    
                    Utente utente;
                    String endpoint;
                    
                    if(LoginController.isDoctor){
                        utente = new Medico(0, password, nome, cognome, specializzazione);
                        endpoint = "medico";
                    }
                    else {
                        utente = new Paziente(0, password, nome, cognome);
                        endpoint = "paziente";
                    }
                    
                    RequestHandler rh = new RequestHandler();
                  
                    Integer result = rh.POSTRequest("account/register/" + endpoint, utente, Integer.class);
                    
                    Platform.runLater(() -> {
                        
                        if (result == null) {
                            showMessage("Errore nella comunicazione con il server riprovare più tardi.");
                            registerButton.setDisable(false);
                            return;
                        }

                        switch(result){
                            case -1:
                                showMessage("Compilare tutti i campi!");
                                registerButton.setDisable(false);
                                break;
                            case -2:
                                showMessage("La password deve essere lunga almeno 8 caratteri, deve contenere solo caratteri alfanumerici e almeno una lettera maiuscola, un numero e uno trai seguenti caratteri: ! @ .");
                                registerButton.setDisable(false);
                                break;
                            default:
                                if(accediButton != null) accediButton.setVisible(false);
                                if(hoCapitoButton != null) hoCapitoButton.setVisible(true);
                                showMessage("IMPORTANTE!!! La tua matricola è " + result + " assicurati di non perderla, una volta uscito da questa pagina non sarà più possibile recuperarla");
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