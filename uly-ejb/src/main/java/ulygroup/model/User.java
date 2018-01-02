package ulygroup.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

@Entity
@Table(name = "myuser" /*, uniqueConstraints = @UniqueConstraint(columnNames = "email")*/ )
public class User {

    public static final String ROLE_ADMIN = "A";
    public static final String ROLE_USER = "U";

    @Id
    @GeneratedValue
    private long id;

    @NotNull
    private String name = "";
    
    @Column(name = "login_name")
    private String loginName = "";
    
    @NotNull
    private String password = "";
    
    @NotNull
    private String role = ROLE_USER;
    
    
    public User() {
    }

    public User(long id, String name, String loginName, String password, String role) {
        this.id = id;
        this.name = name;
        this.loginName = loginName;
        this.password = password;
        this.role = role;
    }
    
    // --- Getters, setters ---


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
