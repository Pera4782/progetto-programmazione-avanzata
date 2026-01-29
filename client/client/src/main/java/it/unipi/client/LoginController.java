package it.unipi.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;

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
    private ToggleGroup roleGroup;

    @FXML
    private Button loginButton;

    
    @FXML
    void handleLogin(ActionEvent event) {
        System.out.println("Bottone accedi premuto");
    }


    @FXML
    void handleInitialize(ActionEvent event) {
        System.out.println("Bottone inizializza premuto");
    }


    @FXML
    void handleRegister(ActionEvent event) {
        try {
            App.setRoot("register");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento della pagina di registrazione: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
