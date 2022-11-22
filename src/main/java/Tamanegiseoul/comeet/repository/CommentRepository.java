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

    public Comment findCommentById(Long commentId) {
        return em.createQuery("select c from Comment c where c.commentId = :commentId", Comment.class)
                .setParameter("commentId", commentId)
                .getResultList().stream().findFirst().orElse(null);
    }

    public List<Comment> findCommentByPostId(Long postId) {
        return em.createQuery("select c from Comment c where c.post.postId = :postId", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<Comment> findCommentByUserId(Long userId) {
        return em.createQuery("select c from Comment c where c.user.userId = :userId", Comment.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public int removeCommentByUserId(Long userId) {
        return em.createQuery("delete from Comment c where c.user.userId = :userId", Comment.class)
                .setParameter("userId", userId)
                .executeUpdate();
    }

    public int removeCommentByPostId(Long postId) {
        return em.createQuery("delete from Comment c where c.post.postId = :postId", Comment.class)
                .setParameter("postId", postId)
                .executeUpdate();
    }


}
