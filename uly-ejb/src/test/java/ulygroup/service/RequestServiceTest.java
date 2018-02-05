package ulygroup.service;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.logging.Logger;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import ulygroup.model.Request;
import ulygroup.model.Role;
import ulygroup.model.User;
import ulygroup.util.Resources;

import javax.inject.Inject;

import static org.junit.Assert.assertNotNull;

@Ignore
@RunWith(Arquillian.class)
public class RequestServiceTest {
    
    private static final Logger LOGGER = Logger.getLogger(RequestServiceTest.class);
    
    @Deployment
    public static Archive<?> createTestArchive() {
        return ShrinkWrap.create(WebArchive.class, "test.war")
                .addClasses(Request.class, RequestService.class, Resources.class)
                .addAsResource("META-INF/test-persistence.xml", "META-INF/persistence.xml")
                .addAsWebInfResource(EmptyAsset.INSTANCE, "beans.xml")
                
                // Deploy our test datasource
                .addAsWebInfResource("test-ds.xml", "test-ds.xml");
    }

    @Inject
    RequestService requestService;

    @Test
    public void testAddRequest() throws Exception {
        User user = new User("Test User", "test", "test", Role.Admin);
        Request newRequest = new Request(0, user, 200000L, Request.State.Requested);
        requestService.persist(newRequest);
        assertNotNull(newRequest.getId());
        LOGGER.info(newRequest + " was persisted with id " + newRequest.getId());
    }

}
