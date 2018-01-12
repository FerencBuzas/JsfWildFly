package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;

/**
 * Keep info about the currently logged-in user.
 * TODO: app scoped, not good for multiple users.
 */
@ApplicationScoped
public class LoginManager implements Serializable {

    private static final long serialVersionUID = -8908908908L;
    
    private static final Logger LOGGER = Logger.getLogger(LoginManager.class);

    @Inject
    private UserRepository userRepository;
    
    private User currentUser;   // logged in

    public boolean login(String loginName, String password) {
        
        LOGGER.debug("login() name=" + loginName);
        
        LOGGER.debug("  userList: " + userRepository.findAll());

        currentUser = userRepository.findByLoginName(loginName);
        
        if (currentUser != null) {
            LOGGER.info("login successful for " + loginName);
            return true;
        } 
        else {
            LOGGER.info("login failed for " + loginName);
            return false;
        }
    }

    public void logout() {
        LOGGER.info("logout: " + currentUser);
        currentUser = null;
    }

    public boolean isLoggedIn() {
        return currentUser != null;
    }

    public boolean isAdminLoggedIn() {
        return currentUser != null && currentUser.getRole() == User.Role.Admin;
    }

    public User getCurrentUser() { return currentUser; }
}
