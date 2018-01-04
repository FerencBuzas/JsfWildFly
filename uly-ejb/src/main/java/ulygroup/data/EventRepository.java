package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.Event;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;

@ApplicationScoped
public class EventRepository {

    private static final Logger LOGGER = Logger.getLogger(EventRepository.class);

    @Inject
    private EntityManager em;

    public Event findById(Long id) {
        return em.find(Event.class, id);
    }

    public List<Event> findAll() {
        LOGGER.debug("findAll()");
        
//        CriteriaBuilder cb = em.getCriteriaBuilder();
//        CriteriaQuery<Event> criteria = cb.createQuery(Event.class);
//        Root<Event> user = criteria.from(Event.class);
//
//        criteria.select(user).orderBy(cb.asc(user.get("user")));
//        return em.createQuery(criteria).getResultList();
        
        return em.createQuery("from Event", Event.class).getResultList();
    }
}
