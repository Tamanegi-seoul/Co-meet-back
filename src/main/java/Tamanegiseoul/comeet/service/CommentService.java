package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostService postService;
    private final UserService userService;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Long registerComment(Comment comment) {
        commentRepository.save(comment);
        comment.updateCreatedDate();
        comment.updateModifiedDate();
        return comment.getCommentId();
    }


    /**************************
     * UPDATE COMMENT METHODS *
     **************************/

    @Transactional
    public Comment updateComment(UpdateCommentRequest updatedComment) {
        Comment findComment = commentRepository.findOne(updatedComment.getCommentId());
        findComment.updateComment(updatedComment);
        findComment.updateModifiedDate();
        return findComment;
    }

    @Transactional
    public void removeComment(Long commentId) {
        Comment findComment = commentRepository.findOne(commentId);
        em.remove(findComment);
    }


    /**************************
     * COMMENT SEARCH METHODS *
     **************************/

    @Transactional(readOnly = true)
    public List<Comment> findAll() {
        return commentRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        return commentRepository.findCommentById(commentId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByUserId(Long userId) {
        return commentRepository.findCommentByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByPostId(Long postId) {
        return commentRepository.findCommentByPostId(postId);
    }


}
