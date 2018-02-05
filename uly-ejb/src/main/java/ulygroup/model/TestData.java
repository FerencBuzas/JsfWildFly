package ulygroup.model;

import org.jboss.logging.Logger;
import ulygroup.data.UserRepository;

import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.persistence.EntityManager;

@Stateless
public class TestData {

    private static final Logger LOGGER = Logger.getLogger(TestData.class);
    
    @Inject
    private EntityManager em;

    @Inject
    private UserRepository userRepository;
    
    public void createData() {
        if (userRepository.findAll().size() > 0) {
            return;
        }
        
        User feri = new User("feri", "Buzas Ferenc", "feri", User.Role.Admin);
        User bea  = new User("bea",  "Buzas Bea",    "bea", User.Role.User);
        em.persist(feri);
        em.persist(bea);
    }
}
