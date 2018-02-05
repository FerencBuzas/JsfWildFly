package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.UserRepository;
import ulygroup.model.TestData;
import ulygroup.model.User;
import ulygroup.util.FacesUtil;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.event.AjaxBehaviorEvent;
import javax.inject.Inject;
import javax.inject.Named;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.Serializable;

@Named("loginController")
@ApplicationScoped
public class LoginController implements Serializable {

    private static final long serialVersionUID = -2879890213L;

    private static final Logger LOGGER = Logger.getLogger(LoginController.class);

    private static final String HOME_PAGE_REDIRECT = "index.xhtml?faces-redirect=true";

    private static final String S_CURRENT_USER = "currentUser";
    private static final String S_EDIT_USER = "editUser";

    @Inject
    private FacesUtil facesUtil;
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    TestData testData;
    
    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("@PostConstruct");

        testData.createData();
        
        if (getEdiUser() == null) {
            LOGGER.debug("  PostC, creating ediUser");
            setEdiUser(new User());
        }
    }

    public User getEdiUser() { return facesUtil.getSessionUser(S_EDIT_USER); }
    public void setEdiUser(User user) { facesUtil.setSessionUser(S_EDIT_USER, user); }

    public void login(AjaxBehaviorEvent evt) {   // for experiments with Ajax 
        login();
    }
    
    public String login() {

        LOGGER.info("## login()");
        User eu = getEdiUser();
        LOGGER.debug("login: name=" + eu.getLoginName() + " password=" + eu.getPassword());

        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();

        HttpServletResponse response;
        try {
            request.login(eu.getLoginName(), eu.getPassword());
            response = (HttpServletResponse) externalContext.getResponse();
            request.authenticate(response);
            LOGGER.debug("login survived");
        }
        catch (ServletException | IOException e) {
            LOGGER.info("## login failed");
            context.addMessage("Ajaj", new FacesMessage(FacesMessage.SEVERITY_WARN, "Login failed", e.toString()));
            return null;
        }

        // Login successful, set members, log
        facesUtil.logRequest(request, true);
        if (response != null) {
            facesUtil.logResponse(response);
        }
        
        User user = userRepository.findByLoginName(eu.getLoginName());
        setCurrentUser(user);
        LOGGER.debug("isLoggedIn: " + getCurrentUser() + " isAdmin: " + isAdminLoggedIn());
        LOGGER.debug("login: OK, redirecting to " + HOME_PAGE_REDIRECT);
        
        return HOME_PAGE_REDIRECT;
    }
        
    public String logout() {
        LOGGER.debug("logout");
                
        FacesContext context = FacesContext.getCurrentInstance();
        ExternalContext externalContext = context.getExternalContext();
        
        HttpServletRequest request = (HttpServletRequest) externalContext.getRequest();
        if (request != null) {
            try {
                request.logout();
                LOGGER.info("logout successful");
            } catch (ServletException e) {
                LOGGER.info("logout() error");
            }
            facesUtil.logRequest(request, false);
        }
        setCurrentUser(null);
        
        return HOME_PAGE_REDIRECT;
    }

    public String ifLoggedInForwardHome() {
        return isLoggedIn() ? HOME_PAGE_REDIRECT : null;
    }
    public boolean isLoggedIn() {
        return getCurrentUser() != null;
    }

    public boolean isAdminLoggedIn() {
        User currentUser = facesUtil.getSessionUser(S_CURRENT_USER);
        return currentUser != null && currentUser.hasAdminRole();
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
