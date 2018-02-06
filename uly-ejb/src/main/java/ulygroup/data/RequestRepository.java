package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Request;
import ulygroup.model.User;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Selects requests (one or filtered or all).
 */
@Stateless
public class RequestRepository implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(RequestRepository.class);

    public enum Filter { Requested, Accepted, All }

    @PersistenceContext
    private EntityManager em;

    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("postConstruct");
    }

    public Request findById(Long id) {
        LOGGER.debug("findById() id=" + id);
        return em.find(Request.class, id);
    }

    /**
     * Get requests, depending on the given parameters.
     * @param filter   Admin users can set whether to see all/accepted/requested only.
     * @param user     If not null, get only items of the given user. If null, all users.
     * @param from     Log message to see, where it has been called from
     * @return         the items matching the given criteria.
     */
    public List<Request> findAll(Filter filter, User user, String from) {
        LOGGER.debug("findAll()  filter=" + filter + ", user=" + user + ", from=" + from);
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Request> criteria = cb.createQuery(Request.class);
        Root<Request> request = criteria.from(Request.class);

        // Create Where conditions
        List<Predicate> predicates = new ArrayList<>();
        if (filter == Filter.Accepted) {
            predicates.add(cb.equal(request.get("state"), Request.State.Accepted));
        }
        if (filter == Filter.Requested) {
            predicates.add(cb.equal(request.get("state"), Request.State.Requested));
        }
        if (user != null) {
            predicates.add(cb.equal(request.get("user"), user));
        }
        Predicate[] predArray = predicates.toArray(new Predicate[predicates.size()]);
        
        criteria.select(request)
                .where(predArray)
                .orderBy(cb.asc(request.get("user")));

        List<Request> list = em.createQuery(criteria).getResultList();
        
        LOGGER.debug("  end of findAll \n## list: " + list);

        return list;
    }
}
