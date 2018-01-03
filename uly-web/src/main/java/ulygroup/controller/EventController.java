package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.EventRepository;
import ulygroup.data.LoginManager;
import ulygroup.model.Event;

import javax.enterprise.inject.Model;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Model
public class EventController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EventController.class);
    
    @Inject
    private LoginManager loginManager;
    
    @Inject
    private EventRepository eventRepository;

    private List<Event> list;
    
    public List<Event> getList() {
        list = eventRepository.findAll();
        LOGGER.info("## getList(): list=" + list);
        return list;
    }
    
    public Event findById(long id) {
        return eventRepository.findById(id);
    }
    

}
