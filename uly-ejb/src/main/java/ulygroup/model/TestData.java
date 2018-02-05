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

        LOGGER.debug("##################");
        LOGGER.debug("##  createData()");
        LOGGER.debug("##################");

        User feri = new User("feri", "Buzas Ferenc", "feri");
        User bea  = new User("bea",  "Buzas Bea",    "bea");
        Role feriRole  = new Role(feri,  "Admin");
        Role beaRole   = new Role(bea,   "User");
        feri.addRole(feriRole);
        bea.addRole(beaRole);

        em.persist(feri);
        em.persist(bea);
        em.persist(feriRole);
        em.persist(beaRole);
        
    }
}
