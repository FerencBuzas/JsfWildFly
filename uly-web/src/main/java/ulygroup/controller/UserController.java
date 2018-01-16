package ulygroup.controller;

import org.jboss.logging.Logger;
import ulygroup.data.UserRepository;
import ulygroup.model.User;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.io.Serializable;
import java.util.List;


@Named("userController")
@SessionScoped
public class UserController implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(UserController.class);

    @Inject
    private UserRepository userRepository;
    
    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("postConstruct()");
    }
    
    public List<User> getList() { return userRepository.findAll(); }
}
