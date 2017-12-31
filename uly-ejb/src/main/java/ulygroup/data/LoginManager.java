package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.User;

import javax.inject.Inject;
import java.io.Serializable;
import java.util.Optional;

public class LoginManager implements Serializable {

    private static final long serialVersionUID = -9107952969237527819L;

    private static final Logger LOGGER = Logger.getLogger(LoginManager.class);

    public static final String HOME_PAGE_REDIRECT = "main.xhtml?faces-redirect=true";
    public static final String LOGOUT_PAGE_REDIRECT = "login.xhtml?faces-redirect=true";

    @Inject
    private UserList userList;
    
    private String loginName;
    private String userPassword;

    private User currentUser;   // logged in

    public String login() {

        Optional<User> user = find(loginName);
        
        if (user.isPresent()) {
            currentUser = user.get();
            LOGGER.debug("login successful for " + loginName);
            return HOME_PAGE_REDIRECT;
        } 
        else {
            LOGGER.info("login failed for " + loginName);
//            FacesContext.getCurrentInstance().addMessage(null,
//                    new FacesMessage(FacesMessage.SEVERITY_WARN,
//                    "Login failed", "Invalid or unknown credentials."));
            return null;
        }
    }

    public String logout() {
        String identifier = loginName;

        LOGGER.debug("invalidating session for " + identifier);
//        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        LOGGER.info("logout successful for " + identifier);
        return LOGOUT_PAGE_REDIRECT;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdminLoggedIn() {
        return currentUser != null && currentUser.getRole().equals(User.ROLE_ADMIN);
    }

    public String isLoggedInForwardHome() {
        return isLoggedIn() ? HOME_PAGE_REDIRECT : null;
    }

    private Optional<User> find(String loginName) {
        
        return userList.find(loginName);
    }

    public String getLoginName() { return loginName; }
    public void setLoginName(String loginName) { this.loginName = loginName; }

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
    
    public User getCurrentUser() { return currentUser; }

    // do not provide a setter for currentUser!

    // for DI
    public UserList getUserList() { return userList; }
    public void setUserList(UserList userList) { this.userList = userList; }
}
