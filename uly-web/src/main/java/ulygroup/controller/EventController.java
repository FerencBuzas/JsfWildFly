package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.model.Event;
import ulygroup.service.EventService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;
import java.util.ResourceBundle;

@Named
@SessionScoped
public class EventController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EventController.class);
    
    @Inject
    private EventService eventService;

    @Inject
    private LoginController loginController;
    
    private ResourceBundle resourceBundle;

    public List<Event> getList() {
        return eventService.getList();
    }

    public void add(Event.Type type, String info, boolean success) {
        eventService.add(loginController.getCurrentUser(), type, info, success);
    }

    @PostConstruct
    public void init() {
        resourceBundle = ResourceBundle.getBundle("ulygroup.msg");
        LOGGER.debug("resourceBundle: " + resourceBundle);
    }

    // Return the key of the event type in the resource bundle
    public String getTypeMsgKey(Event e) {
        return "event.type." + e.getType().toString();
    }
}
