package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Request;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

/**
 * Selects requests (one or filtered or all).
 */
@ApplicationScoped
public class RequestRepository implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(RequestRepository.class);

    public enum Filter { Requested, Accepted, All }

    private List<Request> list;

    public RequestRepository() {
        LOGGER.debug("RequestRepository()");
    }

    @Inject
    private EntityManager em;

    public Request findById(Long id) {
        LOGGER.debug("findById() id=" + id);
        return em.find(Request.class, id);
    }

    public List<Request> findAll(Filter filter) {
        LOGGER.debug("findAll()");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Request> criteria = cb.createQuery(Request.class);
        Root<Request> request = criteria.from(Request.class);
        
        criteria.select(request).orderBy(cb.asc(request.get("user")));

        if (filter == Filter.Accepted) {
            criteria.where(cb.equal(request.get("state"), Request.State.Accepted));
        }
        
        else if (filter == Filter.Requested) {
            criteria.where(cb.equal(request.get("state"), Request.State.Requested));
        }
        list = em.createQuery(criteria).getResultList();
        
        LOGGER.debug("  end of findAll \n## list: " + list);

        return list;
    }
}
