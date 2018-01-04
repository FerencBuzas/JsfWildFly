package ulygroup.service;

import org.jboss.logging.Logger;
import ulygroup.model.Request;

import javax.ejb.Stateless;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.function.Consumer;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class RequestService {

    private static final Logger LOGGER = Logger.getLogger(RequestService.class); 

    @Inject
    private EntityManager em;

    @Inject
    private Event<Request> requestEventSrc;
    
    @Inject
    private EventService eventService;
    
    public void persist(Request request) {
        LOGGER.debug("persist(): " + request);
        if (request.getId() != 0) {
            LOGGER.warn("Request.id (" + request.getId() + ") != 0");
        }
        
        try {
            em.persist(request);
            eventService.add("new request: " + request.getSum(), true);
        }
        catch (Exception e) {
            eventService.add("new request: " + request.getSum(), false);
        }
        requestEventSrc.fire(request);  // caught in RequestListProducer
    }

    public void persistMod(long id, Consumer<Request> changeSomeField) {
        LOGGER.debug("persistMod() id=" + id);
        
        Request tmpReq = findById(id);
        changeSomeField.accept(tmpReq);
        try {
            em.persist(tmpReq);
            eventService.add("modify request: " + tmpReq.getSum(), true);
        }
        catch (Exception e) {
            eventService.add("modify request: " + tmpReq.getSum(), false);
        }         
        requestEventSrc.fire(tmpReq);
    }

    public void remove(long id) {
        LOGGER.info("Removing request id=" + id);
        Request r = em.find(Request.class, id);
        try {
            em.remove(r);
            eventService.add("remove request: " + r.getSum(), true);
        }
        catch (Exception e) {
            eventService.add("remove request: " + r.getSum(), false);
        }
        requestEventSrc.fire(r);
    }
    
    public Request findById(Long id) {
        LOGGER.debug("findById() id=" + id);
        return em.find(Request.class, id);
    }
}
