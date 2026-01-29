package it.unipi.server;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/register")
public class RegisterController {
   
    
    @PostMapping(path="/paziente")
    public @ResponseBody String response(){
        return "ciao";
    }
    
}
