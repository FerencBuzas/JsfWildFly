package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.LoginManager;
import ulygroup.data.RequestRepository;
import ulygroup.data.RequestRepository.Filter;
import ulygroup.model.Request;
import ulygroup.model.User;
import ulygroup.service.RequestService;
import ulygroup.util.FeriUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

@Named("requestController")
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

    private List<Request> list = Arrays.asList();

    private boolean editing;
    private Request ediReq;   // the currently edited request
    
    private Filter filter = Filter.All;   // for the admin screen

    @Produces
    @Named
    public Request getEdiReq() { return ediReq; }
    
    @PostConstruct
    public void initNewMember() {
        LOGGER.debug("@PostConstruct");
        ediReq = new Request(0, loginManager.getCurrentUser(), 0, Request.State.Requested);
        User filterUser = loginManager.isAdminLoggedIn() ? null : loginManager.getCurrentUser();
        list = requestRepository.findAll(filter, filterUser);
    }

    // The [New] button has been pressed
    public String newReq(AjaxBehaviorEvent event) {
        LOGGER.debug("newReq()");

        ediReq.setId(0);
        ediReq.setState(Request.State.Requested);
        ediReq.setSum(0);
        return setEditing(true);
    }

    // The [Mod] button has been pressed; the Id of the Request is put into the label
    public String modify(AjaxBehaviorEvent event) {
        
        HtmlCommandButton cb = (HtmlCommandButton) event.getSource();
        long id = Long.valueOf(cb.getLabel());
        LOGGER.debug("### modify event=" + event + "  label=" + cb.getLabel() + " id=" + id);
 
        ediReq = requestRepository.findById(id);
        return setEditing(true);
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
    
    public List<Request> getList() {
        return list;
    }

    public Request findById(long id) {
        return requestRepository.findById(id);
    }
    
    // The admin user has pressed 'Accept all'; accept those not accepted.
    public String acceptAll() {
        LOGGER.debug("acceptAll()");
        requestService.acceptAll();
        return "";
    }

    public int countJustRequested() {
        return 1;
    } 
    
    public Object submit() {
        LOGGER.debug("## persist() ediReq=" + ediReq);
        requestService.persist(ediReq);
        return setEditing(false);
    }
    
    // Admin only: selected whether to see All, Accepted or Requested
    public void filterChanged(ValueChangeEvent event) {
        if (event.getPhaseId() != PhaseId.INVOKE_APPLICATION) {
            LOGGER.debug("  [filterChanged(): passing event to INVOKE_APPLICATION phase]");
            event.setPhaseId(PhaseId.INVOKE_APPLICATION);
            event.queue();
            return;
        }

        LOGGER.debug("filterChanged() filter=" + filter);
        list = requestRepository.findAll(filter, null);   // admin can see all

        FeriUtil.refreshPage();
    }

    public boolean isEditing() { return editing; }
    public String setEditing(boolean editing) { this.editing = editing; return ""; }

    public String getFilter() { return filter.toString(); }
    public void setFilter(String filter) {
        LOGGER.debug("setFilter(): " + filter);
        this.filter = Filter.valueOf(filter);
    }
}
