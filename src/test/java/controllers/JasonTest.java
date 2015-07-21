package controllers;

import java.io.IOException;
import java.util.Map.Entry;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JasonTest {

    private final String serviceName = "text_to_speech";
    
    @Before
    public void setUp() {
        System.setProperty("VCAP_SERVICES", 
            "{\"text_to_speech\": [{\"name\": \"text-to-speech-service\",\"label\": \"text_to_speech\",\"plan\": \"free\",\"credentials\": {\"url\": \"base-url\",\"username\": \"username\",\"password\": \"password\"}}]}");
    }
    
    @Test
    public void runTest() throws JsonProcessingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        
        JsonNode root = mapper.readTree(System.getProperty("VCAP_SERVICES"));
        
        if(root == null){
            throw new NullPointerException("could not read json from VCAP_SERVICES environment variable");
        }
        
        JsonNode textToSpeechService = root.get(serviceName);
        if(textToSpeechService == null){
            throw new NullPointerException("could not find node for service [" + serviceName + "]");
        }
        if(textToSpeechService.size() == 0){
            throw new ArrayIndexOutOfBoundsException();
        }
        
        JsonNode credentials = textToSpeechService.get(0).get("credentials");
        if(credentials == null){
            throw new NullPointerException("could not locate credentials node");
        }
        
        System.out.println(credentials.get("url").asText());
        System.out.println(credentials.get("username").asText());
        System.out.println(credentials.get("password").asText());
    }
}
