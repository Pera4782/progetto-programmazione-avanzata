package it.unipi.server.controllers;

import it.unipi.server.model.ServerErrorException;
import it.unipi.server.util.QueryHandler;
import it.unipi.server.model.Visita;
import it.unipi.server.model.requests.BookAppointmentRequest;
import it.unipi.server.model.requests.CreateVisitaRequest;
import it.unipi.server.model.responses.GetVisiteResponse;
import it.unipi.server.model.responses.Response;
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
    public @ResponseBody Response removeVisita(@RequestBody Visita visita){
        try{
            QueryHandler.removeVisita(visita);
            return new Response(false);
        }catch(ServerErrorException se){
            return new Response(true);
        }
    }
    
    /**
     * @brief end point per la creazione di un appuntamento da parte di un medico
     * @param createVisitaRequest richiesta di creazione di una visita
     * @return true in caso di successo false altrimenti
     */
    @PostMapping(path = "/crea")
    public @ResponseBody Response createVisita(@RequestBody CreateVisitaRequest createVisitaRequest){
        try{
            
            Visita visita = new Visita(0, createVisitaRequest.getMedico(), null, createVisitaRequest.getDate(), createVisitaRequest.getTime(),
                                       createVisitaRequest.getType(), createVisitaRequest.getOrdinaria());
            
            QueryHandler.createVisita(visita);
            return new Response(false);
            
        }catch(ServerErrorException se){
            return new Response(true);
        }
    }
    
    /**
     * @brief end point per ottenere le visite con una certa data
     * @param date data che si vuole cercare
     * @return le visite cercate e lo stato della risposta
     */
    @GetMapping(path = "/data")
    public @ResponseBody GetVisiteResponse getVisiteByData(@RequestParam(name = "_0") LocalDate date){
        
        try{
            Visita[] visite = QueryHandler.getVisiteByData(date);
            return new GetVisiteResponse(GetVisiteResponse.Status.SUCCESS, visite);
        }catch(ServerErrorException se){
            return new GetVisiteResponse(GetVisiteResponse.Status.ERROR, null);
        }
        
    }
    
    /**
     * @brief end point per prenotare un appuntamento
     * @param bas richiesta di prenotazione
     * @return lo stato della risposta
     */
    @PostMapping(path = "/prenota")
    public @ResponseBody Response bookAppointment(@RequestBody BookAppointmentRequest bas){
        
        try{
            QueryHandler.bookAppointment(bas);
            return new Response(false);
        }catch(ServerErrorException se){
            return new Response(true);
        }
        
    }
    
    
    /**
     * @brief end point per la ricerca degli appuntamenti per paziente 
     * @param matricola matricola del paziente
     * @return le visite e lo stato della risposta
     */
    @GetMapping(path = "/paziente")
    public @ResponseBody GetVisiteResponse getVisiteByPaziente(@RequestParam(name = "_0") int matricola){
    
        try{
            Visita[] visite = QueryHandler.getVisiteByPaziente(matricola);
            return new GetVisiteResponse(GetVisiteResponse.Status.SUCCESS, visite);
        }catch(ServerErrorException se){
            return new GetVisiteResponse(GetVisiteResponse.Status.ERROR, null);
        }
    }
    
    /**
     * @brief end point per la cancellazione di una prenotazione
     * @param visita visita di cui si vuole annullare la prenotazione
     * @return lo stato della risposta
     */
    @PostMapping(path = "/annulla/prenotazione")
    public @ResponseBody Response deleteAppointment(@RequestBody Visita visita){
        try{
            QueryHandler.deleteAppointment(visita);
            return new Response(false);
        }catch(ServerErrorException se){
            return new Response(true);
        }
    }
    
}
