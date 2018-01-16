package ulygroup.service;

import org.jboss.logging.Logger;
import ulygroup.model.Event;
import ulygroup.model.User;

import javax.annotation.PostConstruct;
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
    
    @PostConstruct
    public void postConstruct() {
        LOGGER.debug("@PostConstruct");
    }


    public void add(User user, Event.Type type, String info, boolean success) {

        Event event = new Event(0, new Date(), user, type, info, success);
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
    }
}
