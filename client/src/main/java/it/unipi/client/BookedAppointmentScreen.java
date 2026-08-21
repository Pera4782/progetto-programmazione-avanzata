package it.unipi.client;

import it.unipi.client.model.Visita;
import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.UserSession;
import it.unipi.client.model.responses.GetVisiteResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import javafx.application.Platform;
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
    
    @FXML
    private TableColumn<Visita, String> ordinariaColumn;
    
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
    
    
    /**
     * @brief funzione per il filtraggio delle visite 
     * @param unfilteredVisits array di visite
     * @param clickedDate data selezionata
     * @return ArrayList di visite filtrate
     */
    private ArrayList<Visita> filterVisits(Visita[] unfilteredVisits, LocalDate clickedDate){
    
        ArrayList<Visita> visite = new ArrayList<>(Arrays.asList(unfilteredVisits));
        HashMap<LocalTime, Integer> timeMap = new HashMap<>();

        for(int i = 0; i < visite.size(); ++i){
            if(timeMap.containsKey(visite.get(i).getOra())) 
                timeMap.put(visite.get(i).getOra(), timeMap.get(visite.get(i).getOra()) + 1);
            else
                timeMap.put(visite.get(i).getOra(), 1);
        }

        
        
        visite.removeIf(v -> {
            boolean hasDuplicateTimeAndNoPatient = v.getOrdinaria() && timeMap.get(v.getOra()) > 1 && v.getPaziente() == null;
            boolean isNotOrdinaryAndDifferentDate = !v.getOrdinaria() && !v.getData().equals(clickedDate);
            return hasDuplicateTimeAndNoPatient || isNotOrdinaryAndDifferentDate;
        });
                    
        return visite;
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
        ordinariaColumn.setCellValueFactory(new PropertyValueFactory<>("ordinariaChar"));

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

        rowList.clear();
        
        //riempimento della tabella
        Task<Void> task = new Task<Void>() {
            
            @Override
            public Void call(){
                try{
                    
                    GetVisiteResponse response = RequestHandler.GETRequest("visita/medico", GetVisiteResponse.class, 
                                                                                   Integer.toString(UserSession.getSession().getLoggedMatricola()));
                    if(response == null || response.getStatus() == GetVisiteResponse.Status.ERROR)
                        throw new Exception();
                    
                    
                    ArrayList<Visita> rows = filterVisits(response.getVisite(), clickedDate);
                    
                    if(rows == null || rows.isEmpty()) return null;
                    
                    rows.sort(Comparator.comparing(Visita::getOra));
                    
                    
                    Platform.runLater(() -> {
                        rowList.addAll(rows);
                        appointmentsTable.setItems(rowList);
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
