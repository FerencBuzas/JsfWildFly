package ulygroup.service;

import org.jboss.logging.Logger;
import ulygroup.model.Event;
import ulygroup.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Date;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class EventService {

    private static final Logger LOGGER = Logger.getLogger(EventService.class);

    @Inject
    private EntityManager em;

    @Inject
    private javax.enterprise.event.Event<Event> eventSrc;

    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("@PostConstruct");
    }

    // Add an event to the repo, then notify observers
    public void add(User user, Event.Type type, String info, boolean success) {

        Event event = new Event(0, new Date(), user, type, info, success);
        LOGGER.debug("add(): " + event);

        em.persist(event);
        eventSrc.fire(event); 
    }
}
