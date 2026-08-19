package it.unipi.server.controllers;

import it.unipi.server.model.Paziente;
import it.unipi.server.model.ServerErrorException;
import it.unipi.server.model.utils.QueryHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "paziente")
public class PazienteRequestsController {
    
    
    /**
     * @brief end point per ottenere le informazioni di un paziente tramite la matricola
     * @param matricola matricola del paziente
     * @return il paziente o null se non esiste
     */
    @GetMapping(path = "info")
    public @ResponseBody Paziente getInfoPaziente(@RequestParam(name = "_0") int matricola){
        try{
            Paziente result = QueryHandler.findUtenteByMatricola(matricola, Paziente.class);
            return result;
        }catch(ServerErrorException se){
            return null;
        }
        
    }
    
}
