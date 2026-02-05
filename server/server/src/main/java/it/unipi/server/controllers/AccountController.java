package it.unipi.server.controllers;

import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import it.unipi.server.model.Medico;
import it.unipi.server.model.Paziente;
import it.unipi.server.model.utils.QueryHandler;
import it.unipi.server.model.ServerErrorException;
import it.unipi.server.model.Utente;
import it.unipi.server.model.requests.LoginRequest;
import it.unipi.server.model.requests.RegisterRequest;
import it.unipi.server.model.responses.LoginResponse;
import it.unipi.server.model.responses.RegisterResponse;

@Controller
@RequestMapping(path="/account")
public class AccountController {
    
    /**
     * @brief endpoint per la registrazione di un paziente
     * @param registerRequest richiesta di registrazione
     * @return stato della risposta
     */
    @PostMapping(path = "/register")
    public @ResponseBody RegisterResponse pazienteRegister(@RequestBody RegisterRequest registerRequest){
        
        
        if(registerRequest.getNome() == null || registerRequest.getCognome() == null || (registerRequest.getIsDoctor() && registerRequest.getSpecializzazione() == null ))
            return new RegisterResponse(RegisterResponse.Status.EMPTYFIELDS, -1);
        
        String passwordRegex = "^(?=.*[A-Z])(?=.*\\d)(?=.*[@! .=])[A-Za-z\\d@! .=]{8,}$";
        if(registerRequest.getPassword() == null || !registerRequest.getPassword().matches(passwordRegex))
            return new RegisterResponse(RegisterResponse.Status.WRONGPASSWORDFORMAT, -1);
        
        try{
            Utente utente;
            if (registerRequest.getIsDoctor()) {
                utente = new Medico(0, registerRequest.getPassword(), registerRequest.getNome(), registerRequest.getCognome(), registerRequest.getSpecializzazione());
            } else {
                utente = new Paziente(0, registerRequest.getPassword(), registerRequest.getNome(), registerRequest.getCognome());
            }

            QueryHandler.insertUtente(utente);
            return new RegisterResponse(RegisterResponse.Status.SUCCESS, utente.getMatricola());
        }catch(ServerErrorException se){
            return null;
        }
        
    }

    
    /**
     * @brief end point per il login di un utente sia esso medico o paziente
     * @param loginRequest  richiesta di login
     * @return stato della risposta
     */
    @PostMapping(path = "/login")
    public @ResponseBody LoginResponse login(@RequestBody LoginRequest loginRequest){
        
        try{
            
            Class table = (loginRequest.getIsDoctor())? Medico.class:Paziente.class;
            
            Utente result = QueryHandler.findUtenteByMatricola(loginRequest.getMatricola(), table);
            if(result == null) return new LoginResponse(LoginResponse.Status.MATRICOLANOTFOUND);

            String hash = result.getPassword();
            if(!BCrypt.checkpw(loginRequest.getPassword(), hash)) return new LoginResponse(LoginResponse.Status.WRONGPASSWORD);

            return new LoginResponse(LoginResponse.Status.SUCCESS);
        }catch(ServerErrorException se){
            return null;
        }
    }
    

    
}
