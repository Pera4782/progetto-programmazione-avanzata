package it.unipi.server;

import java.util.List;
import org.hibernate.Session;
import org.springframework.security.crypto.bcrypt.BCrypt;


public class QueryHandler {
    
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
    
    
}
