package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Request;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;

@SessionScoped
public class RequestList implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(RequestList.class);

    @Inject
    private LoginManager loginManager;

    @Inject
    private TestData testData;
    
    private long lastId;
    private List<Request> list;

    private boolean editing;
    private long editingId;      // 0: new
    
    public RequestList() {
        LOGGER.debug("RequestList()");
    }
    
    public void add(long sum) {
        LOGGER.debug("add() sum="+sum);
        
        createTestData();
        if (editingId == 0) {
            list.add(new Request(++lastId, loginManager.getCurrentUser(), sum,  Request.STATE_REQUESTED));
        }
        else {
            Request request = findById(editingId);
            request.setSum(sum);
            editingId = 0;
        }
    }

    public String newReq(/* AjaxBehaviorEvent event */) {
        LOGGER.debug("newReq()");

        setEditing(true);
        editingId = 0;
        return "";
    }

    // The [Mod] button has been pressed
    public String modify(/* AjaxBehaviorEvent event */) {
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
        request.setState(Request.STATE_REJECTED);
        return "";
    }

    private long getRowId() {
//        FacesContext context = FacesContext.getCurrentInstance();
//        long id = context.getApplication().evaluateExpressionGet(context, "#{r.id}", Long.class);
//        LOGGER.debug("id=" + id);
        return 0;  // TODO
    }
    
    public String delete(long id) {
        Request r = findById(id);
        list.remove(r);
        return "";
    }
    
    // The admin user has pressed 'Accept all'; accept those not accepted.
    public String acceptAll() {
        LOGGER.debug("acceptAll()");
        list.stream()
                .filter(Request::isRequested)
                .forEach(r -> r.setState(Request.STATE_ACCEPTED));
        return "";
    }

    Request findById(long id) {
        return list.stream().filter(r -> r.getId() == id).findFirst().get();
    }

    public long countJustRequested() {
        return list.stream().filter(Request::isRequested).count();
    }

    public void writeValues() {
        list.forEach(req -> LOGGER.debug("req: " + req));
    }

    // --- set, get ---
    
    public List<Request> getList() {
        createTestData();
        return list;
    }

    public boolean isEditing() {
        return editing;
    }
    public String setEditing(boolean e) {
        LOGGER.debug("setEditing() " + e);
        editing = e;
        return "";  // stay on the page, if called from an action
    }

    public long getEditingId() { return editingId; }
    public void setEditingId(long editingId) { this.editingId = editingId; }

    public LoginManager getLoginManager() { return loginManager; }
    public void setLoginManager(LoginManager loginManager) {this.loginManager = loginManager; }
    
    public void setTestData(TestData testData) { this.testData = testData;  }

    // --- data ---
    
    private void createTestData() {
        if (list == null) {
            list = testData.createRequestTestData();
        }
    }
}
