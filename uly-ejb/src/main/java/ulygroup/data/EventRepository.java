package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Event;

import javax.annotation.PostConstruct;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

@ApplicationScoped
public class EventRepository {

    private static final Logger LOGGER = Logger.getLogger(EventRepository.class);

    @Inject
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
