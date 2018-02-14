package jsfwf.util;

import org.jboss.logging.Logger;

import javax.faces.application.Application;
import javax.faces.event.AbortProcessingException;
import javax.faces.event.PostConstructApplicationEvent;
import javax.faces.event.PreDestroyApplicationEvent;
import javax.faces.event.SystemEvent;
import javax.faces.event.SystemEventListener;

public class FacesAppListener implements SystemEventListener {
    
    private static final Logger LOGGER = Logger.getLogger(FacesAppListener.class);

    @Override
    public void processEvent(SystemEvent event) throws AbortProcessingException {
        
        if (event instanceof PostConstructApplicationEvent) {
            LOGGER.debug("PostConstructApplicationEvent");
        }

        if (event instanceof PreDestroyApplicationEvent){
            LOGGER.debug("PreDestroyApplicationEvent (not reliable)");
        }

    }

    @Override
    public boolean isListenerForSource(Object source) {
        //only for Application
        return (source instanceof Application);
    }
}
