package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Request;

import javax.annotation.PostConstruct;
import javax.enterprise.context.RequestScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.enterprise.inject.Produces;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

@RequestScoped
public class RequestListProducer {

    private static final Logger LOGGER = Logger.getLogger(RequestListProducer.class);
    
    @Inject
    private RequestRepository requestRepository;

    private List<Request> requestList;

    // @Named provides access via the EL variable name "requestList" in the UI (e.g. Facelets view)
    @Produces
    @Named
    public List<Request> getRequestList() {
        LOGGER.trace("getRequestList()");
        return requestList;
    }

    public void onRequestListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Request request) {
        LOGGER.debug("onRequestListChanged()");
        retrieveAllRequests();
    }

    @PostConstruct
    public void retrieveAllRequests() {
        LOGGER.debug("retrieveAllRequests()");
        requestList = requestRepository.findAll();
    }
}
