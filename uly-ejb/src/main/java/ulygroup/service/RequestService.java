package ulygroup.service;

import org.jboss.logging.Logger;
import ulygroup.data.RequestRepository;
import ulygroup.model.Request;
import ulygroup.model.Event.Type;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.function.Consumer;

// The @Stateless annotation eliminates the need for manual transaction demarcation
@Stateless
public class RequestService {

    private static final Logger LOGGER = Logger.getLogger(RequestService.class); 

    @Inject
    private EntityManager em;

    @Inject
    private RequestRepository requestRepository;
    
    @Inject
    private javax.enterprise.event.Event<Request> requestEventSrc;
    
    @Inject
    private EventService eventService;
    
    public void persist(Request request) {
        LOGGER.debug("persist(): " + request);
        if (request.getId() != 0) {
            LOGGER.warn("Request.id (" + request.getId() + ") != 0");
        }
        
        try {
            em.persist(request);
            eventService.add(Type.Request, request.getSumStr(), true);
        }
        catch (Exception e) {
            eventService.add(Type.Request, request.getSumStr(), false);
        }
        requestEventSrc.fire(request);  // caught in RequestListProducer
    }

    public void persistMod(long id, Consumer<Request> changeSomeField) {
        LOGGER.debug("persistMod() id=" + id);
        
        Request tmpReq = findById(id);
        changeSomeField.accept(tmpReq);
        try {
            em.persist(tmpReq);
            eventService.add(Type.Modify, tmpReq.getSumStr(), true);
        }
        catch (Exception e) {
            eventService.add(Type.Modify, tmpReq.getSumStr(), false);
        }         
        requestEventSrc.fire(tmpReq);
    }

    // Accept all request that are in 'requested' state
    public void acceptAll() {
        LOGGER.debug("acceptAll()");

        List<Request> reqList = requestRepository.findAll(RequestRepository.Filter.Requested, null);

        reqList.forEach(r -> {
            r.setState(Request.State.Accepted);
            em.persist(r);
        });
    }


    public void remove(long id) {
        LOGGER.info("Removing request id=" + id);
        Request r = em.find(Request.class, id);
        try {
            em.remove(r);
            eventService.add(Type.Delete, r.getSumStr(), true);
        }
        catch (Exception e) {
            eventService.add(Type.Delete, r.getSumStr(), false);
        }
        requestEventSrc.fire(r);
    }
    
    public Request findById(Long id) {
        LOGGER.debug("findById() id=" + id);
        return em.find(Request.class, id);
    }
}
