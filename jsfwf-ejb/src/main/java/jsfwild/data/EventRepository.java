package jsfwf.data;

import org.jboss.logging.Logger;
import jsfwf.model.Event;

import javax.annotation.PostConstruct;
import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;

@Singleton
public class EventRepository implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(EventRepository.class);

    @PersistenceContext
    private EntityManager em;
    
    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("@PostConstruct");
    }
    
    public List<Event> getList() {
        LOGGER.debug("getList()");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> criteria = cb.createQuery(Event.class);
        Root<Event> event = criteria.from(Event.class);

        criteria.select(event).orderBy(cb.asc(event.get("id")));
        return em.createQuery(criteria).getResultList();
    }
}
