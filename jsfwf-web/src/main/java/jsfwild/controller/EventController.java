package jsfwf.controller;

import org.jboss.logging.Logger;
import jsfwf.data.EventRepository;
import jsfwf.model.Event;
import jsfwf.service.EventService;
import jsfwf.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.enterprise.event.Observes;
import javax.enterprise.event.Reception;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;

@Named("eventController")
@SessionScoped
public class EventController implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EventController.class);
    
    @Inject
    private EventRepository eventRepository;

    @Inject
    private EventService eventService;

    @Inject
    private FacesUtil facesUtil;
    
    @Inject
    private LoginController loginController;
    
    private ResourceBundle resourceBundle;

    private List<Event> list = Collections.emptyList();

    @PostConstruct
    public void init() {
        resourceBundle = ResourceBundle.getBundle("jsfwf.msg");
        LOGGER.debug("resourceBundle: " + resourceBundle);
        refreshList();
    }

    public List<Event> getList() {
        return list;
    }
    
    public void refreshList() {
        LOGGER.debug("refreshList()");
        list = eventRepository.getList();
    }
    
    public void refreshList(AjaxBehaviorEvent event) {
        refreshList();
        facesUtil.refreshPage();
    }
    
    public void add(Event.Type type, String info, boolean success) {
        eventService.add(Event.create(loginController.getCurrentUser(), type, info, success));
        refreshList();
    }

    // Return the key of the event type in the resource bundle
    public String getTypeMsgKey(Event e) {
        return "event.type." + e.getType().toString();
    }

    public void onEventListChanged(@Observes(notifyObserver=Reception.ALWAYS) final Event event) {
        LOGGER.info("onEventListChanged()");
        refreshList();
    }


    // button [Generate test data] calls this method to create some events
    public void generateTestData(AjaxBehaviorEvent event) {
        int n = 5;
        LOGGER.info("generateTestData() n=" + n);
        for (int i = 0; i < n; ++i) {
            Event.Type type = ((i % 3) <= 1 ? Event.Type.Request : Event.Type.Accept);
            String info = "info_gen_" + i;
            eventService.add(Event.create(loginController.getCurrentUser(), type, info, true));
        }
        refreshList();
    }
}
