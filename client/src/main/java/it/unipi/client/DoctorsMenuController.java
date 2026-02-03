package it.unipi.client;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Arrays;
import java.util.Comparator;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
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
    
    private Button[] dateButtons;
    private int activeDateButton = -1;
    private LocalDate nextButtonDate;
    private final LocalDate today = LocalDate.now();
    
    @FXML
    private Button navButton0;
    @FXML
    private Button navButton1;
    @FXML
    private Button navButton2;
    
    private Button[] navButtons;
    private int activeNavButton = 0;
    
    @FXML
    private TableView<Visita> appointmentsTable;
    
    @FXML
    private TableColumn<Visita, String> patientColumn;
    @FXML
    private TableColumn<Visita, LocalTime> timeColumn;
    @FXML
    private TableColumn<Visita, String> typeColumn;
    
    private ObservableList<Visita> rowList;
    
    /**
     * @brief funzione per stampare le date all'interno dei bottoni
     */
    private void printNextDates(){
        DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM");
        
        if(activeDateButton != -1){
            dateButtons[activeDateButton].getStyleClass().remove("day-button-active");
            dateButtons[activeDateButton].getStyleClass().add("day-button");
            activeDateButton = -1;
            rowList.clear();
            appointmentsTable.setVisible(false);
        }
            
        for(int i = 0; i < 5; ++i){
            dateButtons[i].setText(nextButtonDate.format(format));
            nextButtonDate = nextButtonDate.plusDays(1);
        }
    }
    
    @FXML
    void initialize(){
        
        //riempimento del nome del dottore
        try{
            String[] nomeCognome = RequestHandler.GETRequest("medico/nominativo", String[].class, Integer.toString(LoginController.loggedMatricola));
            
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
            
            activeDateButton = -1;
            
            dateButtons = new Button[] {date0, date1, date2, date3, date4};
            nextButtonDate = LocalDate.now();
            printNextDates();
            
            //inizializzazione dei bottoni di navigazione
            navButton0.setUserData(0);
            navButton1.setUserData(1);
            navButton2.setUserData(2);
            
            activeNavButton = 0;
            
            navButtons = new Button[] {navButton0, navButton1, navButton2};
            
            //inizializzazione delle colonne della tabella
            patientColumn.setCellValueFactory(new PropertyValueFactory<>("nomePaziente"));
            timeColumn.setCellValueFactory(new PropertyValueFactory<>("ora"));
            typeColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));
            
            rowList = FXCollections.observableArrayList();
            
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
    
    
    /**
     * @brief funzione per mostrare la tabella degli appuntamenti quando viene premuta una data
     * @param e evento
     */
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
        LocalDate clickedDate = nextButtonDate.minusDays(5 - index);
        appointmentsTable.setVisible(true);

        //riempimento della tabella
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call(){
                
                try{
                    
                    rowList.clear();
                    
                    Visita[] response = RequestHandler.GETRequest("visita/medico", Visita[].class, Integer.toString(LoginController.loggedMatricola));
                    if(response == null) return null;
                    
                    Visita[] rows = Arrays.stream(response).filter(v -> v.getData().equals(clickedDate)).toArray(Visita[]::new);
                    
                    if(rows == null) return null;
                    
                    Arrays.sort(rows, Comparator.comparing(Visita::getOra));
                    
                    rowList.addAll(rows);
                    appointmentsTable.setItems(rowList);
                    
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
     * @brief funzione per la rimozione di una visita tramite menu a scomparsa
     */
    @FXML
    void removeVisita(){
        
        //salvo la data che era selezionata per evitare che vengano caricate informazioni sbagliate
        //se l'utente clicca su un'altra data durante l'interazione con la rete
        int selectedDate = activeDateButton;
        
        Task<Void> task = new Task<Void>(){
            @Override
            public Void call(){
                
                try{
                    
                    Visita selected = appointmentsTable.getSelectionModel().getSelectedItem();
                    Integer response = RequestHandler.POSTRequest("visita/elimina", selected, Integer.class);
                    
                    if(response == null) return null;
                    
                    if(selectedDate != activeDateButton) return null;
                    
                    rowList.remove(selected);
                    appointmentsTable.setItems(rowList);
                    
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
     * @brief funzione associata all'evento di click sui bottoni di navigazione
     * @param e evento
     */
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
