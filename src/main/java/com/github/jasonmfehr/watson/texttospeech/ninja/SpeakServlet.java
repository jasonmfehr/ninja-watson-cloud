package com.github.jasonmfehr.watson.texttospeech.ninja;

import java.io.IOException;
import java.net.URI;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpStatus;
import org.apache.http.client.fluent.Executor;
import org.apache.http.client.fluent.Request;
import org.apache.http.client.fluent.Response;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@MultipartConfig
public class SpeakServlet extends HttpServlet {

    private static final long serialVersionUID = -1766490450968322545L;

    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(SpeakServlet.class);
    
    private final String SERVICE_NAME = "text_to_speech";

    /*
    private String baseURL = "<url>";
    private String username = "<username>";
    private String password = "<password>";
    */
    
    private String baseURL = "https://stream.watsonplatform.net/text-to-speech-beta/api";
    private String username = "";
    private String password = "";
    
    @Override
    public void init() throws ServletException {
        super.init();
        
        if("dev".equals(System.getProperty("ninja.mode"))){
            LOGGER.debug("running in dev mode, no credentials loaded");
        }else{
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root;
            
            try {
                root = mapper.readTree(System.getenv("VCAP_SERVICES"));
            } catch (IOException e) {
                throw new ServletException(e);
            }
            
            if(root == null){
                throw new NullPointerException("could not read json from VCAP_SERVICES environment variable");
            }
            
            JsonNode textToSpeechService = root.get(SERVICE_NAME);
            if(textToSpeechService == null){
                throw new NullPointerException("could not find node for service [" + SERVICE_NAME + "]");
            }
            if(textToSpeechService.size() == 0){
                throw new ArrayIndexOutOfBoundsException();
            }
            
            JsonNode credentials = textToSpeechService.get(0).get("credentials");
            if(credentials == null){
                throw new NullPointerException("could not locate credentials node");
            }
            
            this.baseURL = credentials.get("url").asText();
            this.username = credentials.get("username").asText();
            this.password = credentials.get("password").asText();
        }
        
        LOGGER.debug("using text to speech service base URL: {}", this.baseURL);
        LOGGER.debug("using text to speech service username: {}", this.username);
    }

    @Override
    protected void doGet(final HttpServletRequest req, final HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.debug("entering {}.doGet", SpeakServlet.class.getSimpleName());
        
        LOGGER.debug("value of text parameter:  {}" , req.getParameter("text"));
        LOGGER.debug("value of voice parameter:  {}" , req.getParameter("voice"));
        
        if (req.getParameter("text") == null || req.getParameter("voice") == null) {
            LOGGER.debug("either the text or voice parameter was not specified");
            req.getRequestDispatcher("/index.jsp").forward(req, resp);
        } else {
            req.setCharacterEncoding("UTF-8");
            
            try {
                String queryStr = req.getQueryString();
                String url = baseURL + "/v1/synthesize";
                if (queryStr != null) {
                    url += "?" + queryStr;
                }
                URI uri = new URI(url).normalize();

                Request newReq = Request.Get(uri);
                newReq.addHeader("Accept", "audio/ogg; codecs=opus");

                Executor executor = Executor.newInstance().auth(username, password);
                Response response = executor.execute(newReq);

                ServletOutputStream servletOutputStream = resp.getOutputStream();
                response.returnResponse().getEntity()
                .writeTo(servletOutputStream);
                servletOutputStream.flush();
                servletOutputStream.close();
            } catch (Exception e) {
                // Log something and return an error message
                LOGGER.error("unknown error", e);
                resp.setStatus(HttpStatus.SC_BAD_GATEWAY);
            }
        }
            
        LOGGER.debug("exiting {}.doGet", SpeakServlet.class.getSimpleName());
    }
    
}
