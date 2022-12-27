package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.ImageData;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.response.CommentDto;
import Tamanegiseoul.comeet.dto.member.response.ImageDto;
import Tamanegiseoul.comeet.repository.CommentRepository;
import Tamanegiseoul.comeet.repository.ImageDataRepository;
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
    private final PostService postService;
    private final MemberService memberService;
    private final ImageDataRepository imageDataRepository;

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


    /***********************
     * DTO TRANSFER METHODS *
     ***********************/

    @Transactional(readOnly = true)
    public CommentDto toDto(Comment comment) {

        // Member 프록시의 Fetching을 위한 강제 Member 조회
        Member findMember = memberService.findMemberById(comment.getMember().getMemberId());
        log.warn("FOUND MEMBER IS " + findMember.getMemberId());
        ImageData findImage = imageDataRepository.findByMemberId(findMember.getMemberId());
        log.warn("FOUND IMAGE IS " + findImage);
        ImageDto commentProfile = ImageDto.toDto(findImage);

        log.warn("FOUND MEMBER ID IS " + findMember.getMemberId());
        //log.warn("FOUND MEMBER PROFILE IS " + imageDataRepository.findByMemberId(findMember.getMemberId()).getImageId());

        return CommentDto.builder()
                .commentId(comment.getCommentId())
                .postId(comment.getCommentId())
                .commenterNickname(findMember.getNickname())
                .commenterId(findMember.getMemberId())
                .content(comment.getContent())
                .createdTime(comment.getCreatedTime())
                .modifiedTime(comment.getModifiedTime())
                .commenterProfile(commentProfile)
                .build();
    }

    @Transactional(readOnly = true)
    public List<CommentDto> commentListToDto(List<Comment> commentList) {
        log.info("[SearchCommentResponse:commentToDto] commentList param's size is "+commentList.size());
        List<CommentDto> commentDtoList = new ArrayList<>();
        for(Comment c : commentList) {
            commentDtoList.add(toDto(c));
        }
        return commentDtoList;
    }

}
