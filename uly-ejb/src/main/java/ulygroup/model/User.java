package ulygroup.model;

import org.jboss.logging.Logger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity
@Table(name = "Myuser", schema="uly")
public class User implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(User.class);

    @Id
    @Column(name = "login_name")
    private String loginName;

    @NotNull
    @Column(name = "full_name")
    private String fullName;
    
    @NotNull
    private String password;
    
    @OneToMany(targetEntity=Role.class, fetch=FetchType.EAGER, mappedBy="user")
    private Collection<Role> roles;

    public User() {
    }
    
    public User(String loginName, String fullName, String password) {
        this.loginName = loginName;
        this.fullName = fullName;
        this.password = password;
        this.roles = new ArrayList<>();
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
        String result = "";
        for (Role role: roles) {
            if (result.length() > 0) {
                result += ", ";
            }
            result += role.getRoleName();
        }
        return result;
    }

    public void addRole(Role role) {
        this.roles.add(role);
    }
    
    public boolean hasAdminRole() {
        LOGGER.trace("hasAdminRole() '"+getLoginName()+"' roles: " + roles.size());
        for (Role role: roles) {
            if (role.getRoleName().toLowerCase().contains("admin")) {
                return true;
            }
        }
        return false;
    }

    @Override
    public String toString() {
        String roleString = "";
        for (Role role: roles) {
            roleString += role.getRoleName() + " ";
        }
        return String.format("User{fullName='%s', loginName='%s', password='%s', roles='%s'}\n", fullName, loginName, password, roleString);
    }
}
