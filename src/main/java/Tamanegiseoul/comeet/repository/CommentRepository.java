package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.Comment;
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
        return em.createQuery("select c from Comment c where c.post.postId = :postId order by c.commentId asc", Comment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<Comment> findCommentByMemberId(Long memberId) {
        return em.createQuery("select c from Comment c where c.member.memberId = :memberId", Comment.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public int removeCommentBymemberId(Long memberId) {
        return em.createQuery("delete from Comment c where c.member.memberId = :memberId")
                .setParameter("memberId", memberId)
                .executeUpdate();
    }

    public int removeCommentByPostId(Long postId) {
        return em.createQuery("delete from Comment c where c.post.postId = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
    }


}
