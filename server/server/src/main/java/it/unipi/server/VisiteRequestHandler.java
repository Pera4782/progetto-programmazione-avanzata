package it.unipi.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/visita")
public class VisiteRequestHandler {

    /**
     * @brief end point per cercare le visite per medico
     * @param matricola matricola del medico
     * @return un array di visite che contiene le visite trovate null in caso di errore/visite non trovate
     */
    @GetMapping(path = "/medico")
    public @ResponseBody Visita[] getVisiteByMedico(@RequestParam(name = "_0") int matricola){
        try{
            return QueryHandler.getVisiteByMedico(matricola);
        }catch(ServerErrorException se){
            return null;
        }
    }
    
    /**
     * @brief end point per eliminare una determinata visita
     * @param visita visita che si vuole eliminare
     * @return 0 in caso di successo null altrimenti
     */
    @PostMapping(path = "/elimina")
    public @ResponseBody Integer removeVisita(@RequestBody Visita visita){
        try{
            QueryHandler.removeVisita(visita);
            return 0;
        }catch(ServerErrorException se){
            return null;
        }
    }
    
}
