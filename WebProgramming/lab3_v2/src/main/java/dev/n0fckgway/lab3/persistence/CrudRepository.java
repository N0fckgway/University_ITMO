package dev.n0fckgway.lab3.persistence;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import java.util.function.Consumer;
import java.util.function.Function;

public abstract class CrudRepository {

    protected void executeWrite(Consumer<EntityManager> action) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction et = em.getTransaction();

        try {
            et.begin();
            action.accept(em);
            et.commit();
        } catch (RuntimeException ex) {
            if (et.isActive()) {
                et.rollback();
            }
            throw ex;
        } finally {
            em.close();
            JpaUtil.closeEntityManagerFactory();
        }
    }

    protected <T> T executeRead(Function<EntityManager, T> action) {
        EntityManager em = JpaUtil.getEntityManagerFactory().createEntityManager();
        EntityTransaction tx = em.getTransaction();

        try {
            tx.begin();
            T result = action.apply(em);
            tx.commit();
            return result;
        } catch (RuntimeException e) {
            if (tx.isActive()) tx.rollback();
            throw e;
        } finally {
            em.close();
            JpaUtil.closeEntityManagerFactory();
        }
    }
}
