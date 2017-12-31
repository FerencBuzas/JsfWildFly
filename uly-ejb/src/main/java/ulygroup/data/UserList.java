package ulygroup.data;

import org.jboss.logging.Logger;
import ulygroup.model.User;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;

@ApplicationScoped
public class UserList implements Serializable {
    private static final Logger LOGGER = Logger.getLogger(UserList.class);

    @Inject
    private TestData testData;
    
    private long lastId;
    private List<User> list;
    
    private AtomicBoolean editing = new AtomicBoolean(false);
    
    public UserList() {
        LOGGER.debug("UserList()");
    }

    public void add(String name, String loginName, String password, String role) {
        LOGGER.debug("add() loginName=" + loginName);
    
        createTestData();
        list.add(new User(++lastId, name, loginName, password, role));
    }

    public Optional<User> find(String loginName) {
        createTestData();
        return list.stream()
                .filter(u -> loginName.equals(u.getLoginName()))
                .findFirst();
    }
    public Optional<User> findById(long id) {
        createTestData();
        return list.stream()
                .filter(u -> u.getId() == id)
                .findFirst();
    }

    User get(String loginName) {
        return find(loginName).orElse(new User(++lastId, "no such", "ln="+loginName, "",
                User.ROLE_USER ));
    }
    User getById(long id) {
        return findById(id).orElse(new User(++lastId, "no such", "id="+id, "", User.ROLE_USER ));
    }


    // --- set, get ---
    
    public List<User> getList() { createTestData(); return list; }

    public boolean isEditing() { return editing.get(); }

    public void setTestData(TestData testData) { this.testData = testData; }

    public String setEditing(boolean e) {
        LOGGER.debug("setEditing() " + e);
        editing.set(e);
        return "";  // maradjon ott
    }
    
    public String delete(long id) {
        LOGGER.debug("delete... id=" + id);
        return "";
    }

    // --- data ---
    
    private void createTestData() {
        if (list == null) {
            list = testData.createUserTestData();
        }
    }
}
