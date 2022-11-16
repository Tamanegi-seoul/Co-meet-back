package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class CommentRepository {
    private final EntityManager em;

    public void save(Comment comment) {
        em.persist(comment);
    }

    public Comment findOne(Long id) {
        return em.find(Comment.class, id);
    }

    public List<Comment> findAll() {
        return em.createQuery("select c from Comment c", Comment.class)
                .getResultList();
    }

    public List<Comment> findCommentByPostId(Long postId) {
        return em.createQuery("select c from Comment c where c.post.id = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<Comment> findCommentByUserId(Long userId) {
        return em.createQuery("select c from Comment c where c.user.id = :userId", Comment.class)
                .setParameter("userId", userId)
                .getResultList();
    }

}
