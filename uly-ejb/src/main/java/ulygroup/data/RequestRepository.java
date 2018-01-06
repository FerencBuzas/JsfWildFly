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
//            ParameterExpression<Request.State> p = cb.parameter(Request.State.class, );
//            criteria.add(cb.equal(emp.get("name"), p));
        }
        
        else if (filter == Filter.Requested) {
            criteria.where(cb.equal(request.get("state"), Request.State.Requested));
        }
        list = em.createQuery(criteria).getResultList();
        
        LOGGER.debug("  end of findAll \n## list: " + list);

        return list;
    }

    public void remove(Request request) {
        LOGGER.debug("remove() " + request);
        em.remove(request);
    }

    public void persist(Request request) {
        LOGGER.debug("persist() " + request);
        em.persist(request);
    }
}
