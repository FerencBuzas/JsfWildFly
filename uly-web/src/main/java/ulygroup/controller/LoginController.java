package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.LoginManager;
import ulygroup.model.User;

import javax.enterprise.inject.Model;
import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import java.io.Serializable;

//@ApplicationScoped
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
    private String userPassword;

    public String login() {

        LOGGER.info("login() name=" + loginName + " password=" + userPassword);
                
        if (loginManager.login(loginName, userPassword)) {
            return HOME_PAGE_REDIRECT;
        } 
        else {
            FacesContext.getCurrentInstance().addMessage(null,
                    new FacesMessage(FacesMessage.SEVERITY_WARN,
                    "Login failed", "Invalid or unknown credentials."));
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

    public String getUserPassword() { return userPassword; }
    public void setUserPassword(String userPassword) { this.userPassword = userPassword; }
    
    public User getCurrentUser() { return loginManager.getCurrentUser(); }

    // do not provide a setter for currentUser!

    // for DI
    public UserController getUserController() { return userController; }
    public void setUserController(UserController userController) { this.userController = userController; }
}
