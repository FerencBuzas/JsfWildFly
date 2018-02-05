package ulygroup.model;

import org.jboss.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(name = "Myuser", schema="uly")
public class User implements Serializable {
    
    public enum Role { Admin, User }

    private static final Logger LOGGER = Logger.getLogger(User.class);

    @Id
    @Column(name = "login_name")
    private String loginName;

    @NotNull
    @Column(name = "full_name")
    private String fullName;
    
    @NotNull
    private String password;
    
    @Transient
    private Set<Role> roles = new HashSet<>();

    public User() {
    }
    
    public User(String loginName, String fullName, String password) {
        this.loginName = loginName;
        this.fullName = fullName;
        this.password = password;
    }

    public User(String loginName, String fullName, String password, Role role) {
        this(loginName, fullName, password);
        addRole(role);
    }

    // --- Getters, setters ---
    
    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
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

    public Collection<Role> getRoles() {
        return roles;
    }

    public String getRolesString() {
        return roles.stream().map(Role::toString).collect(Collectors.joining(","));
    }

    public void addRole(Role... roles) {
        for (Role role: roles) {
            this.roles.add(role);
        }
    }
    
    public boolean hasAdminRole() {
        LOGGER.trace("hasAdminRole() '"+getLoginName()+"' roles: " + roles.size());
        return roles.stream().map(Role::toString).anyMatch(s -> s.contains("Admin"));
    }

    @Override
    public String toString() {
        return String.format("User{name='%s', loginName='%s', roles='%s'}\n",
                fullName, loginName, getRolesString());
    }
}
