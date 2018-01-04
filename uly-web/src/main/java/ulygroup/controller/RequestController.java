package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.LoginManager;
import ulygroup.data.RequestRepository;
import ulygroup.model.Request;
import ulygroup.service.RequestService;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;

@Named
@SessionScoped
public class RequestController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RequestController.class);

    @Inject
    private RequestRepository requestRepository;

    @Inject
    private RequestService requestService;

    @Inject
    private LoginManager loginManager;

    @Inject
    private FacesContext facesContext;

    private boolean editing;

    private Request ediReq;   // the currently edited request

    @Produces
    @Named
    public Request getEdiReq() { return ediReq; }
    
    @PostConstruct
    public void initNewMember() {
        ediReq = new Request(0, loginManager.getCurrentUser(), 0, Request.State.Requested);
    }

    // The [New] button has been pressed
    public String newReq(AjaxBehaviorEvent event) {
        LOGGER.debug("newReq()");

        ediReq.setId(0);
        ediReq.setState(Request.State.Requested);
        ediReq.setSum(0);
        setEditing(true);
        return "";
    }

    // The [Mod] button has been pressed; the Id of the Request is put into the label
    public String modify(AjaxBehaviorEvent event) {
        
        HtmlCommandButton cb = (HtmlCommandButton) event.getSource();
        long id = Long.valueOf(cb.getLabel());
        LOGGER.debug("### modify event=" + event + "  label=" + cb.getLabel() + " id=" + id);
 
        ediReq = requestRepository.findById(id);
        setEditing(true);
        
        return "";
    }

    // Admin: Accept one request
    public String accept(long id) {
        LOGGER.debug("accept() id=" + id);
        requestService.persistMod(id, r -> r.setAccepted(true) );
        return "";
    }

    // Admin: Reject one request
    public String reject(long id) {
        LOGGER.debug("reject() id=" + id);
        requestService.persistMod(id, r -> r.setAccepted(false) );
        return "";
    }

    public String delete(long id) {
        LOGGER.debug("delete() id=" + id);
        requestService.remove(id);
        return "";
    }

    public Request findById(long id) {
        return requestRepository.findById(id);
    }
    
    // The admin user has pressed 'Accept all'; accept those not accepted.
    public String acceptAll() {
        LOGGER.debug("acceptAll()");
        
        LOGGER.debug("TODO: acceptAll()");
//        list.stream()
//                .filter(Request::isRequested)
//                .forEach(r -> r.setState(Request.State.Accepted));
        return "";
    }

    public Object submit() {
        LOGGER.debug("## persist() ediReq=" + ediReq);
        requestService.persist(ediReq);
        setEditing(false);
        return "";
    }
    
    public boolean isEditing() { return editing; }
    public Object setEditing(boolean editing) { this.editing = editing; return ""; }
}
