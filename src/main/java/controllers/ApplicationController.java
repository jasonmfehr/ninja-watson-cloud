/**
 * Copyright (C) 2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package controllers;

import java.util.logging.Logger;

import ninja.Result;
import ninja.Results;

import com.github.jasonmfehr.watson.texttospeech.ninja.SpeakDelegate;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class ApplicationController {

    private static Logger logger = Logger.getLogger(ApplicationController.class.getName());
    
    @Inject
    private SpeakDelegate speaker;
    
    public Result index() {

        return Results.html();

    }
    
    /*
    public Result speak(Context context) {
        String queryStr;
        
        try {
            Result r = Results.contentType("audio/ogg; codec=opus");
            
            queryStr = "text=" + context.getParameter("text");
            queryStr += "&voice=" + context.getParameter("voice");
            
            //String queryStr = req.getQueryString();
            String url = baseURL + "/v1/synthesize";
            if (queryStr != null) {
                url += "?" + queryStr;
            }
            URI uri = new URI(url).normalize();

            Request newReq = Request.Get(uri);
            newReq.addHeader("Accept", "audio/ogg; codecs=opus");

            Executor executor = Executor.newInstance().auth(username, password);
            Response response = executor.execute(newReq);
            r.renderRaw(response.returnContent().asBytes());
            
            return r;
            ServletOutputStream servletOutputStream = resp.getOutputStream();
            response.returnResponse().getEntity()
            .writeTo(servletOutputStream);
            servletOutputStream.flush();
            servletOutputStream.close();
        } catch (Exception e) {
            // Log something and return an error message
            logger.log(Level.SEVERE, "got error: " + e.getMessage(), e);
            return Results.badRequest();
        }
        
    }
    */
    
    public Result helloWorldJson() {
        
        SimplePojo simplePojo = new SimplePojo();
        simplePojo.content = "Hello World! Hello Json!";

        return Results.json().render(simplePojo);

    }
    
    public static class SimplePojo {

        public String content;
        
    }
}
