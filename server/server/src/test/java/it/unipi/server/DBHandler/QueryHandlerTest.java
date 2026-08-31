package it.unipi.server.DBHandler;

import it.unipi.server.model.Medico;
import it.unipi.server.model.Paziente;
import it.unipi.server.model.Visita;
import it.unipi.server.model.requests.BookAppointmentRequest;
import java.time.LocalDate;
import java.time.LocalTime;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class QueryHandlerTest {
    
    private Medico medicoTest;
    private Paziente pazienteTest;
    
    public QueryHandlerTest() {
    }
    
    @BeforeEach
    public void setUp() throws Exception {
        QueryHandler.loadDB();
        
        medicoTest = new Medico();
        medicoTest.setNome("Mario");
        medicoTest.setCognome("Rossi");
        medicoTest.setPassword("password123");
        medicoTest.setSpecializzazione("Cardiologia");

        pazienteTest = new Paziente();
        pazienteTest.setNome("Luigi");
        pazienteTest.setCognome("Verdi");
        pazienteTest.setPassword("password456");
    }
    
    @AfterEach
    public void tearDown() {
        try {
            QueryHandler.loadDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testLoadDB() throws Exception {
        System.out.println("loadDB");
        assertDoesNotThrow(() -> QueryHandler.loadDB());
    }

    @Test
    public void testInsertUtente() throws Exception {
        System.out.println("insertUtente");
        QueryHandler.insertUtente(medicoTest);
        
        assertTrue(medicoTest.getMatricola() > 0, "La matricola dovrebbe essere stata generata dal DB");
        
        Medico trovato = QueryHandler.findUtenteByMatricola(medicoTest.getMatricola(), Medico.class);
        assertNotNull(trovato);
        assertEquals("Rossi", trovato.getCognome());
    }

    @Test
    public void testFindUtenteByMatricola() throws Exception {
        System.out.println("findUtenteByMatricola");
        QueryHandler.insertUtente(medicoTest);
        
        Medico result = QueryHandler.findUtenteByMatricola(medicoTest.getMatricola(), Medico.class);
        assertNotNull(result);
        assertEquals(medicoTest.getMatricola(), result.getMatricola());
        
        Medico nonEsistente = QueryHandler.findUtenteByMatricola(-1, Medico.class);
        assertNull(nonEsistente);
    }

    @Test
    public void testGetVisiteByMedico() throws Exception {
        System.out.println("getVisiteByMedico");
        QueryHandler.insertUtente(medicoTest);
        
        Visita visita = new Visita();
        visita.setMedico(medicoTest);
        visita.setTipo("Controllo");
        visita.setData(LocalDate.now());
        visita.setOra(LocalTime.of(10, 0));
        visita.setOrdinaria(true);
        QueryHandler.createVisita(visita);

        Visita[] result = QueryHandler.getVisiteByMedico(medicoTest.getMatricola());
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals(medicoTest.getMatricola(), result[0].getMedico().getMatricola());
    }

    @Test
    public void testRemoveVisita() throws Exception {
        System.out.println("removeVisita");
        QueryHandler.insertUtente(medicoTest);
        
        Visita visita = new Visita();
        visita.setMedico(medicoTest);
        visita.setTipo("Controllo");
        visita.setData(LocalDate.now());
        visita.setOra(LocalTime.of(11, 0));
        visita.setOrdinaria(true);
        
        QueryHandler.createVisita(visita);
        Visita[] primaDellaRimozione = QueryHandler.getVisiteByMedico(medicoTest.getMatricola());
        int countIniziale = primaDellaRimozione.length;

        QueryHandler.removeVisita(visita);
        Visita[] dopoRimozione = QueryHandler.getVisiteByMedico(medicoTest.getMatricola());
        
        assertEquals(countIniziale - 1, dopoRimozione.length);
    }

    @Test
    public void testCreateVisita() throws Exception {
        System.out.println("createVisita");
        QueryHandler.insertUtente(medicoTest);
        
        Visita visita = new Visita();
        visita.setMedico(medicoTest);
        visita.setTipo("Prima Visita");
        visita.setData(LocalDate.now());
        visita.setOra(LocalTime.of(12, 0));
        visita.setOrdinaria(true);

        assertDoesNotThrow(() -> QueryHandler.createVisita(visita));
    }

    @Test
    public void testGetMedicoByCognome() throws Exception {
        System.out.println("getMedicoByCognome");
        QueryHandler.insertUtente(medicoTest);

        Medico[] result = QueryHandler.getMedicoByCognome("Ros");
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertTrue(result[0].getCognome().toLowerCase().contains("ros"));
    }

    @Test
    public void testGetMedicoBySpecializzazione() throws Exception {
        System.out.println("getMedicoBySpecializzazione");
        QueryHandler.insertUtente(medicoTest);

        Medico[] specifici = QueryHandler.getMedicoBySpecializzazione("Cardiologia");
        assertNotNull(specifici);
        assertTrue(specifici.length > 0);

        Medico[] tutti = QueryHandler.getMedicoBySpecializzazione("Qualsiasi");
        assertNotNull(tutti);
        assertTrue(tutti.length >= specifici.length);
    }

    @Test
    public void testGetMedicoByCognomeAndSpecializzazione() throws Exception {
        System.out.println("getMedicoByCognomeAndSpecializzazione");
        QueryHandler.insertUtente(medicoTest);

        Medico[] result = QueryHandler.getMedicoByCognomeAndSpecializzazione("Rossi", "Cardiologia");
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals("Rossi", result[0].getCognome());
        assertEquals("Cardiologia", result[0].getSpecializzazione());
    }

    @Test
    public void testGetVisiteByData() throws Exception {
        System.out.println("getVisiteByData");
        QueryHandler.insertUtente(medicoTest);
        LocalDate oggi = LocalDate.now();

        Visita visita = new Visita();
        visita.setMedico(medicoTest);
        visita.setTipo("Generica");
        visita.setData(oggi);
        visita.setOra(LocalTime.of(9, 30));
        visita.setOrdinaria(true);
        QueryHandler.createVisita(visita);

        Visita[] result = QueryHandler.getVisiteByData(oggi);
        assertNotNull(result);
        assertTrue(result.length > 0);
    }

    @Test
    public void testBookAppointment() throws Exception {
        System.out.println("bookAppointment");
        QueryHandler.insertUtente(medicoTest);
        QueryHandler.insertUtente(pazienteTest);

        LocalDate prossimoLunedì = LocalDate.now().plusDays(1);
        while (prossimoLunedì.getDayOfWeek().getValue() >= 6) {
            prossimoLunedì = prossimoLunedì.plusDays(1);
        }
        LocalTime ora = LocalTime.of(15, 0);

        Visita visitaSlot = new Visita();
        visitaSlot.setMedico(medicoTest);
        visitaSlot.setTipo("Ordinaria");
        visitaSlot.setData(prossimoLunedì);
        visitaSlot.setOra(ora);
        visitaSlot.setOrdinaria(true);
        QueryHandler.createVisita(visitaSlot);

        BookAppointmentRequest req = new BookAppointmentRequest();
        req.setMedico(medicoTest);
        req.setPaziente(pazienteTest);
        req.setDate(prossimoLunedì);
        req.setTime(ora);

        assertDoesNotThrow(() -> QueryHandler.bookAppointment(req));
    }

    @Test
    public void testGetVisiteByPaziente() throws Exception {
        System.out.println("getVisiteByPaziente");
        QueryHandler.insertUtente(medicoTest);
        QueryHandler.insertUtente(pazienteTest);

        Visita visita = new Visita();
        visita.setMedico(medicoTest);
        visita.setPaziente(pazienteTest);
        visita.setTipo("Controllo");
        visita.setData(LocalDate.now().plusDays(2));
        visita.setOra(LocalTime.of(10, 30));
        visita.setOrdinaria(true);
        QueryHandler.createVisita(visita);

        Visita[] result = QueryHandler.getVisiteByPaziente(pazienteTest.getMatricola());
        assertNotNull(result);
        assertTrue(result.length > 0);
        assertEquals(pazienteTest.getMatricola(), result[0].getPaziente().getMatricola());
    }

    @Test
    public void testDeleteAppointment() throws Exception {
        System.out.println("deleteAppointment");
        QueryHandler.insertUtente(medicoTest);
        QueryHandler.insertUtente(pazienteTest);

        Visita visita = new Visita();
        visita.setMedico(medicoTest);
        visita.setPaziente(pazienteTest);
        visita.setTipo("Da Cancellare");
        visita.setData(LocalDate.now().plusDays(5));
        visita.setOra(LocalTime.of(16, 0));
        visita.setOrdinaria(true);
        QueryHandler.createVisita(visita);

        assertDoesNotThrow(() -> QueryHandler.deleteAppointment(visita));
    }
}