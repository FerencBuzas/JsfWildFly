package ulygroup.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;

@Entity
@XmlRootElement
@Table(name = "Event" )
public class Event {

    public enum Type { Request, Accept, Reject, Modify, Delete }
    
    @GeneratedValue
    @Id
    private long id;

    @Column(name = "date")
    private Date date;
    
    @ManyToOne
    private User user;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "info")
    private String info;

    private boolean success;

    public Event() {
    }
    
    public Event(long id, Date date, User user, Type type, String info, boolean success) {
        this.id = id;
        this.date = date;
        this.user = user;
        this.type = type;
        this.info = info;
        this.success = success;
    }

    // --- setters, getters ---
    
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getDate() { return date; }
    public void setDate(Date date) { this.date = date; }
    
    public User getUser() { return user;  }
    public void setUser(User user) { this.user = user; }
    
    public Type getType() {return type; }
    public void setType(Type type) {this.type = type; }

    public String getInfo() { return info; }
    public void setInfo(String info) { this.info = info; }

    public boolean isSuccess() { return success; }
    public void setSuccess(boolean success) { this.success = success; }
}
