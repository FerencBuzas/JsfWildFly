package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Event;
import ulygroup.model.Request;
import ulygroup.model.User;

import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

@SessionScoped
public class TestData implements Serializable {
    
    
    private static final Logger LOGGER = Logger.getLogger(TestData.class);
    
    private User feri  = new User(1, "Feri", "feri", "feri", User.ROLE_ADMIN);
    private User bea   = new User(2, "Bea", "bea", "bea", User.ROLE_USER);
    private User marci = new User(3, "Marci", "marci", "marci", User.ROLE_USER);
    
    List<User> createUserTestData() {

        LOGGER.debug("createUserTestData()");

        User[] users = {feri, bea, marci};

        return new CopyOnWriteArrayList<>(users);
    }
    
    List<Request> createRequestTestData() {

        LOGGER.debug("createRequestTestData()");

        Request[] requests = {
                new Request(1, feri,  400000, Request.STATE_ACCEPTED),
                new Request(2, feri,  400000, Request.STATE_REQUESTED),
                new Request(3, bea,   200000, Request.STATE_ACCEPTED),
                new Request(4, bea,   200000, Request.STATE_REQUESTED),
                new Request(5, marci, 100000, Request.STATE_REJECTED),
                new Request(6, marci, 100000, Request.STATE_REQUESTED)
        };

        return new CopyOnWriteArrayList<>(requests);
    }

    List<Event> createEventTestData() {

        LOGGER.debug("createEventTestData()");

        Event[] events = {
                new Event(1, new Date(), feri, "feriEvent1", true),
                new Event(2, new Date(), feri, "feriEvent2", false),
                new Event(3, new Date(), bea, "BeaEvent1", true),
                new Event(4, new Date(), bea, "BeaEvent2", false),
                new Event(5, new Date(), marci, "MarciEvent1", true),
                new Event(6, new Date(), marci, "MarciEvent2", false)
        };

        return new CopyOnWriteArrayList<>(events);
    }
}
