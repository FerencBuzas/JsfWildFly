package ulygroup.model;

import org.jboss.logging.Logger;
import ulygroup.data.RequestList;

import javax.inject.Inject;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;

@Entity
@XmlRootElement
@Table(name = "Request")
public class Request implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(Request.class);
    
    public static final String STATE_REQUESTED = "R";
    public static final String STATE_ACCEPTED  = "A";
    public static final String STATE_REJECTED  = "X";
    
    @Inject
    private RequestList requestList;
    
    @GeneratedValue
    @Id
    private long   id;
    
    @ManyToOne
    private User   user;
    
    private long   sum;
    private String state;

    public Request() {
        LOGGER.debug("Request()");
    }
    
    public Request(long id, User user, long sum, String state) {
        LOGGER.debug(String.format("Request() id=%d user=%s sum=%d", id, user.getLoginName(), sum));
        this.id = id;
        this.user = user;
        this.sum = sum;
        this.state = state;
    }

    public void submit() {
        LOGGER.debug("submit() sum=" + sum);

        requestList.add(sum);
        requestList.setEditing(false);
    }

    // --- getter, setter ---
    
    // needed for Dependency Injection
    public RequestList getRequestList() {
        return requestList;
    }

    public void setRequestList(RequestList requestList) {
        this.requestList = requestList;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
    
    public void setAccepted(boolean accepted) {
        this.state = accepted ? STATE_ACCEPTED : STATE_REJECTED; 
    }
    
    public boolean isRequested() { return state.equals(STATE_REQUESTED); }
    public boolean isAccepted() { return state.equals(STATE_ACCEPTED); }
    public boolean isRejected() { return state.equals(STATE_REJECTED); }
    
    @Override
    public String toString() {
        String userName = (user == null? "null" : user.getLoginName());
        return String.format("req[id=%d user=%s sum=%d", id, userName, sum);
    }
}
