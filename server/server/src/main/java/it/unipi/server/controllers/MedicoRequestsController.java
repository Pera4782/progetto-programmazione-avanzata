package it.unipi.server.controllers;

import it.unipi.server.model.ServerErrorException;
import it.unipi.server.model.utils.QueryHandler;
import it.unipi.server.model.Medico;
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
}
