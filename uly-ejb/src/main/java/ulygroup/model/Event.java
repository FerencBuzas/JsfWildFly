package ulygroup.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name = "Event", schema="uly")
public class Event {

    public enum Type { Request, Accept, Reject, Modify, Delete }
    
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Id
    private long id;

    @Column(name = "date")
    private Date date;

    @ManyToOne
    @JoinColumn(name="user_id")
    private User user;

    @Enumerated(EnumType.STRING)
    private Type type;

    @Column(name = "info")
    private String info;

    private boolean success;

    public Event() {
    }
    
    public static Event create(User user, Type type, String info, boolean success)
    {
        Event event = new Event();
        event.setId(0);
        event.setDate(new Date());
        event.setUser(user);
        event.setType(type);
        event.setInfo(info);
        event.setSuccess(success);
        return event;
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

    @Override
    public String toString() {
        return "Event{id=" + id 
                + ", date=" + date
                + ", user=" + (user == null ? "null" : user.getLoginName())
                + ", type=" + type
                + ", info='" + info
                + "', success=" + success + '}';
    }
}
