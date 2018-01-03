package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.LoginManager;
import ulygroup.data.RequestRepository;
import ulygroup.model.Request;

import javax.enterprise.inject.Model;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@Model
public class RequestController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RequestController.class);

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private LoginManager loginManager;

    private boolean editing;
    private long editingId;

    public void add(long sum) {
        LOGGER.debug("add() sum="+sum);
        
        Request request;
        if (editingId == 0) {
            request = new Request(0, loginManager.getCurrentUser(), sum,  Request.State.Requested);
        }
        else {
            request = findById(editingId);
            request.setSum(sum);
            editingId = 0;
        }
        requestRepository.persist(request);
    }

    public String newReq(AjaxBehaviorEvent event) {
        LOGGER.debug("newReq()");

        setEditing(true);
        editingId = 0;
        return "";
    }

    // The [Mod] button has been pressed
    public String modify(AjaxBehaviorEvent event) {
        editingId = getRowId();
        setEditing(true);
        return "";
    }

    // Admin: Accept one request
    public String accept(long id) {
        Request request = findById(id);
        request.setAccepted(true);
        return "";
    }

    // Admin: Reject one request
    public String reject(long id) {
        Request request = findById(id);
        request.setState(Request.State.Rejected);
        return "";
    }

    private long getRowId() {
        FacesContext context = FacesContext.getCurrentInstance();
        // long id = context.getApplication().evaluateExpressionGet(context, "#{r.id}", Long.class);
        long id = Long.valueOf(context.getExternalContext().getRequestParameterMap().get("reqId"));
        LOGGER.debug("id=" + id);
        return id;
    }

    public String delete(long id) {
        Request request = findById(id);
        requestRepository.remove(request);
        return "";
    }

    public List<Request> getList() {
        return requestRepository.findAll();
    }
    
    public Request findById(long id) {
        return requestRepository.findById(id);
    }
    
    // The admin user has pressed 'Accept all'; accept those not accepted.
    public String acceptAll() {
        LOGGER.debug("acceptAll()");
        
        LOGGER.info("TODO: acceptAll()");
//        list.stream()
//                .filter(Request::isRequested)
//                .forEach(r -> r.setState(Request.State.Accepted));
        return "";
    }

    public boolean isEditing() { return editing; }
    public void setEditing(boolean editing) { this.editing = editing; }
}
