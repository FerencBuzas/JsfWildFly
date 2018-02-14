package jsfwf.service;

import org.jboss.logging.Logger;
import jsfwf.data.RequestRepository;
import jsfwf.model.Event;
import jsfwf.model.Event.Type;
import jsfwf.model.Request;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;
import java.util.List;
import java.util.function.Consumer;

@Stateless 
public class RequestService implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RequestService.class); 
    
    @PersistenceContext
    private EntityManager em;

    @Inject
    private RequestRepository requestRepository;
    
    @Inject
    private javax.enterprise.event.Event<Request> requestEventSrc;
    
    @Inject
    private EventService eventService;

    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("@PostConstruct");
    }

    public void persist(Request request) {
        LOGGER.debug("persist(): " + request);
        if (request.getId() != 0) {
            LOGGER.warn("Request.id (" + request.getId() + ") != 0");
        }
        
        try {
            em.persist(request);
            eventService.add(em, Event.create(request.getUser(), Type.Request, request.getSumStr(), true));
        }
        catch (Exception e) {
            eventService.add(em, Event.create(request.getUser(), Type.Request, request.getSumStr(), false));
        }
        requestEventSrc.fire(request);  // caught in RequestListProducer
    }

    public void persistMod(long id, Consumer<Request> changeSomeField, Type eventType) {
        LOGGER.debug("persistMod() id=" + id);

        Request tmpReq = findById(id);
        changeSomeField.accept(tmpReq);
        try {
            em.merge(tmpReq);
            Event event = Event.create(tmpReq.getUser(), eventType, tmpReq.getSumStr(), true);
            em.persist(event);
        }
        catch (Exception e) {
            LOGGER.warn("## Exception: " + e.getMessage() + " Caused by: " + e.getCause().getMessage());
            eventService.add(Event.create(tmpReq.getUser(), eventType, tmpReq.getSumStr(), false));
        }         
        requestEventSrc.fire(tmpReq);
    }

    // Accept all request that are in 'requested' state
    public void acceptAll() {
        LOGGER.debug("acceptAll()");

        List<Request> reqList = requestRepository.findAll(RequestRepository.Filter.Requested, null, "acceptAll");

        reqList.forEach(r -> {
            r.setState(Request.State.Accepted);
            em.merge(r);   // maybe before setState?
            eventService.add(Event.create(r.getUser(), Type.Accept, "All", true));
        });
    }


    public void remove(long id) {
        LOGGER.info("Removing request id=" + id);
        Request r = em.find(Request.class, id);
        try {
            em.remove(r);
            eventService.add(Event.create(r.getUser(), Type.Delete, r.getSumStr(), true));
        }
        catch (Exception e) {
            eventService.add(Event.create(r.getUser(), Type.Delete, r.getSumStr(), false));
        }
        requestEventSrc.fire(r);
    }
    
    private Request findById(Long id) {
        LOGGER.debug("findById() id=" + id);
        return em.find(Request.class, id);
    }
}
