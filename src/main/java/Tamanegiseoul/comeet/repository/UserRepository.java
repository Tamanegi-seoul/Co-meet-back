package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final EntityManager em;

    public void save(Users user) {
        em.persist(user);
    }

    public Users findOne(Long id) {
        return em.find(Users.class, id);
    }

    public Users findUserByNickname(String nickname) {
        return em.createQuery("select u from Users u where u.nickname = :nickname", Users.class)
                .setParameter("nickname", nickname)
                .getResultList().stream().findFirst().orElse(null);
    }

    public List<Users> findAll() {
        return em.createQuery("select u from Users u", Users.class)
                .getResultList();
    }

    public List<StackRelation> findPreferredStacks(Long userId) {
        return em.createQuery("select sr from StackRelation sr where sr.user.id = :userId", StackRelation.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public Users findUserByEmail(String email) {
        return em.createQuery("select u from Users u where u.email = :email", Users.class)
                .setParameter("email", email)
                .getResultList().stream().findFirst().orElse(null);
    }
}
