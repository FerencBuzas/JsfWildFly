package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;

@ApplicationScoped
public class LoginManager implements Serializable {

    private static final long serialVersionUID = -8908908908L;
    
    private static final Logger LOGGER = Logger.getLogger(LoginManager.class);

    @Inject
    private UserRepository userRepository;
    
    private User currentUser;   // logged in

    public boolean login(String loginName, String password) {
        
        LOGGER.info("## login() name=" + loginName);
        
//        List<User> userList = userRepository.findAll();
//        LOGGER.info("## userList: " + userList);
        
        currentUser = userRepository.findByLoginName(loginName);
        
        if (currentUser != null) {
            LOGGER.debug("login successful for " + loginName);
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
        return currentUser != null && currentUser.getRole().equals(User.ROLE_ADMIN);
    }

    public User getCurrentUser() { return currentUser; }
    public void setCurrentUser(User currentUser) { this.currentUser = currentUser; }
}
