package dev.n0fckgway.lab4.persistence;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import java.util.function.Consumer;
import java.util.function.Function;


public abstract class CrudRepository {
    @PersistenceContext(unitName = "default")
    private EntityManager entityManager;

    public EntityManager getEntityManager() {
        return entityManager;
    }

    protected void executeWrite(Consumer<EntityManager> action) {
        action.accept(getEntityManager());
    }

    protected <T> T executeRead(Function<EntityManager, T> action) {
        return action.apply(getEntityManager());
    }
}
