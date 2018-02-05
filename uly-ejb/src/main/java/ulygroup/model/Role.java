package ulygroup.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Objects;

@Entity
@Table(name = "Myrole", schema="uly")
public class Role implements Serializable {
    
    @Id
    @ManyToOne(targetEntity = User.class)
    @JoinColumn(name="princ_id")
    private User user;

    @Id
    @Column(name = "role_name")
    private String roleName;
    
    public Role() {
    }
    
    public Role(User user, String roleName) {

        this.user = user;
        this.roleName = roleName;
    }

    // --- Getters, setters ---

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Role))
            return false;
        Role role = (Role) o;
        return Objects.equals(user, role.user) && Objects.equals(roleName, role.roleName);
    }

    @Override
    public int hashCode() {

        return Objects.hash(user, roleName);
    }

    @Override
    public String toString() {
        return "Role{" + "user='" + user.getLoginName() + "', roleName='" + roleName + "'}";
    }
}
