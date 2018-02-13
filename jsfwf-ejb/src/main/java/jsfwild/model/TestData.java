package jsfwf.model;

import org.jboss.logging.Logger;
import jsfwf.data.UserRepository;

import javax.ejb.EJB;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.Serializable;

@Singleton
public class TestData implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(TestData.class);

    @PersistenceContext
    private EntityManager em;

    @EJB
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
