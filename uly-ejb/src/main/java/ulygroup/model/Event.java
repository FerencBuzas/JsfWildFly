package ulygroup.model;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Column; 
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@XmlRootElement
@Table(name = "Event" )
public class Event {

    @GeneratedValue
    @Id
    private long id;

    private Date date;
    
    @ManyToOne
    private User user;
    
    @Column(name = "event_name")
    private String eventName;
    private boolean success;

    public Event() {
    }
    
    public Event(long id, Date date, User user, String eventName, boolean success) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.eventName = eventName;
        this.success = success;
    }

    // --- setters, getters ---
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
