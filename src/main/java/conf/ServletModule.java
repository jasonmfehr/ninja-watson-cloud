package conf;

import ninja.servlet.NinjaServletDispatcher;

import com.github.jasonmfehr.watson.texttospeech.ninja.SpeakServlet;

public class ServletModule extends com.google.inject.servlet.ServletModule {

    @Override
    protected void configureServlets() {
        bind(SpeakServlet.class).asEagerSingleton();
        bind(NinjaServletDispatcher.class).asEagerSingleton();

        serve("/speak").with(SpeakServlet.class);
        serve("/*").with(NinjaServletDispatcher.class);
    }
    
}
