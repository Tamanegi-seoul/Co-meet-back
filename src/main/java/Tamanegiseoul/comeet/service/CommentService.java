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
        return comment.getId();
    }


    /**************************
     * UPDATE COMMENT METHODS *
     **************************/

    @Transactional
    public void updateComment(Long commentId, UpdateCommentRequest updatedComment) {
        Comment findComment = commentRepository.findOne(commentId);
        findComment.updateComment(updatedComment);
        findComment.updateModifiedDate();
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
    public List<Posts> findAll() {
        return postService.findAll();
    }

    @Transactional(readOnly = true)
    public List<Posts> findCommentById(Long userId) {
        return postService.findPostByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Posts> findCommentByUserId(Long userId) {
        return postService.findPostByUserId(userId);
    }

    @Transactional(readOnly = true)
    public List<Posts> findCommentByPostId(Long postId) {
        return postService.findPostByUserId(postId);
    }


}
