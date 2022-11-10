package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class StackRelationRepository {
    private final EntityManager em;

    public void save(Users user) {
        em.persist(user);
    }

    public Users findOne(Long id) {
        return em.find(Users.class, id);
    }

    public Users findUserByNickname(String nickname) {
        return em.createQuery("select u from Users u where u.nickname = :nickname", Users.class)
                .getSingleResult();
    }

    public List<Users> findAll() {
        return em.createQuery("select u from Users u", Users.class)
                .getResultList();
    }
}
