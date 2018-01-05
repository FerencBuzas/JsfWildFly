package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.LoginManager;
import ulygroup.model.User;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

@Model
public class LoginController implements Serializable {

    private static final long serialVersionUID = -9107952969237527819L;

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    public static final String HOME_PAGE_REDIRECT = "index.xhtml?faces-redirect=true";
    public static final String LOGOUT_PAGE_REDIRECT = "login.xhtml?faces-redirect=true";

    @Inject
    private LoginManager loginManager;

    @Inject
    private UserController userController;

    private String loginName;
    private String password;

    public String login() {

        LOGGER.info("login() name=" + loginName + " password=" + password);

        if (loginManager.login(loginName, password)) {
            return HOME_PAGE_REDIRECT;
        } else {
            FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed", "Invalid or unknown credentials."));
            return null;
        }
    }

    public String logout() {
        String identifier = loginName;
        loginManager.logout();

        LOGGER.debug("invalidating session for " + identifier);
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();

        LOGGER.info("logout successful for " + identifier);
        return LOGOUT_PAGE_REDIRECT;
    }

    public boolean isLoggedIn() {
        return loginManager.isLoggedIn();
    }

    public boolean isAdminLoggedIn() {
        return loginManager.isAdminLoggedIn();
    }

    public String ifLoggedInForwardHome() {
        return isLoggedIn() ? HOME_PAGE_REDIRECT : null;
    }

    public String getLoginName() { return loginName; }
    public void setLoginName(String loginName) { this.loginName = loginName; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }
    
    public User getCurrentUser() { return loginManager.getCurrentUser(); }
    
    // For JSF
    public String getCurrentUserName() {
        User cu = getCurrentUser();
        return (cu != null ? cu.getLoginName() : "");
    }
    // Since the current user and 'logout' are in a combo box whose value can change
    public void setCurrentUserName(String name) {
        LOGGER.debug("dummy setCurrentUserName() name=" + name);
    }
}
