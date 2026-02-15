package dev.n0fckgway.lab4.persistence;

import dev.n0fckgway.lab4.model.PointResult;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;

@Stateless
@Transactional
public class PointResultRepository extends CrudRepository {

    public void save(PointResult result) {
        executeWrite(em -> {
            if (result.getId() == null) {
                em.persist(result);
            } else {
                em.merge(result);
            }
        });
    }

    public List<PointResult> findAllByUserId(Long userId) {
        return executeRead(em ->
                em.createQuery(
                                "select p from PointResult p where p.user.id = :userId order by p.checkedAt desc, p.id desc",
                                PointResult.class
                        )
                        .setParameter("userId", userId)
                        .getResultList()
        );
    }

    public void deleteAllByUserId(Long userId) {
        executeRead(em ->
                em.createQuery("delete from PointResult p where p.user.id = :userId")
                        .setParameter("userId", userId)
                        .executeUpdate()
        );
    }
}
