package ulygroup.service;

import org.jboss.logging.Logger;
import ulygroup.data.RequestRepository;
import ulygroup.model.Event.Type;
import ulygroup.model.Request;

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
            eventService.add(request.getUser(), Type.Request, request.getSumStr(), true);
        }
        catch (Exception e) {
            eventService.add(request.getUser(), Type.Request, request.getSumStr(), false);
        }
        requestEventSrc.fire(request);  // caught in RequestListProducer
    }

    public void persistMod(long id, Consumer<Request> changeSomeField, Type eventType) {
        LOGGER.debug("persistMod() id=" + id);

        Request tmpReq = findById(id);
        changeSomeField.accept(tmpReq);
        try {
            em.persist(tmpReq);
            eventService.add(tmpReq.getUser(), eventType, tmpReq.getSumStr(), true);
        }
        catch (Exception e) {
            eventService.add(tmpReq.getUser(), eventType, tmpReq.getSumStr(), false);
        }         
        requestEventSrc.fire(tmpReq);
    }

    // Accept all request that are in 'requested' state
    public void acceptAll() {
        LOGGER.debug("acceptAll()");

        List<Request> reqList = requestRepository.findAll(RequestRepository.Filter.Requested, null, "acceptAll");

        reqList.forEach(r -> {
            r.setState(Request.State.Accepted);
            em.persist(r);
            eventService.add(r.getUser(), Type.Accept, "All", true);
        });
    }


    public void remove(long id) {
        LOGGER.info("Removing request id=" + id);
        Request r = em.find(Request.class, id);
        try {
            em.remove(r);
            eventService.add(r.getUser(), Type.Delete, r.getSumStr(), true);
        }
        catch (Exception e) {
            eventService.add(r.getUser(), Type.Delete, r.getSumStr(), false);
        }
        requestEventSrc.fire(r);
    }
    
    public Request findById(Long id) {
        LOGGER.debug("findById() id=" + id);
        return em.find(Request.class, id);
    }
}
