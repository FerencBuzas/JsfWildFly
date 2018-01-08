package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Request;
import ulygroup.model.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@SessionScoped
public class RequestListProducer implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RequestListProducer.class);
    
    @Inject
    private RequestRepository requestRepository;

    @Inject
    private LoginManager loginManager;
    
    private List<Request> requestList;

    public List<Request> getRequestList() {
        LOGGER.trace("getRequestList()");
        return requestList;
    }

    // Thought to be called after fire() in RequestService
    public void onRequestListChanged(@Observes(notifyObserver = Reception.IF_EXISTS) final Request request) {
        LOGGER.debug("onRequestListChanged()");
        retrieveAllRequests();
    }
    
    @PostConstruct
    public void retrieveAllRequests() {
        LOGGER.debug("@PostConstruct: retrieveAllRequests()");
        User filterUser = loginManager.isAdminLoggedIn() ? null : loginManager.getCurrentUser();
        requestList = requestRepository.findAll(RequestRepository.Filter.All, filterUser);
    }
}
