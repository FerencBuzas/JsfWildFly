package ulygroup.data;

import org.hibernate.Session;
import org.jboss.logging.Logger;
import ulygroup.model.Role;
import ulygroup.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

/**
 * Get one user or all users from the database.
 */
@ApplicationScoped
public class UserRepository {

    private static final Logger LOGGER = Logger.getLogger(UserRepository.class);

    @Inject
    private EntityManager em;

    public User findById(Long id) {
        return em.find(User.class, id);
    }

    public User findByLoginNameHSQL(String loginName) {
        LOGGER.debug("## HSQL findByLoginName-HSQL() " + loginName);

        String userQuery = "SELECT u FROM User u WHERE u.loginName=:loNa";
        List<User>userList = em.createQuery(userQuery, User.class)
                .setParameter("loNa", loginName)
                .getResultList();
        User result = ((userList == null || userList.isEmpty()) ? null : userList.get(0));
        LOGGER.debug("end of findByLoginName()");
        return result;
    }

    public User findByLoginName(String loginName) {
        LOGGER.debug("## findByLoginName-CritA() " + loginName);

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
            LOGGER.info("  noResultException");
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
        try ( Session hibernateSession = em.unwrap(Session.class)) {

              hibernateSession.doWork(connection -> {
                  PreparedStatement statement = connection.prepareStatement(
                          "SELECT role_name FROM Myrole WHERE princ_id=?");
                  statement.setString(1, user.getLoginName());
                  ResultSet rs = statement.executeQuery();
                  if (rs != null) {
                      while (rs.next()) {
                          String roleName = rs.getString(1);
                          if (roleName.toLowerCase().contains("admin")) {
                              user.addRole(Role.Admin);
                          } else {
                              user.addRole(Role.User);
                          }
                      }
                  }
              });
        } catch (Exception e) {
            LOGGER.warn("## " + e.getMessage());
        }
    }

    public List<User> findAll() {
        LOGGER.debug("findAll()");
        
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> criteria = cb.createQuery(User.class);
        Root<User> user = criteria.from(User.class);

        criteria.select(user).orderBy(cb.asc(user.get("loginName")));
        return em.createQuery(criteria).getResultList();
    }
}
