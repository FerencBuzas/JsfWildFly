package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Get one user or all users from the database.
 */
@ApplicationScoped
public class UserRepository {

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class);

    @Inject
    private EntityManager em;

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public User findByLoginName(String loginName) {
        LOGGER.debug("findByLoginName() " + loginName);
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);
        
        criteria.select(user).where(cb.equal(user.get("loginName"), loginName));
        try {
            return em.createQuery(criteria).getSingleResult();
        }
        catch (NoResultException e) {
            return null;
        }
    }
    
    public List<User> findAll() {
        LOGGER.debug("findAll()");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user).orderBy(cb.asc(user.get("name")));
        return em.createQuery(criteria).getResultList();
    }
}
