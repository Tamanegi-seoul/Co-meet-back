package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.ImageData;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.comment.request.CreateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.response.CommentDto;
import Tamanegiseoul.comeet.dto.comment.response.CreateCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.RemoveCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.UpdateCommentResponse;
import Tamanegiseoul.comeet.dto.member.response.ImageDto;
import Tamanegiseoul.comeet.repository.CommentRepository;
import Tamanegiseoul.comeet.repository.ImageDataRepository;
import Tamanegiseoul.comeet.repository.MemberRepository;
import Tamanegiseoul.comeet.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class CommentService {
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;

    private final MemberRepository memberRepository;
    private final ImageDataRepository imageDataRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public CreateCommentResponse registerComment(CreateCommentRequest request) {

        Posts findPost = postRepository.findOne(request.getPostId());
        if(findPost==null) throw new ResourceNotFoundException("post id", "postId", request.getPostId());

        Member findMember = memberRepository.findOne(request.getMemberId());
        if(findMember==null) throw new ResourceNotFoundException("member id", "memberId", request.getMemberId());

        Comment newComment = Comment.builder()
                .post(findPost)
                .member(findMember)
                .content(request.getContent())
                .build();
        newComment.updateCreatedTime();
        newComment.updateModifiedTime();
        commentRepository.save(newComment);

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
            throw new ResourceNotFoundException("comment_id", "comment id", updatedComment.getCommentId());
        }
        findComment.updateComment(updatedComment);
        findComment.updateModifiedTime();
        return UpdateCommentResponse.toDto(findComment);
    }

    @Transactional
    public RemoveCommentResponse removeComment(Long commentId) {
        Comment findComment = findCommentById(commentId);
        if(findComment == null) {
            throw new ResourceNotFoundException("comment_id", "comment id", commentId);
        }
        RemoveCommentResponse responseDto = RemoveCommentResponse.toDto(findComment);
        em.remove(findComment);
        return responseDto;

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
    public List<Comment> findCommentByMemberId(Long memberId) {
        return commentRepository.findCommentByMemberId(memberId);
    }

    @Transactional(readOnly = true)
    public List<Comment> findCommentByPostId(Long postId) {
        log.warn("[CommentServce:findCommentByPostId]method init");
        List<Comment> findComments = commentRepository.findCommentByPostId(postId);
        log.warn("[CommentServce:findCommentByPostId]" + " found " + findComments.size() + "ea comments for " + postId);
        return findComments;
    }

}
