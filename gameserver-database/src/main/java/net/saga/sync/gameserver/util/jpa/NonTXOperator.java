package net.saga.sync.gameserver.util.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public final class NonTXOperator {

    private NonTXOperator(){}
    
    public static <T> T execute(EntityManagerFactory emf, Operation<T> op) {
        EntityManager em = null;
        try {
            em = emf.createEntityManager();
            return op.run(em);
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
    
}
