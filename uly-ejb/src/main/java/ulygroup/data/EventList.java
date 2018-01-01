package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Event;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@SessionScoped
public class EventList implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EventList.class);
    
    @Inject
    private LoginManager loginManager;
    
    @Inject
    private TestData testData;

    private long lastId;
    private List<Event> list;

    public void add(String eventName, boolean success) {
        LOGGER.debug("add() =");

        createTestData();
        list.add(new Event(++lastId, new Date(), loginManager.getCurrentUser(),
                eventName, success));
    }

    public String delete(long id) {
        LOGGER.debug("delete... id=" + id);
        return "";
    }

    
    // --- set, get ---
    
    public List<Event> getList() { createTestData(); return list; }

    // for DI
    public void setLoginManager(LoginManager loginManager) {this.loginManager = loginManager;}
    public void setTestData(TestData testData) { this.testData = testData; }

    private void createTestData() {
        if (list == null) {
            list = testData.createEventTestData();
        }
    }
}
