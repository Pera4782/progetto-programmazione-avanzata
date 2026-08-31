package it.unipi.server.controllers;

import it.unipi.server.model.ServerErrorException;
import it.unipi.server.DBHandler.QueryHandler;
import it.unipi.server.model.Medico;
import it.unipi.server.model.responses.FindDottoriResponse;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/medico")
public class MedicoRequestsController {
    
    /**
     * @brief end point per ottenere nome e cognome di un medico
     * @param matricola matricola del medico
     * @return il medico trovato null altrimenti
     */
    @GetMapping(path = "/info")
    public @ResponseBody Medico getNominativoDottore(@RequestParam(name = "_0") int matricola){
        
        try{
            
            Medico result = QueryHandler.findUtenteByMatricola(matricola, Medico.class);
            
            return result;
        }catch(ServerErrorException se){
            return null;
        }
    }
    
    
    /**
     * @brief end point per ottenere i medici attraverso cognome e/o specializzazione
     * @param cognome cognome del medico
     * @param specializzazione specializzazione del medico
     * @return un array di medici
     */
    @GetMapping(path = "/find")
    public @ResponseBody FindDottoriResponse findDottori(@RequestParam(name = "_0") String cognome, @RequestParam(name = "_1") String specializzazione){
        
        try{
            
            if(cognome == null) return new FindDottoriResponse(FindDottoriResponse.Status.SUCCESS, QueryHandler.getMedicoBySpecializzazione(specializzazione));
            else if(specializzazione.equals("Qualsiasi")) return new FindDottoriResponse(FindDottoriResponse.Status.SUCCESS, QueryHandler.getMedicoByCognome(cognome));
            else return new FindDottoriResponse(FindDottoriResponse.Status.SUCCESS, QueryHandler.getMedicoByCognomeAndSpecializzazione(cognome, specializzazione));
            
        }catch(ServerErrorException se){
            return new FindDottoriResponse(FindDottoriResponse.Status.ERROR, null);
        }
        
    }
}
