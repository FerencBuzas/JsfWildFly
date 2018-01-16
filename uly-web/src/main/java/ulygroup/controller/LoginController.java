package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.UserRepository;
import ulygroup.model.User;
import ulygroup.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.Serializable;

@Named("loginController")
@SessionScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = -2879890213L;

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    private static final String HOME_PAGE_REDIRECT = "index.xhtml?faces-redirect=true";
    private static final String LOGOUT_PAGE_REDIRECT = "login.xhtml?faces-redirect=true";

    private static final String S_CURRENT_USER = "currentUser";
    private static final String S_EDIT_USER = "editUser";

    @Inject
    private FacesUtil facesUtil;
    
    @Inject
    private UserRepository userRepository;
    
    @PostConstruct
    public void postConstruct() {
        if (getEdiUser() == null) {
            LOGGER.info("@PostConstruct, creating ediUser");
            setEdiUser(new User(0, null, null, null, User.Role.User));
        }
    }

    public User getEdiUser() { return facesUtil.getSessionUser(S_EDIT_USER); }
    public void setEdiUser(User user) { facesUtil.setSessionUser(S_EDIT_USER, user); }

    public String login() {

        User eu = getEdiUser();
        LOGGER.info("## login() name=" + eu.getLoginName() + " password=" + eu.getPassword());

        FacesContext context = FacesContext.getCurrentInstance();
        HttpServletRequest request = (HttpServletRequest) context.getExternalContext().getRequest();
        LOGGER.debug("request authType=" + request.getAuthType() + " userPr: " + request.getUserPrincipal());

        try {
            request.login(eu.getLoginName(), eu.getPassword());
        }
        catch (ServletException e) {
            LOGGER.info("login failed");
            context.addMessage("Ajaj", new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed", e.toString()));
            return null;
        }
        
        // Login successful, set members, log
        User user = userRepository.findByLoginName(eu.getLoginName());
        setCurrentUser(user);
        facesUtil.logRequest(request);
        LOGGER.debug("isLoggedIn: " + getCurrentUser() + " isAdmin: " + isAdminLoggedIn());
        LOGGER.debug("login: OK, redirecting to " + HOME_PAGE_REDIRECT);
        return HOME_PAGE_REDIRECT;
    }
        
    public String logout() {
        String identifier = getEdiUser().getLoginName();

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

    public String ifLoggedInForwardHome() {
        return isLoggedIn() ? HOME_PAGE_REDIRECT : null;
    }
    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public boolean isAdminLoggedIn() {
        User currentUser = facesUtil.getSessionUser(S_CURRENT_USER);
        return currentUser != null && currentUser.getRole() == User.Role.Admin;
    }

    User getCurrentUser() {
        return facesUtil.getSessionUser(S_CURRENT_USER);
    }
    public void setCurrentUser(User user) {
        facesUtil.setSessionUser(S_CURRENT_USER, user);
    }

    // For JSF
    public String getCurrentUserName() {
        LOGGER.trace("getCurrentUserName()");
        User cu = getCurrentUser();
        return (cu != null ? cu.getLoginName() : "");
    }
    // Since the current user and 'logout' are in a combo box whose value can change
    public void setCurrentUserName(String name) {
        LOGGER.debug("dummy setCurrentUserName() name=" + name);
    }
}
