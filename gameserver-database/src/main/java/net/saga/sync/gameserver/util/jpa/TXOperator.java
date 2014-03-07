package net.saga.sync.gameserver.util.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

public final class TXOperator {

    private TXOperator() {
    }

    public static <T> T execute(EntityManagerFactory emf, Operation<T> op) {
        EntityManager em = null;
        EntityTransaction tx = null;
        T value = null;
        try {
            em = emf.createEntityManager();
            tx = em.getTransaction();
            tx.begin();
            value = op.run(em);
            tx.commit();
            return value;
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            if (em != null) {
                em.close();
            }
        }
    }
}
