package it.unipi.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path = "/get")
public class GetRequestsHandler {
    
    @GetMapping(path = "/nomeCognomeDottore")
    public @ResponseBody String[] getNomeCognomeDottore(@RequestParam(name = "_0") String matricola){
        
        try{
            
            Medico medico = new Medico(Integer.parseInt(matricola), "", "", "", "");
            Medico result = QueryHandler.findUtenteByMatricola(medico, Medico.class);
            
            String[] nomeCognome = new String[2];
            nomeCognome[0] = result.getNome();
            nomeCognome[1] = result.getCognome();
            
            return nomeCognome;
        }catch(ServerErrorException se){
            return null;
        }
        
    }
}
