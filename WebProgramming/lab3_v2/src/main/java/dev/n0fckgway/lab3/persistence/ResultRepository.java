package dev.n0fckgway.lab3.persistence;

import dev.n0fckgway.lab3.model.Result;

import java.util.List;


public class ResultRepository extends CrudRepository {

    public void save(Result result) {
        executeWrite(em -> em.persist(result));

    }

    public List<Result> findAll() {
        return executeRead(em ->
                em.createQuery("SELECT r FROM Result r", Result.class)
                        .getResultList()
        );
    }

    public List<Result> findBySessionId(String sessionId) {
        return executeRead(em ->
                em.createQuery("SELECT r FROM Result r WHERE r.sessionId = :sessionId", Result.class)
                        .setParameter("sessionId", sessionId)
                        .getResultList()
        );
    }

    public void deleteAll() {
        executeWrite(em -> em.createQuery("DELETE FROM Result").executeUpdate());

    }

    public void deleteBySessionId(String sessionId) {
        executeWrite(em ->
                em.createQuery("DELETE FROM Result r WHERE r.sessionId = :sessionId")
                        .setParameter("sessionId", sessionId)
                        .executeUpdate()
        );
    }

}

