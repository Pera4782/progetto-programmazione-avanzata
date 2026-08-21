package it.unipi.server.model.utils;

import it.unipi.server.model.Medico;
import it.unipi.server.model.ServerErrorException;
import it.unipi.server.model.Utente;
import it.unipi.server.model.Visita;
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
            session.remove(visita);
            session.getTransaction().commit();
            
        }catch(Exception e){
            session.getTransaction().rollback();
            throw new ServerErrorException();
        }finally{
            session.close();
        }
        
    }
    
    
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
    
    public static Visita[] getVisiteByData(LocalDate date) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            List<Visita> result = session.createQuery("SELECT v FROM Visita v WHERE v.data = :data", Visita.class)
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
    
}
