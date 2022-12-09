package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(User user) {
        em.persist(user);
    }

    public Long remove(User user) {
        Long removedUserId = user.getUserId();
        em.remove(user);
        return removedUserId;
    }

    public int removeByUserId(Long userId) {
        return em.createQuery("delete from User u where u.userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    public User findOne(Long id) {
        return em.find(User.class, id);
    }

    public User findUserByNickname(String nickname) {
        return em.createQuery("select u from User u where u.nickname = :nickname", User.class)
                .setParameter("nickname", nickname)
                .getResultList().stream().findFirst().orElse(null);
    }

    public List<User> findAll() {
        return em.createQuery("select u from User u", User.class)
                .getResultList();
    }

    public List<StackRelation> findPreferredStacks(Long userId) {
        return em.createQuery("select sr from StackRelation sr where sr.user.userId = :userId", StackRelation.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public User findUserByEmail(String email) {
        return em.createQuery("select u from User u where u.email = :email", User.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst().orElse(null);
    }


}
