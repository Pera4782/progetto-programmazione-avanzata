package it.unipi.client;

import com.google.gson.Gson;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class RequestHandler{
    
    private final String serverUrl = "http://localhost:8080/";
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();
    
    
    public RequestHandler(){}
    
    public <R> R GETRequest(String endPoint, Class<R> clazz, String... queryParams) throws IOException, InterruptedException{
        
        
        StringBuilder sb = new StringBuilder(serverUrl + endPoint);
        
        if(queryParams != null && queryParams.length > 0){
            sb.append("?");
            
            for(int i = 0; i < queryParams.length; ++i){
                
                if(i > 0) sb.append("&");
                sb.append("_").append(i).append("=").append(queryParams[i]);
            }
        }
        
        String completeUrl = sb.toString();
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(completeUrl))
                .GET()
                .header("Accept", "application/json")
                .build();
        
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        if(response.statusCode() != 200){
            System.err.println("Errore HTTP: " + response.statusCode());
            return null;
        }

        R data = gson.fromJson(response.body(), clazz);
        return data;
    }
    
    
    public <R, C> R POSTRequest(String endPoint, C body, Class<R> returnClass) throws IOException, InterruptedException{
        
        String completeUrl = serverUrl + endPoint;
        String jsonBody = gson.toJson(body);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(completeUrl))
                .header("Content-Type", "application/json")
                .header("Accept", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();
        
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        
        if(response.statusCode() != 200){
            System.err.println("Errore HTTP: " + response.statusCode());
            return null;
        }
        
        R data = gson.fromJson(response.body(), returnClass);
        return data;
    }
    
}
