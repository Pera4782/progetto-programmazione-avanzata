package it.unipi.client;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializer;

public class RequestHandler{
    
    private static final String serverUrl = "http://localhost:8080/";
    private static final HttpClient client = HttpClient.newHttpClient();
    private static final Gson gson = new GsonBuilder() //aggiunta di metodi di serializzazione/deserializzazione custom per le date e gli orari
            .registerTypeAdapter(LocalDate.class, (JsonDeserializer<LocalDate>) (json, type, jsonDeserializationContext) -> LocalDate.parse(json.getAsString()))
            .registerTypeAdapter(LocalDate.class, (JsonSerializer<LocalDate>) (src, type, jsonSerializationContext) -> new JsonPrimitive(src.toString()))
            .registerTypeAdapter(LocalTime.class, (JsonDeserializer<LocalTime>) (json, type, jsonDeserializationContext) -> LocalTime.parse(json.getAsString()))
            .registerTypeAdapter(LocalTime.class, (JsonSerializer<LocalTime>) (src, type, jsonSerializationContext) -> new JsonPrimitive(src.toString()))
            .create();
    
    
    public RequestHandler(){}
    
    /**
     * @brief funzione per preparare la query string
     * @param endPoint end point da contattare
     * @param queryParams parametri da inserire nella query string
     * @return l'url completo
     */
    private static String prepareQueryString(String endPoint, String[] queryParams){
        StringBuilder sb = new StringBuilder(serverUrl + endPoint);
        
        if(queryParams != null && queryParams.length > 0){
            sb.append("?");
            
            for(int i = 0; i < queryParams.length; ++i){
                
                if(i > 0) sb.append("&");
                sb.append("_").append(i).append("=").append(queryParams[i]);
            }
        }
        
        String completeUrl = sb.toString();
        
        return completeUrl;
    }
    
    /**
     * @brief fa una richiesta get all'endpoint specificato
     * @param <R> tipo di ritorno della funzione
     * @param endPoint endpoint del servizio a cui si vuole fare richiesta
     * @param returnClass tipo di ritorno
     * @param queryParams parametri da aggiungere nella query string
     * @return il risultato della GET
     */
    public static <R> R GETRequest(String endPoint, Class<R> returnClass, String... queryParams) throws IOException, InterruptedException{
        
        
        String completeUrl = prepareQueryString(endPoint, queryParams);
        
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

        R data = gson.fromJson(response.body(), returnClass);
        return data;
    }
    
    /**
     * @brief fa una richiesta POST all'endpoint specificato
     * @param <R> tipo di ritorno
     * @param <B> tipo del body della richiesta
     * @param endPoint endpoint del servizio a cui si vuole fare richiesta
     * @param body body della richiesta
     * @param returnClass tipo di ritorno
     * @return il risultato della POST
     */
    public static <R, B> R POSTRequest(String endPoint, B body, Class<R> returnClass) throws IOException, InterruptedException{
        
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
    
    
    public static <R> R DELETERequest(String endPoint, Class<R> returnClass, String... queryParams) throws IOException, InterruptedException{
        
        String completeUrl = prepareQueryString(endPoint, queryParams);
        
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(completeUrl))
                .DELETE()
                .header("Accept", "application/json")
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
