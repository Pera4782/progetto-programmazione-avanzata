package it.unipi.client;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;


public class DoctorsMenuController {
    
    @FXML
    private VBox mainContainer;
    
    private List<Node> appointmentsScreen;
    
    @FXML
    private Label doctorNameLabel;
    
    @FXML
    private Button date0;
    @FXML
    private Button date1;
    @FXML
    private Button date2;
    @FXML
    private Button date3;
    @FXML
    private Button date4;
    
    private static Button[] dateButtons;
    private static int activeDateButton = -1;
    private static LocalDate nextButtonDate;
    private static final LocalDate today = LocalDate.now();
    
    @FXML
    private Button navButton0;
    @FXML
    private Button navButton1;
    @FXML
    private Button navButton2;
    
    private static Button[] navButtons;
    private static int activeNavButton = 0;
    
    @FXML
    private TableView appointmentsTable;
    
    @FXML
    private Button nextDatesButton;
    
    private static void printNextDates(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM");
            
        for(int i = 0; i < 5; ++i){
            dateButtons[i].setText(nextButtonDate.format(format));
            nextButtonDate = nextButtonDate.plusDays(1);
        }
    }
    
    @FXML
    void initialize(){
        
        //riempimento del nome del dottore
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
            
            //salvataggio della schermata degli appuntamenti
            appointmentsScreen = new ArrayList<>(mainContainer.getChildren());
            
            //inizializzazione dei bottoni per le date
            date0.setUserData(0);
            date1.setUserData(1);
            date2.setUserData(2);
            date3.setUserData(3);
            date4.setUserData(4);
            
            dateButtons = new Button[] {date0, date1, date2, date3, date4};
            nextButtonDate = LocalDate.now();
            printNextDates();
            
            //inizializzazione dei bottoni di navigazione
            navButton0.setUserData(0);
            navButton1.setUserData(1);
            navButton2.setUserData(2);
            
            navButtons = new Button[] {navButton0, navButton1, navButton2};
            
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
    
    @FXML
    void showNextDates(){
        printNextDates();
    }
    
    @FXML
    void showPrevDates(){
        
        if(today.plusDays(5).equals(nextButtonDate)) return;
        
        nextButtonDate = nextButtonDate.minusDays(10);
        printNextDates();
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
    
    @FXML
    void loadAppointmentsTable(ActionEvent e){
        
        Button clicked = (Button) e.getSource();
        int index = (Integer) clicked.getUserData();
        
        if(index == activeDateButton) return;
        
        if(activeDateButton != -1){
            dateButtons[activeDateButton].getStyleClass().remove("day-button-active");
            dateButtons[activeDateButton].getStyleClass().add("day-button");
        }
        
        clicked.getStyleClass().remove("day-button");
        clicked.getStyleClass().add("day-button-active");
        
        activeDateButton = index;
        appointmentsTable.setVisible(true);
        
        //TODO caricamento della tabella degli appuntamenti
    }
    
    @FXML
    void switchScreen(ActionEvent e){
        Button clicked = (Button) e.getSource();
        int index = (Integer) clicked.getUserData();
        
        if(index == activeNavButton) return;
        mainContainer.getChildren().clear();
        
        navButtons[activeNavButton].getStyleClass().remove("nav-button-active");
        clicked.getStyleClass().add("nav-button-active");
        
        activeNavButton = index;
        
        switch(activeNavButton){
            
            case 0: 
                mainContainer.getChildren().setAll(appointmentsScreen);
                break;
            case 1:
                //TODO
                break;
            case 2:
                //TODO
                break;
            
        }
        
    }
    
    @FXML
    void reload(){
        try{
            App.setRoot("doctorsMenu");
        }catch(Exception e){
            e.printStackTrace();
            System.exit(1);
        }
    }
    
}
