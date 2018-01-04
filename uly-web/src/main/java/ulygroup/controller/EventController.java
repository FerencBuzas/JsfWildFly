package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.model.Event;
import ulygroup.service.EventService;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;

@Named
@SessionScoped
public class EventController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EventController.class);
    
    @Inject
    private EventService eventService;

    public List<Event> getList() {
        return eventService.getList();
    }

    public void add(String eventName, boolean success) {
        eventService.add(eventName, success);
    }

}
