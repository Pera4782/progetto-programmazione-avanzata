package it.unipi.client;

import it.unipi.client.model.Visita;
import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.responses.GetVisiteByMedicoResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.Comparator;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;


public class BookedAppointmentScreen extends VBox {

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
    
    public BookedAppointmentScreen() {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("bookedAppointmentScreen.fxml"));
        fxmlLoader.setRoot(this);
        fxmlLoader.setController(this);

        try {
            fxmlLoader.load();
        } catch (IOException exception) {
            throw new RuntimeException();
        }
    }
    
    
    @FXML
    void initialize(){
        
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
        
        //inizializzazione delle colonne della tabella
        patientColumn.setCellValueFactory(new PropertyValueFactory<>("nomePaziente"));
        timeColumn.setCellValueFactory(new PropertyValueFactory<>("ora"));
        typeColumn.setCellValueFactory(new PropertyValueFactory<>("tipo"));

        rowList = FXCollections.observableArrayList();
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
                    
                    GetVisiteByMedicoResponse response = RequestHandler.GETRequest("visita/medico", GetVisiteByMedicoResponse.class, 
                                                                                   Integer.toString(LoginController.loggedMatricola));
                    if(response == null || response.getStatus() == GetVisiteByMedicoResponse.Status.ERROR){
                        //TODO
                        return null;
                    }
                    
                    Visita[] rows = Arrays.stream(response.getVisite()).filter(v -> v.getData().equals(clickedDate)).toArray(Visita[]::new);
                    
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
        int selectedDate = activeDateButton;
        
        Task<Void> task = new Task<Void>(){
            @Override
            public Void call(){
                try{
                    
                    Visita selected = appointmentsTable.getSelectionModel().getSelectedItem();
                    Boolean response = RequestHandler.POSTRequest("visita/elimina", selected, Boolean.class);
                    
                    if(response == null || response == false) {
                        return null;
                    }
                    
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
    
}
