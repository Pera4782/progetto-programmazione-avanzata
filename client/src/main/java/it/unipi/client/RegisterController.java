package it.unipi.client;

import it.unipi.client.App;
import it.unipi.client.LoginController;
import it.unipi.client.util.RequestHandler;
import it.unipi.client.model.requests.RegisterRequest;
import it.unipi.client.model.responses.RegisterResponse;
import it.unipi.client.util.MessageHandler;
import java.io.IOException;
import javafx.application.Platform;
import javafx.concurrent.Task;
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

    private MessageHandler messageHandler;

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
               "Neurologia", 
               "Ortopedia", 
               "Pediatria", 
               "Psichiatria"
            );
        }
        
        messageHandler = new MessageHandler(message, "success", "error");
    }

    /**
     * @brief funzione associata alla pressione del tasto registrati
     */
    @FXML
    void handleRegister() {
        
        messageHandler.hideMessage();
        
        //controlli sui campi inseriti
        if(hoCapitoButton != null) hoCapitoButton.setVisible(false);

        String nome = nameField.getText();
        String cognome = surnameField.getText();
        String specializzazione = specializationComboBox.getValue();
        String password = passwordField.getText();
        String confirmPassword = confirmPasswordField.getText();
        
        if(nome.isEmpty() || cognome.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
           || ((specializzazione == null || specializzazione.isEmpty()) && LoginController.isDoctor)){
            messageHandler.showMessage("Compilare tutti i campi!", true);
            return;
        }
        
        if(!password.equals(confirmPassword)){
            messageHandler.showMessage("Le password digitate non coincidono!", true);
            return;
        }
        
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@! .=])[A-Za-z\\d@! .=]{8,}$";
        
        if(!password.matches(passwordRegex)){
            messageHandler.showMessage("La password deve essere lunga almeno 8 caratteri, deve contenere solo caratteri alfanumerici e almeno una lettera maiuscola, un numero e uno trai seguenti caratteri: ! @ .", true);
            return;
        }

        registerButton.setDisable(true);

        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call() {
                
                try{
                    
                    RegisterRequest rr;
                    
                    if(LoginController.isDoctor) rr = new RegisterRequest(nome, cognome, password, specializzazione, true);
                    else rr = new RegisterRequest(nome, cognome, password, null, false);
                    
                  
                    RegisterResponse result = RequestHandler.POSTRequest("account/register", rr, RegisterResponse.class);
                    
                    Platform.runLater(() -> {
                        
                        if (result == null) {
                            messageHandler.showMessage("Errore nella comunicazione con il server riprovare più tardi.", true);
                            registerButton.setDisable(false);
                            return;
                        }

                        switch(result.getStatus()){
                            case EMPTYFIELDS:
                                messageHandler.showMessage("Compilare tutti i campi!", true);
                                registerButton.setDisable(false);
                                break;
                            case WRONGPASSWORDFORMAT:
                                messageHandler.showMessage("La password deve essere lunga almeno 8 caratteri, deve contenere solo caratteri alfanumerici e almeno una lettera maiuscola, un numero e uno trai seguenti caratteri: ! @ .", true);
                                registerButton.setDisable(false);
                                break;
                            case SUCCESS:
                                if(accediButton != null) accediButton.setVisible(false);
                                if(hoCapitoButton != null) hoCapitoButton.setVisible(true);
                                messageHandler.showMessage("IMPORTANTE!!! La tua matricola è " + result.getMatricola() + " assicurati di non perderla, una volta uscito da questa pagina non sarà più possibile recuperarla", false);
                                break;
                            default:
                                System.exit(1);
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

    /**
     * @brief funzione associata alla pressione del bottone accedi
     */
    @FXML
    void switchToLoginPage() {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento della pagina di login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}