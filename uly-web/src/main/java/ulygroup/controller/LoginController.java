package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.LoginManager;
import ulygroup.model.User;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

@Named("loginController")
@ManagedBean
@Singleton    // TODO: make it multiuser
public class LoginController implements Serializable {

    private static final long serialVersionUID = -2879890213L;

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    public static final String HOME_PAGE_REDIRECT = "index.xhtml?faces-redirect=true";
    public static final String LOGOUT_PAGE_REDIRECT = "login.xhtml?faces-redirect=true";

    @Inject
    private LoginManager loginManager;

    private User ediUser;
    
    public User getEdiUser() { return ediUser; }

    @PostConstruct
    public void initNewMember() {
        if (ediUser == null) {
            LOGGER.info("@PostConstruct, creating ediUser");
            ediUser = new User(0, null, null, null, User.Role.User);
        }
    }

    public String login() {

        LOGGER.info("################################################");
        LOGGER.info("login() name=" + ediUser.getLoginName() + " password=" + ediUser.getPassword());
        LOGGER.info("################################################");

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
       
        try {
            request.login(ediUser.getLoginName(), ediUser.getPassword());
            LOGGER.info("## Survived request.login, redirecting to " + HOME_PAGE_REDIRECT);
            return HOME_PAGE_REDIRECT;
        
        }
        catch (ServletException e) {
            LOGGER.info("### login failed :(");
            context.addMessage("Ajaj", new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed", "Invalid or unknown credentials."));
            return null;
        }
        
//        if (loginManager.login(loginName, password)) {
//            return HOME_PAGE_REDIRECT;
//        } else {
//            context.addMessage(null, new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed", "Invalid or unknown credentials."));
//            return null;
//        }
    }

    public String logout() {
        String identifier = ediUser.getLoginName();

        LOGGER.debug("invalidating session for " + identifier);
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        try {
            request.logout();
        } catch (ServletException e) {
            LOGGER.info("logout() error");
        }
        externalContext.invalidateSession();

        try {
            externalContext.redirect(LOGOUT_PAGE_REDIRECT);
        } catch (IOException e) {
            LOGGER.info("Could not redirect to index.xhtml");
        }

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

    public User getCurrentUser() { return loginManager.getCurrentUser(); }
    
    // For JSF
    public String getCurrentUserName() {
        LOGGER.info("#### getCurrentUserName ");
        User cu = getCurrentUser();
        return (cu != null ? cu.getLoginName() : "");
    }
    // Since the current user and 'logout' are in a combo box whose value can change
    public void setCurrentUserName(String name) {
        LOGGER.debug("dummy setCurrentUserName() name=" + name);
    }
}
