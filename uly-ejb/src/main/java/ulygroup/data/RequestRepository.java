package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Request;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.enterprise.context.SessionScoped;
import java.io.Serializable;
import java.util.List;

@SessionScoped
public class RequestRepository implements Serializable {
    
    private static final Logger LOGGER = Logger.getLogger(RequestRepository.class);

    private List<Request> list;

    public RequestRepository() {
        LOGGER.debug("RequestRepository()");
    }

    @Inject
    private EntityManager em;

    public Request findById(Long id) {
        return em.find(Request.class, id);
    }

    public List<Request> findAll() {
        LOGGER.info("findAll()");

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Request> criteria = cb.createQuery(Request.class);
        Root<Request> request = criteria.from(Request.class);

        criteria.select(request).orderBy(cb.asc(request.get("user")));
        list = em.createQuery(criteria).getResultList();
        
        list = em.createQuery("from Request", ulygroup.model.Request.class).getResultList();
        LOGGER.info("\n\n###### list: " + list + "\n\n#####\n\n");
        
        return list;
    }

    public void remove(Request request) {
        em.remove(request);
    }

    public void persist(Request request) {
        em.persist(request);
    }
}
