package Tamanegiseoul.comeet.repository;
import Tamanegiseoul.comeet.domain.Posts;
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

    public List<Posts> findAll(int offset, int limit) {
        return em.createQuery("select p from Posts p order by p.postId asc", Posts.class)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .getResultList();
    }

    public List<Posts> findPostByMemberId(Long memberId) {
        return em.createQuery("select p from Posts p where p.poster.memberId = :memberId order by p.postId asc", Posts.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public int removePostByPosterId(Long memberId) {
        return em.createQuery("delete from Posts p where p.poster.memberId = :memberId ")
                .setParameter("memberId", memberId)
                .executeUpdate();
    }

    public int removePostByPostId(Long postId) {
        return em.createQuery("delete from Posts p where p.postId = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
    }

}
