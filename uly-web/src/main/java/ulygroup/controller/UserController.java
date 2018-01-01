package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.UserRepository;
import ulygroup.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class UserController implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Inject
    UserRepository userRepository;
    
    private AtomicBoolean editing = new AtomicBoolean(false);
    
    public UserController() {
        LOGGER.debug("UserController()");
    }
    
    public List<User> getList() { return userRepository.findAll(); }

    // --- set, get ---
    
    public boolean isEditing() { return editing.get(); }

    public String setEditing(boolean e) {
        LOGGER.debug("setEditing() " + e);
        editing.set(e);
        return "";  // maradjon ott
    }
}
