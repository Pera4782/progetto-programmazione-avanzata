package it.unipi.server;

import org.hibernate.Session;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/register")
public class RegisterController {
   
    
    @PostMapping(path = "/paziente")
    public @ResponseBody Integer pazienteRegister(@RequestBody Paziente paziente){
        
        if(paziente.getNome() == null || paziente.getNome().isEmpty() || 
           paziente.getCognome() == null || paziente.getCognome().isEmpty()) return -1;
        
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@! .=])[A-Za-z\\d@! .=]{8,}$";
        if(paziente.getPassword() == null || !paziente.getPassword().matches(passwordRegex)) return -2;
        
        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            session.beginTransaction();
            String hashed = BCrypt.hashpw(paziente.getPassword(), BCrypt.gensalt());
            paziente.setPassword(hashed);
            session.persist(paziente);
            session.getTransaction().commit();
        } catch (Exception e) {
            
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.exit(1);
            
        } finally {
            session.close();
        }
        
        return paziente.getMatricola();
    }
    
    @PostMapping(path = "/medico")
    public @ResponseBody Integer medicoRegister(@RequestBody Medico medico){
        
        if(medico.getNome() == null || medico.getNome().isEmpty() || medico.getSpecializzazione().isEmpty()) return -1;
           
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@! .=])[A-Za-z\\d@! .=]{8,}$";
        if(medico.getPassword() == null || !medico.getPassword().matches(passwordRegex)) return -2;

        Session session = HibernateUtil.getSessionFactory().openSession();
        
        try {
            session.beginTransaction();
            String hashed = BCrypt.hashpw(medico.getPassword(), BCrypt.gensalt());
            medico.setPassword(hashed);
            session.persist(medico);
            session.getTransaction().commit();
            
        } catch (Exception e) {
            e.printStackTrace();
            if (session.getTransaction() != null) {
                session.getTransaction().rollback();
            }
            System.exit(1);
        } finally {
            session.close();
        }
        
        return medico.getMatricola();
    }
    
}
