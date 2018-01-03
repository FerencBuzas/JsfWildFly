package ulygroup.model;

import org.jboss.logging.Logger;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
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
    
    public enum State { Requested, Accepted, Rejected }
    
    @GeneratedValue
    @Id
    private long   id;
    
    @ManyToOne(targetEntity = User.class, fetch=FetchType.EAGER)
    private User   user;
    
    private long   sum;
    
    @Enumerated(EnumType.STRING)
    private State state;

    public Request() {
        LOGGER.debug("Request()");
    }
    
    public Request(long id, User user, long sum, State state) {
        LOGGER.debug(String.format("Request() id=%d user=%s sum=%d", id, user.getLoginName(), sum));
        this.id = id;
        this.user = user;
        this.sum = sum;
        this.state = state;
    }

    public void submit() {
        LOGGER.debug("submit() sum=" + sum);

//        requestRepository.add(sum);
//        requestRepository.setEditing(false);
    }

    // --- getter, setter ---
    
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
        return state.toString();
    }

    public void setState(State state) {
        this.state = state;
    }

    public void setAccepted(boolean accepted) {
        this.state = accepted ? State.Accepted : State.Rejected; 
    }
    
    public boolean isRequested() { return state == State.Requested; }
    public boolean isAccepted() { return state == State.Accepted; }
    public boolean isRejected() { return state == State.Rejected; }
    
    @Override
    public String toString() {
        String userName = (user == null? "null" : user.getLoginName());
        return String.format("req[id=%d user=%s sum=%d]\n", id, userName, sum);
    }
}
