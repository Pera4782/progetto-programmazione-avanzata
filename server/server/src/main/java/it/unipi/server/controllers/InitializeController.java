package it.unipi.server.controllers;

import it.unipi.server.model.ServerErrorException;
import it.unipi.server.model.responses.Response;
import it.unipi.server.util.QueryHandler;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/inizializza")
public class InitializeController {
    
    
    @PostMapping
    public @ResponseBody Response loadDB(){
        try{
            QueryHandler.loadDB();
            return new Response(false);
        }catch(ServerErrorException se){
            return new Response(true);
        }
    }
    
}
