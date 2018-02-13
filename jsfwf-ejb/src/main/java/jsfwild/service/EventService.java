package jsfwf.service;

import org.jboss.logging.Logger;
import jsfwf.model.Event;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
public class EventService {
    
    private static final Logger LOGGER = Logger.getLogger(EventService.class);

    @PersistenceContext
    private EntityManager em;

    @Inject
    private javax.enterprise.event.Event<Event> eventSrc;

    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("@PostConstruct");
    }

    // Add an event to the repo, then notify observers
    public void add(EntityManager emParam, Event event) {
        LOGGER.debug("add() " + event);
        
        emParam.persist(event);
        eventSrc.fire(event); 
    }
    
    public void add(Event event) {
        add(this.em, event);
    }
}
