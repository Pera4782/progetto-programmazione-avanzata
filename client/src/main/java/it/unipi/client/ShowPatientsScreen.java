package it.unipi.client;

import it.unipi.client.model.Paziente;
import it.unipi.client.model.Visita;
import it.unipi.client.model.RequestHandler;
import it.unipi.client.model.responses.GetVisiteByMedicoResponse;
import java.io.IOException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.VBox;

public class ShowPatientsScreen extends VBox{
    
    @FXML
    private TableView<Visita> patientsTable;
    
    @FXML
    private TableColumn<Visita, String> nameColumn;
    @FXML
    private TableColumn<Visita, Integer> matColumn;
    @FXML
    private TableColumn<Visita, LocalDate> nearestVisitColumn;
    @FXML
    private TableColumn<Visita, String> statusColumn;
    
    private ObservableList<Visita> rowList;
    
    /**
     * 
     * @param visite lista di visite
     * @param paziente paziente da trovare
     * @return l'indice all'interno dell'array -1 altrimenti
     */
    private int findVisitaPaziente(ArrayList<Visita> visite, Paziente paziente){
        
        for(int i = 0; i < visite.size(); ++i)
            if(visite.get(i).getPaziente().getMatricola() == paziente.getMatricola()) return i;
        
        return -1;
    }
    
    /**
     * 
     * @param date1
     * @param date2
     * @return funzione che restituisce vero se date1 è più vicina a oggi di date2
     */
    private boolean isCloser(LocalDate date1, LocalDate date2) {
        if (date1 == null) return false;
        if (date2 == null) return true;

        LocalDate oggi = LocalDate.now();

        // ChronoUnit.DAYS calcola la differenza in giorni interi
        long diff1 = Math.abs(ChronoUnit.DAYS.between(oggi, date1));
        long diff2 = Math.abs(ChronoUnit.DAYS.between(oggi, date2));

        return diff1 < diff2;
    }
    
    
    public ShowPatientsScreen(){
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("showPatientsScreen.fxml"));
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
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("nomePaziente"));
        matColumn.setCellValueFactory(new PropertyValueFactory<>("matricolaPaziente"));
        nearestVisitColumn.setCellValueFactory(new PropertyValueFactory<>("data"));
        statusColumn.setCellValueFactory(new PropertyValueFactory<>("statusVisita"));
        
        rowList = FXCollections.observableArrayList();
        
        Task<Void> task = new Task<Void>() {
            @Override
            public Void call() {
                
                try {
                    
                    GetVisiteByMedicoResponse response = RequestHandler.GETRequest("visita/medico", GetVisiteByMedicoResponse.class, 
                                                                                    Integer.toString(LoginController.loggedMatricola));
                    
                    if(response == null || response.getStatus() == GetVisiteByMedicoResponse.Status.ERROR){
                        throw new Exception();
                    }
                    
                    Visita[] visiteMedico = response.getVisite();
                    ArrayList<Visita> nearestVisite = new ArrayList<>();
                    
                    for(Visita visita: visiteMedico){
                        
                        int index = findVisitaPaziente(nearestVisite, visita.getPaziente());
                        
                        if(index == -1) nearestVisite.add(visita);
                        else if(!isCloser(nearestVisite.get(index).getData(), visita.getData())) nearestVisite.set(index, visita);
                    }
                    
                    rowList.addAll(nearestVisite);
                    patientsTable.setItems(rowList);
                    
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
