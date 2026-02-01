package it.unipi.server;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/account")
public class AccountController {
    
    @PostMapping(path = "/register/paziente")
    public @ResponseBody Integer pazienteRegister(@RequestBody Paziente paziente){
        
        if(paziente.getNome() == null || paziente.getCognome() == null) return -1;
        
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@! .=])[A-Za-z\\d@! .=]{8,}$";
        if(paziente.getPassword() == null || !paziente.getPassword().matches(passwordRegex)) return -2;
        
        try{
            QueryHandler.insertUtente(paziente);
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
        
        try{
            QueryHandler.insertUtente(medico);
            return medico.getMatricola();
        }catch(ServerErrorException se){
            return null;
        }
    }
    
    @PostMapping(path = "/login")
    public @ResponseBody Integer login(@RequestBody LoginRequest lr){
        
        try{
            
            Class table = (lr.getIsDoctor())? Medico.class:Paziente.class;
            
            Utente result = QueryHandler.findUtenteByMatricola(lr.getMatricola(), table);
            if(result == null) return 0;

            String hash = result.getPassword();
            if(!BCrypt.checkpw(lr.getPassword(), hash)) return 1;

            return 2;
        }catch(ServerErrorException se){
            return null;
        }
    }
    

    
}
