package it.unipi.server.controllers;

import it.unipi.server.model.ServerErrorException;
import it.unipi.server.model.utils.QueryHandler;
import it.unipi.server.model.Visita;
import it.unipi.server.model.requests.BookAppointmentRequest;
import it.unipi.server.model.requests.CreateVisitaRequest;
import it.unipi.server.model.responses.GetVisiteResponse;
import java.time.LocalDate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/visita")
public class VisiteRequestController {

    /**
     * @brief end point per cercare le visite per medico
     * @param matricola matricola del medico
     * @return lo stato della risposta
     */
    @GetMapping(path = "/medico")
    public @ResponseBody GetVisiteResponse getVisiteByMedico(@RequestParam(name = "_0") int matricola){
        try{
            Visita[] visite = QueryHandler.getVisiteByMedico(matricola);
            return new GetVisiteResponse(GetVisiteResponse.Status.SUCCESS, visite);
        }catch(ServerErrorException se){
            return new GetVisiteResponse(GetVisiteResponse.Status.ERROR, null);
        }
    }
    
    /**
     * @brief end point per eliminare una determinata visita
     * @param visita visita che si vuole eliminare
     * @return true in caso di successo false altrimenti
     */
    @PostMapping(path = "/elimina")
    public @ResponseBody Boolean removeVisita(@RequestBody Visita visita){
        try{
            QueryHandler.removeVisita(visita);
            return true;
        }catch(ServerErrorException se){
            return false;
        }
    }
    
    /**
     * @brief end point per la creazione di un appuntamento da parte di un medico
     * @param createVisitaRequest richiesta di creazione di una visita
     * @return true in caso di successo false altrimenti
     */
    @PostMapping(path = "crea")
    public @ResponseBody Boolean createVisita(@RequestBody CreateVisitaRequest createVisitaRequest){
        try{
            
            Visita visita = new Visita(0, createVisitaRequest.getMedico(), null, createVisitaRequest.getDate(), createVisitaRequest.getTime(),
                                       createVisitaRequest.getType(), createVisitaRequest.getOrdinaria());
            
            QueryHandler.createVisita(visita);
            return true;
            
        }catch(ServerErrorException se){
            return false;
        }
    }
    
    @GetMapping(path = "data")
    public @ResponseBody GetVisiteResponse getVisiteByData(@RequestParam(name = "_0") LocalDate date){
        
        try{
            Visita[] visite = QueryHandler.getVisiteByData(date);
            return new GetVisiteResponse(GetVisiteResponse.Status.SUCCESS, visite);
        }catch(ServerErrorException se){
            return new GetVisiteResponse(GetVisiteResponse.Status.ERROR, null);
        }
        
    }
    
    @PostMapping(path = "/prenota")
    public @ResponseBody Integer bookAppointment(@RequestBody BookAppointmentRequest bas){
        
        try{
            QueryHandler.bookAppointment(bas);
            return 0;
        }catch(ServerErrorException se){
            return null;
        }
        
    }
    
}
