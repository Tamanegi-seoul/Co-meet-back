package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.Users;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
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

    public List<StackRelation> findByPostId(Long postId) {
        return em.createQuery("select sr from StackRelation sr where sr.post.postId = :postId", StackRelation.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<StackRelation> findByUserId(Long userId) {
        return em.createQuery("select sr from StackRelation sr where sr.user.userId = :userId", StackRelation.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public int removeRelatedStacksByPost(Long postId) {
        return em.createQuery("delete from StackRelation sr where sr.post.postId = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
    }

    public int removeRelatedStakcsByUser(Long userId) {
        log.warn("[StackRelationRepository:removeRelatedStacksByUser]method init");
        return em.createQuery("delete from StackRelation sr where sr.user.userId = :userId")
                .setParameter("userId", userId)
                .executeUpdate();
    }


}
