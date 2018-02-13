package jsfwf.data;

import org.hibernate.Session;
import org.jboss.logging.Logger;
import jsfwf.model.User;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Get one user or all users from the database.
 */
@javax.ejb.Singleton
public class UserRepository implements Serializable {

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class);

    @PersistenceContext
    private EntityManager em;

    public User findByLoginName(String loginName) {
        LOGGER.debug("## findByLoginName() " + loginName);

        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        User result = null;
        criteria.select(user).where(cb.equal(user.get("loginName"), loginName));
        try {
            TypedQuery<User> query = em.createQuery(criteria);
            result = query.getSingleResult();
        }
        catch (NoResultException e) {
            LOGGER.warn("noResultException");
            return null;
        }
        getRoles(result);

        LOGGER.debug("end of findByLoginName()");
        return result;
    }
    
    // Temporary hack until I can handle database authorization correctly
    // NOTE: this is Hibernate-specific.
    //
    private void getRoles(User user) {
        if (user.getRoles().size() > 0) {
            return;  // already read
        }
        
        try {
            Session hibernateSession = em.unwrap(Session.class);
            hibernateSession.doWork(conn -> getRolesWithJdbc(conn, user));
        } catch (Exception e) {
            LOGGER.warn("## " + e.getMessage() + " Cause: " + e.getCause().getMessage());
        }
    }

    private void getRolesWithJdbc(Connection connection, User user) throws SQLException {
        LOGGER.debug("getRolesWithJdbc() for user=" + user.getLoginName());
        
        PreparedStatement statement = connection.prepareStatement(
                "SELECT role_name FROM Myrole WHERE princ_id=?");
        statement.setString(1, user.getLoginName());
        ResultSet rs = statement.executeQuery();
        if (rs == null) {
            LOGGER.warn("## No role for user " + user);
            return;
        }
        while (rs.next()) {
            String name = rs.getString(1).toLowerCase();
            user.addRole(name.contains("admin") ? User.Role.Admin : User.Role.User);
        }
    }

    public List<User> findAll() {
        LOGGER.debug("findAll()");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user).orderBy(cb.asc(user.get("loginName")));
        List<User> result = em.createQuery(criteria).getResultList();
        
        result.forEach(this::getRoles);

        LOGGER.debug("end of findAll()");
        return result;
    }
}
