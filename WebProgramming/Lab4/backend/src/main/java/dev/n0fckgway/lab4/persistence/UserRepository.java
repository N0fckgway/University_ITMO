package dev.n0fckgway.lab4.persistence;

import dev.n0fckgway.lab4.model.User;
import jakarta.ejb.Stateless;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

@Stateless
@Transactional
public class UserRepository extends CrudRepository {

    public void save(User user) {
        executeWrite(em -> {
            if (user.getId() == null) {
                em.persist(user);
            } else em.merge(user);
        });
    }

    public User findById(Long id) {
        return executeRead(em -> em.find(User.class, id));
    }

    public Optional<User> findByEmail(String email) {
        return executeRead(em ->
            em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultStream()
                .findFirst()
        );
    }

    public boolean userExists(String email) {
        return findByEmail(email).isPresent();
    }

}
