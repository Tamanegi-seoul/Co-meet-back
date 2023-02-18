package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.comment.request.CreateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.response.CreateCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.RemoveCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.UpdateCommentResponse;
import Tamanegiseoul.comeet.repository.CommentRepository;
import Tamanegiseoul.comeet.repository.MemberRepository;
import Tamanegiseoul.comeet.repository.PostRepository;
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
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public CreateCommentResponse registerComment(CreateCommentRequest request) {

        Posts findPost = postRepository.findOne(request.getPostId());
        if(findPost==null) {
            log.info("[CommentService:registerComment] post with post id {} not exists", request.getPostId());
            throw new ResourceNotFoundException("post id", "postId", request.getPostId());
        }
        log.info("[CommentService:registerComment] found post with post id {}", request.getPostId());

        Member findMember = memberRepository.findMemberWithStack(request.getMemberId());
        if(findMember==null) {
            log.info("[CommentService:registerComment] member with member id {} not exists", request.getMemberId());
            throw new ResourceNotFoundException("member id", "memberId", request.getMemberId());
        }
        log.info("[CommentService:registerComment] found member with member id", request.getMemberId());

        Comment newComment = Comment.builder()
                .post(findPost)
                .member(findMember)
                .content(request.getContent())
                .build();
        newComment.updateCreatedTime();
        newComment.updateModifiedTime();
        commentRepository.save(newComment);
        log.info("[CommentService:registerComment] register comment for post with post id {}", request.getPostId());

        // 연관관계 메핑(member-post)을 위한 편의 메소드 호출
        findMember.addWroteComments(newComment);
        findPost.addComments(newComment);
        return CreateCommentResponse.toDto(newComment);
    }


    /**************************
     * UPDATE COMMENT METHODS *
     **************************/

    @Transactional
    public UpdateCommentResponse updateComment(UpdateCommentRequest updatedComment) {
        Comment findComment = commentRepository.findOne(updatedComment.getCommentId());
        if(findComment == null) {
            log.info("[CommentService:updateComment] comment with id {} not exists", updatedComment.getCommentId());
            throw new ResourceNotFoundException("comment_id", "comment id", updatedComment.getCommentId());
        }
        log.info("[CommentService:updateComment] found comment with id {}", updatedComment.getCommentId());
        findComment.updateComment(updatedComment);
        findComment.updateModifiedTime();
        log.info("[CommentService:updateComment] updated comment with id {}", updatedComment.getCommentId());
        return UpdateCommentResponse.toDto(findComment);
    }

    @Transactional
    public RemoveCommentResponse removeComment(Long commentId) {
        Comment findComment = findCommentById(commentId);
        if(findComment == null) {
            log.info("[CommentService:updateComment] comment with id {} not exists", commentId);
            throw new ResourceNotFoundException("comment_id", "comment id", commentId);
        }
        log.info("[CommentService:updateComment] found comment with id {}", commentId);
        RemoveCommentResponse responseDto = RemoveCommentResponse.toDto(findComment);
        em.remove(findComment);
        log.info("[CommentService:updateComment] removed comment with id {}", commentId);
        return responseDto;

    }


    /**************************
     * COMMENT SEARCH METHODS *
     **************************/

    @Transactional(readOnly = true)
    public Comment findCommentById(Long commentId) {
        Comment findComment = commentRepository.findCommentById(commentId);
        if(findComment == null) {
            log.info("[CommentService:findCommentById] comment with id {} not exists", commentId);
            throw new ResourceNotFoundException("Comment", "commentId", commentId);
        }
        log.info("[CommentService:findCommentById] found with id {}", commentId);
        return findComment;
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByMemberId(Long memberId) {
        Member findMember = memberRepository.findOne(memberId);
        if(findMember == null) {
            log.info("[CommentService:findCommentByMemberId] member with id {} not exists", memberId);
            new ResourceNotFoundException("member id", "memberId", memberId);
        }
        log.info("[CommentService:findCommentByMemberId] found member with id", memberId);

        List<Comment> commentList = commentRepository.findCommentByMemberId(memberId);
        if(commentList == null) {
            log.info("[CommentService:findCommentByMemberId] couldn't found comments wrote by member with id {}", memberId);
        } else {
            log.info("[CommentService:findCommentByMemberId] found {}ea comments wrote by member with id {}", commentList.size(), memberId);
        }

        return commentList;
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByPostId(Long postId) {
        Posts findPost = postRepository.findOne(postId);
        if(findPost == null) {
            log.info("[CommentService:findCommentByPostId] post with id {} not exists", postId);
        } else {
            log.info("[CommentService:findCommentByPostId] found post with id {}", postId);
        }

        List<Comment> findComments = commentRepository.findCommentByPostId(postId);
        if(findComments == null) {
            log.info("[CommentService:findCommentByPostId] couldn't found comments for post with id {}", postId);
        } else {
            log.info("[CommentService:findCommentByMemberId] found {}ea comments for post with id {}", findComments.size(), postId);
        }
        return findComments;
    }

}
