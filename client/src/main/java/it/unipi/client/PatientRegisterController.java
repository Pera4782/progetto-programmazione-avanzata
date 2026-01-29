package it.unipi.client;

import java.io.IOException;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

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
    private Button registerButton;

    @FXML
    void handleRegister(ActionEvent event) {
        System.out.println("Bottone registrati premuto");
        
    }

    @FXML
    void handleLogin(ActionEvent event) {
        try {
            App.setRoot("login");
        } catch (IOException e) {
            System.err.println("Errore nel caricamento della pagina di login: " + e.getMessage());
            e.printStackTrace();
        }
    }
}