package ulygroup.service;

import org.jboss.logging.Logger;
import ulygroup.data.LoginManager;
import ulygroup.model.Event;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.Date;
import java.util.List;

@ApplicationScoped
public class EventService {

    private static final Logger LOGGER = Logger.getLogger(EventService.class);

    @Inject
    private EntityManager em;
    
    @Inject
    private LoginManager loginManager;

    public void add(String eventName, boolean success) {

        Event event = new Event(0, new Date(), loginManager.getCurrentUser(), eventName, success);
        LOGGER.debug("add(): " + event);

        em.persist(event);
    }

    public List<Event> getList() {
        LOGGER.debug("getList");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<Event> criteria = cb.createQuery(Event.class);
        Root<Event> user = criteria.from(Event.class);

        criteria.select(user).orderBy(cb.asc(user.get("user")));
        return em.createQuery(criteria).getResultList();
        
//        return em.createQuery("from Event", Event.class).getResultList();
    }
}
