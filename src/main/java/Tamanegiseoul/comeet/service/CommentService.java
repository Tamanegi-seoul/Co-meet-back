package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Service @Slf4j
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
        comment.updateCreatedTime();
        comment.updateModifiedTime();
        return comment.getCommentId();
    }


    /**************************
     * UPDATE COMMENT METHODS *
     **************************/

    @Transactional
    public Comment updateComment(UpdateCommentRequest updatedComment) {
        Comment findComment = this.findCommentById(updatedComment.getCommentId());
        findComment.updateComment(updatedComment);
        findComment.updateModifiedTime();
        return findComment;
    }

    @Transactional
    public void removeComment(Long commentId) {
        Comment findComment = this.findCommentById(commentId);
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
        Comment findComment = commentRepository.findCommentById(commentId);
        if(findComment == null) {
            throw new ResourceNotFoundException("Comment", "commentId", commentId);
        }
        return findComment;
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByUserId(Long userId) {
        return commentRepository.findCommentByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByPostId(Long postId) {
        log.warn("[CommentServce:findCommentByPostId]method init");
        List<Comment> findComments = commentRepository.findCommentByPostId(postId);
        log.warn("[CommentServce:findCommentByPostId]" + " found " + findComments.size() + "ea comments for " + postId);
        return findComments;
    }


}
