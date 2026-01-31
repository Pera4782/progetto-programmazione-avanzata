package it.unipi.server;

import java.util.List;
import org.hibernate.Session;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/account")
public class AccountController {
   
    
    private <T extends Utente> void insertUtente(T utente) throws ServerErrorException{
        
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
    
    private <T extends Utente> T findUtente(T utente, Class<T> clazz) throws ServerErrorException{
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            List<T> result = session.createQuery("FROM " + clazz.getSimpleName() + " WHERE matricola = :matricola", clazz)
                                         .setParameter("matricola", utente.getMatricola())
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
    
    
    
    @PostMapping(path = "/register/paziente")
    public @ResponseBody Integer pazienteRegister(@RequestBody Paziente paziente){
        
        if(paziente.getNome() == null || paziente.getCognome() == null) return -1;
        
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@! .=])[A-Za-z\\d@! .=]{8,}$";
        if(paziente.getPassword() == null || !paziente.getPassword().matches(passwordRegex)) return -2;
        
        try{
            insertUtente(paziente);
            return paziente.getMatricola();
        }catch(ServerErrorException se){
            return null;
        }
        
    }
    
    @PostMapping(path = "/register/medico")
    public @ResponseBody Integer medicoRegister(@RequestBody Medico medico){
        
        if(medico.getNome() == null || medico.getCognome().isEmpty() || medico.getSpecializzazione().isEmpty()) return -1;
           
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@! .=])[A-Za-z\\d@! .=]{8,}$";
        if(medico.getPassword() == null || !medico.getPassword().matches(passwordRegex)) return -2;

        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try{
            insertUtente(medico);
            return medico.getMatricola();
        }catch(ServerErrorException se){
            return null;
        }
    }
    
    @PostMapping(path = "/login/paziente")
    public @ResponseBody Integer pazienteLogin(@RequestBody Paziente paziente){
        
        try{
            
            Paziente result = findUtente(paziente, Paziente.class);
            if(result == null) return 0;

            String hash = result.getPassword();
            if(!BCrypt.checkpw(paziente.getPassword(), hash)) return 1;

            return 2;
        }catch(ServerErrorException se){
            return null;
        }
    }
    
    
    @PostMapping(path = "/login/medico")
    public @ResponseBody Integer pazienteLogin(@RequestBody Medico medico){
        
        try{
            
            Medico result = findUtente(medico, Medico.class);
            if(result == null) return 0;

            String hash = result.getPassword();
            if(!BCrypt.checkpw(medico.getPassword(), hash)) return 1;

            return 2;
        }catch(ServerErrorException se){
            return null;
        }
    }

    
}
