package Tamanegiseoul.comeet.repository;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class PostRepository {
    private final EntityManager em;

    public void save(Posts post) {
        em.persist(post);
    }

    public Posts findOne(Long id) {
        return em.find(Posts.class, id);
    }

    public List<Posts> findAll() {
        return em.createQuery("select p from Posts p", Posts.class)
                .getResultList();
    }

    public List<Posts> findPostByUserId(Long userId) {
        return em.createQuery("select p from Posts p where p.poster.userId = :userId", Posts.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public int removePostByPosterId(Long userId) {
        return em.createQuery("delete from Posts p where p.poster.userId = :userId ")
                .setParameter("userId", userId)
                .executeUpdate();
    }

    public int removePostByPostId(Long postId) {
        return em.createQuery("delete from Posts p where p.postId = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
    }

}
