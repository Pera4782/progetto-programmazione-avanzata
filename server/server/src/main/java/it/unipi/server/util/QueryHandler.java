package it.unipi.server.util;

import it.unipi.server.util.HibernateUtil;
import it.unipi.server.model.Medico;
import it.unipi.server.model.ServerErrorException;
import it.unipi.server.model.Utente;
import it.unipi.server.model.Visita;
import it.unipi.server.model.requests.BookAppointmentRequest;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;

import org.hibernate.Session;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class QueryHandler {
    
    /**
     * @brief funzione per l'inserimento di un utente del database, sia esso paziente o medico
     * @param <T> tipo di utente da inserire
     * @param utente utente da inserire
     * @throws ServerErrorException eccezione che indica un errore del server
     */
    public static <T extends Utente> void insertUtente(T utente) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            session.beginTransaction();
            String hashed = BCrypt.hashpw(utente.getPassword(), BCrypt.gensalt());
            utente.setPassword(hashed);
            session.persist(utente);
            session.getTransaction().commit();
        } catch (Exception e) {
            
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            throw new ServerErrorException();
            
        } finally {
            session.close();
        }
    }
    
    /**
     * @brief funzione per cercare un utente per matricola
     * @param <T> tipo dell'utente da cercare
     * @param matricola matricola dell'utente
     * @param clazz classe dell'utente
     * @return l'utente trovato o null in caso di errore 
     * @throws ServerErrorException eccezione che indica un errore del server
     */
    public static <T extends Utente> T findUtenteByMatricola(int matricola, Class<T> clazz) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            List<T> result = session.createQuery("FROM " + clazz.getSimpleName() + " WHERE matricola = :matricola", clazz)
                                         .setParameter("matricola", matricola)
                                         .setMaxResults(1)
                                         .getResultList();
            
            return result.isEmpty()? null : result.get(0);
            
        } catch (Exception e) {
            
            e.printStackTrace();
            throw new ServerErrorException();
            
        } finally {
            session.close();
        }
    }
    
    /**
     * @brief funzione per cercare tutte le visite di un medico
     * @param matricola matricola del medico
     * @return un array di visite contenente le visite del medico
     * @throws ServerErrorException eccezione che indica un errore del server
     */
    public static Visita[] getVisiteByMedico(int matricola) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
             
            List<Visita> result = session.createQuery("SELECT v FROM Visita v WHERE v.medico.matricola = :matricola", Visita.class)
                                  .setParameter("matricola", matricola)
                                  .getResultList();
            
            if(result == null) return null;
            
            return result.toArray(new Visita[0]);
            
        }catch (Exception e) {
            e.printStackTrace();
            throw new ServerErrorException();
        } finally {
            session.close();
        }
    }
    
    /**
     * @brief funzione per rimuovere una visita
     * @param visita visita da rimuovere
     * @throws ServerErrorException eccezione che indica un errore del server
     */
    public static void removeVisita(Visita visita) throws ServerErrorException {
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            session.beginTransaction();
            session.remove(session.merge(visita));
            session.getTransaction().commit();
            
        }catch(Exception e){
            session.getTransaction().rollback();
            throw new ServerErrorException();
        }finally{
            session.close();
        }
        
    }
    
    /**
     * @brief funzione per creare una visita
     * @param visita visita che si vuole creare
     * @throws ServerErrorException 
     */
    public static void createVisita(Visita visita) throws ServerErrorException {
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            session.beginTransaction();
            session.persist(visita);
            session.getTransaction().commit();
        }catch(Exception e){
            session.getTransaction().rollback();
            throw new ServerErrorException();
        }finally{
            session.close();
        }
    }
    
    
    /**
     * @brief funzione per ottenere tutti i medici per un match di cognome
     * @param cognome stringa per fare match
     * @return un array di medici
     * @throws ServerErrorException 
     */
    public static Medico[] getMedicoByCognome(String cognome) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            List<Medico> result = session.createQuery("SELECT m FROM Medico m WHERE LOWER(m.cognome) LIKE LOWER(:cognome)", Medico.class)
                                  .setParameter("cognome", "%" + cognome + "%")
                                  .getResultList();
            
            if(result == null) return null;
            
            return result.toArray(new Medico[0]);
            
        }catch(Exception e){
            throw new ServerErrorException();
        }finally{
            session.close();
        }       
    }
    
    
    /**
     * @brief funzione per ottenere tutti i medici per specializzazione
     * @param specializzazione specializzazione da cercare
     * @return un array di medici con la specializzazione cercata
     * @throws ServerErrorException 
     */
    public static Medico[] getMedicoBySpecializzazione(String specializzazione) throws ServerErrorException{
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            
            List<Medico> result;
            
            if(specializzazione.equals("Qualsiasi")){
                
                result = session.createQuery("SELECT m FROM Medico m", Medico.class)
                                    .getResultList();
                
            }else{
                result = session.createQuery("SELECT m FROM Medico m WHERE m.specializzazione = :specializzazione", Medico.class)
                                  .setParameter("specializzazione", specializzazione)
                                  .getResultList();
            }
            
            
            if(result == null) return null;
            
            return result.toArray(new Medico[0]);
            
        }catch(Exception e){
            throw new ServerErrorException();
        }finally{
            session.close();
        }       
    }
    
    /**
     * @brief funzione per ottenere tutti i medici per specializzazione e per cognome
     * @param cognome stringa su cui fare match del cognome
     * @param specializzazione specializzazione da cercare
     * @return un array di medici con la specializzazione e cognome cercati
     * @throws ServerErrorException 
     */
    public static Medico[] getMedicoByCognomeAndSpecializzazione(String cognome, String Specializzazione) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            List<Medico> result = session.createQuery("SELECT m FROM Medico m WHERE LOWER(m.cognome) LIKE LOWER(:cognome) AND m.specializzazione = :specializzazione", Medico.class)
                                  .setParameter("cognome", "%" + cognome + "%")
                                  .setParameter("specializzazione", Specializzazione)
                                  .getResultList();
            
            if(result == null) return null;
            
            return result.toArray(new Medico[0]);
            
        }catch(Exception e){
            throw new ServerErrorException();
        }finally{
            session.close();
        }       
    }
    
    /**
     * @brief funzione per ottenere le visite con una certa data
     * @param date data da cercare
     * @return un array di visite con la data cercata
     * @throws ServerErrorException 
     */
    public static Visita[] getVisiteByData(LocalDate date) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            List<Visita> result = session.createQuery("SELECT v FROM Visita v WHERE v.data = :data OR v.data IS NULL ORDER BY v.data", Visita.class)
                                  .setParameter("data", date)
                                  .getResultList();
            
            if(result == null) return null;
            
            return result.toArray(new Visita[0]);
            
        }catch(Exception e){
            throw new ServerErrorException();
        }finally{
            session.close();
        }       
        
    }
    
    
    /**
     * @brief funzione per prenotare un appuntamento ordinario
     * @param bas richiesta di prenotazione
     * @param visita visita da prenotare
     * @param session sessione hibernate
     * @throws ServerErrorException
     * @throws Exception 
     */
    private static void bookAppointmentOrdinario(BookAppointmentRequest bas, Visita visita, Session session) throws ServerErrorException, Exception{
        
        Visita prenotazione = new Visita();
        
        prenotazione.setMedico(visita.getMedico());
        prenotazione.setPaziente(bas.getPaziente());
        prenotazione.setTipo(visita.getTipo());
        prenotazione.setData(bas.getDate());
        prenotazione.setOra(bas.getTime());
        prenotazione.setOrdinaria(true);
        
        session.persist(prenotazione);
        
        session.getTransaction().commit();
    }
    
    
    /**
     * @brief funzione per prenotare un appuntamento non ordinario
     * @param bas richiesta di prenotazione
     * @param visita visita da prenotare
     * @param session sessione hibernate
     * @throws ServerErrorException
     * @throws Exception 
     */
    private static void bookAppointmentNotOrdinario(BookAppointmentRequest bas, Visita visita, Session session){
        visita.setPaziente(bas.getPaziente());
        session.merge(visita);
        session.getTransaction().commit();
    }
    
    
    /**
     * @brief funzione per prenotare un appuntamento
     * @param bas richiesta di prenotazione
     * @throws ServerErrorException 
     */
    public static void bookAppointment(BookAppointmentRequest bas) throws ServerErrorException{
        
        Visita[] visite = QueryHandler.getVisiteByData(bas.getDate());
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            
            session.beginTransaction();
            
            Visita correctVisita = null;
            
            for(Visita visita: visite) {
                if((bas.getDate().getDayOfWeek() == DayOfWeek.SATURDAY || bas.getDate().getDayOfWeek() == DayOfWeek.SUNDAY) &&
                   visita.getOrdinaria()){
                    continue;
                } 
                
                if(visita.getOra().equals(bas.getTime())){
                    correctVisita = visita;
                    break;
                }
            }
            
            if(correctVisita == null) throw new ServerErrorException();
            
            boolean isOrdinaria = correctVisita.getOrdinaria();
            
            if(isOrdinaria) bookAppointmentOrdinario(bas, correctVisita, session);
            else bookAppointmentNotOrdinario(bas, correctVisita, session); 
            
            
        }catch(Exception e){
            session.getTransaction().rollback();
            throw new ServerErrorException();
        }finally{
            session.close();
        }     
    
    }
    
    /**
     * @brief funzione per ottenere le visite per paziente
     * @param matricola matricola del paziente
     * @return le visite trovate
     * @throws ServerErrorException 
     */
    public static Visita[] getVisiteByPaziente(int matricola) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            List<Visita> result = session.createQuery("SELECT v FROM Visita v WHERE v.paziente.matricola = :matricola ORDER BY v.data ASC, v.ora ASC", Visita.class)
                                  .setParameter("matricola", matricola)
                                  .getResultList();
            
            if(result == null) return null;
            
            return result.toArray(new Visita[0]);
            
        }catch(Exception e){
            throw new ServerErrorException();
        }finally{
            session.close();
        }       
        
    }
    
    
    /**
     * @brief funzione per annullare una prenotazione
     * @param visita prenotazione che si vuole annullare
     * @throws ServerErrorException 
     */
    public static void deleteAppointment(Visita visita) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            session.beginTransaction();
            
            if(visita.getOrdinaria()) session.remove(session.merge(visita));
            else{
                visita.setPaziente(null);
                session.merge(visita);
            }
            
            session.getTransaction().commit();
        }catch(Exception e){
            session.getTransaction().rollback();
            throw new ServerErrorException();
        }finally{
            session.close();
        }       
        
    }
}
