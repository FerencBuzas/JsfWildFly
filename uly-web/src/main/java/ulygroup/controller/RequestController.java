package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.RequestRepository;
import ulygroup.data.RequestRepository.Filter;
import ulygroup.model.Event;
import ulygroup.model.Request;
import ulygroup.model.User;
import ulygroup.service.RequestService;
import ulygroup.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.inject.Produces;
import javax.faces.component.html.HtmlCommandButton;
import javax.faces.event.AjaxBehaviorEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.ValueChangeEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@Named("requestController")
@SessionScoped
public class RequestController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RequestController.class);

    @Inject
    private FacesUtil facesUtil;
    
    @Inject
    private RequestRepository requestRepository;

    @Inject
    private RequestService requestService;

    @Inject
    private LoginController loginController;

    private List<Request> list = Collections.emptyList();

    private boolean editing;
    private Request ediReq;   // the currently edited request

    private Filter filter = Filter.All;   // for the admin screen

    @Produces
    @Named
    public Request getEdiReq() { return ediReq; }
    
    @PostConstruct
    public void initNewMember() {
        LOGGER.debug("@PostConstruct");
        User user = loginController.getCurrentUser();
        ediReq = new Request(0, user, 0, Request.State.Requested);
        refreshList("initNewMember()", false);
    }

    private void refreshList(String from, boolean toRefreshPage) {
        User user = loginController.getCurrentUser();
        User filterUser = loginController.isAdminLoggedIn() ? null : user;
        list = new CopyOnWriteArrayList<>(requestRepository.findAll(filter, filterUser, from));
        if (toRefreshPage) {
            facesUtil.refreshPage();
        }
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
        requestService.persistMod(id, r -> r.setAccepted(true), Event.Type.Accept);
        refreshList("accept()", true);
        return "";
    }

    // Admin: Reject one request
    public String reject(long id) {
        LOGGER.debug("reject() id=" + id);
        requestService.persistMod(id, r -> r.setAccepted(false), Event.Type.Reject);
        refreshList("reject()", true);
        return "";
    }

    public String delete(long id) {
        LOGGER.debug("delete() id=" + id);
        requestService.remove(id);
        refreshList("delete()", true);
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
        refreshList("acceptAll()", true);
        return "";
    }

    public int countJustRequested() {
        return (int) list.stream().filter(r -> r.isRequested()).count();
    } 
    
    public Object submit() {
        LOGGER.debug("persist() ediReq=" + ediReq);
        if (ediReq.getId() == 0) {
            requestService.persist(ediReq);
        } else {
            requestService.persistMod(ediReq.getId(), r -> r.setSum(ediReq.getSum()), Event.Type.Modify);
        }
        refreshList("submit()", true);
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
        refreshList("filterChanged", true);
    }

    public boolean isEditing() { return editing; }
    public String setEditing(boolean editing) { this.editing = editing; return ""; }

    public String getFilter() { return filter.toString(); }
    public void setFilter(String filter) {
        LOGGER.debug("setFilter(): " + filter);
        this.filter = Filter.valueOf(filter);
    }
}
