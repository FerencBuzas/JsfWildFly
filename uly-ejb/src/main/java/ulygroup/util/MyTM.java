package ulygroup.util;

import javax.ejb.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import java.io.Serializable;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Use this class in case of RESOURCE_LOCAl transaction management.
 * With app servers, JTA is better.
 */
@Singleton
public class MyTM implements Serializable {
    
    @PersistenceUnit
    private EntityManagerFactory emf;   // this is thread safe            
            
    public EntityManager createEntityManager() {
        return emf.createEntityManager();
    }
    
    /**
     * Execute the given code block in a transaction; also close the transaction.
     *
     * @param toCall  - code to be called in the transaction; gets an EntityManager
     * @param <R>     - type of the result
     * @return        - the result of the code
     */
    public <R> R apply(Function<EntityManager, R> toCall) {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        R result = toCall.apply(em);

        em.getTransaction().commit();
        em.close();

        return result;
    }
    
    /**
     * Execute the given code block in a transaction; also close the transaction.
     *
     * @param toCall  - code to be called in the transaction; gets an EntityManager
     */
    public void accept(Consumer<EntityManager> toCall) {

        EntityManager em = emf.createEntityManager();
        em.getTransaction().begin();

        toCall.accept(em);

        em.getTransaction().commit();
        em.close();
    }
}
