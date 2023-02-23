package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.comment.response.CommentDto;
import Tamanegiseoul.comeet.dto.member.response.ImageDto;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.*;
import Tamanegiseoul.comeet.repository.CommentRepository;
import Tamanegiseoul.comeet.repository.MemberRepository;
import Tamanegiseoul.comeet.repository.PostRepository;
import Tamanegiseoul.comeet.repository.StackRelationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class PostService {

    private final CommentRepository commentRepository;
    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final StackRelationRepository stackRelationRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public CreatePostResponse registerPost(CreatePostRequest request) {
        Member findMember = null;
        try {
            findMember = memberRepository.findMemberWithStack(request.getPosterId());
        } catch(EmptyResultDataAccessException e) {
            log.info("[PostService:registerPost] member with member id '{}' not exits", request.getPosterId());
            throw new ResourceNotFoundException("member id", "memberId", request.getPosterId());
        }
        log.info("[PostService:registerPost] found member with member id '{}'", request.getPosterId());

        Posts newPost = Posts.builder()
                .title(request.getTitle())
                .content(request.getContent())
                .groupType(request.getGroupType())
                .contactType(request.getContactType())
                .contact(request.getContact())
                .poster(findMember)
                .remote(request.getRemote())
                .startDate(request.getStartDate())
                .expectedTerm(request.getExpectedTerm())
                .recruitCapacity(request.getRecruitCapacity())
                .groupType(request.getGroupType())
                .build();

        newPost.updateModifiedDate();
        newPost.updateCreatedDate();

        postRepository.save(newPost);
        log.info("[PostService:registerPost] register post with title '{}' wrote by '{}'", newPost.getTitle(), newPost.getPoster().getNickname());

        if(request.getDesignatedStacks() == null) {
            log.info("[PostService:registerPost] designated stacks are not provided.");
        } else {
            log.info("[PostService:registerPost] designated stacks are provided: {}", request.getDesignatedStacks().toString());
            updateDesignateStacks(newPost, request.getDesignatedStacks());
        }

        em.flush();
        log.warn("check");
        findMember.addWrotePost(newPost);
        return CreatePostResponse.toDto(newPost);
    }

    /***********************
     * UPDATE POST METHODS *
     ***********************/

    @Transactional
    public UpdatePostResponse updatePost(UpdatePostRequest updatedPost) {
        Posts findPost;
        try {
            findPost = postRepository.findPostWithStackAndPoster(updatedPost.getPostId());
        } catch (EmptyResultDataAccessException e) {
            log.info("[PostService:updatePost] post with post id '{}' not exists", updatedPost.getPostId());
            throw new ResourceNotFoundException("postId", "post id", updatedPost.getPostId());
        }

        log.info("[PostService:updatePost] found post with post id '{}'", updatedPost.getPostId());
        findPost.updatePost(updatedPost);
        findPost.updateDesignateStack(updatedPost.getDesignatedStacks());
        findPost.updateModifiedDate();
        log.info("[PostService:updatePost] updated post with post id '{}'", updatedPost.getPostId());

        return UpdatePostResponse.toDto(findPost).designatedStacks(findPost.exportTechStack());
    }

    // 포스트 ID를 기반으로 단건 포스트 삭제
    @Transactional
    public RemovePostResponse removePostByPostId(Long postId) {
        Posts findPost = postRepository.findOne(postId);
        if(findPost == null) {
            log.info("[PostService:removePostByPostId] post with post id '{}' not exists", postId);
            throw new ResourceNotFoundException("post", "postId", postId);
        }
        log.info("[PostService:removePostByPostId] found post with post id '{}'", postId);
//        em.remove(findPost);
        int removeCommentNum = commentRepository.removeCommentByPostId(findPost.getPostId());
        log.info("[PostService:removePostByPostId] removed {}ea from Comment", removeCommentNum);
        int removeStackNum = stackRelationRepository.removeRelatedStacksByPost(findPost.getPostId());
        log.info("[PostService:removePostByPostId] removed {}ea from StackRelation", removeStackNum);
        postRepository.removePostByPostId(findPost.getPostId());
        log.info("[PostService:removePostByPostId] removed post id '{}'", postId);
        return RemovePostResponse.builder()
                .postId(findPost.getPostId())
                .build();
    }

    // 회원이 작성한 모든 포스트 삭제
    @Transactional
    public void removePostByPosterId(Long memberId) {
        Member findMember = memberRepository.findOne(memberId);
        if(findMember == null) {
            log.info("[PostService:removePostByPosterId] member with member id '{}' not exists", memberId);
            throw new ResourceNotFoundException("Member", "memberId", memberId);
        }
        log.info("[PostService:removePostByPosterId] found member with member id '{}'", memberId);
        List<Posts> findPosts = postRepository.findPostByMemberId(memberId);
        log.info("[PostService:removePostByPosterId] member with member id '{}' not exists", memberId);
        for(Posts p : findPosts) {
            em.remove(p);
        }
    }

    /***********************
     * SEARCH POST METHODS *
     ***********************/
    @Transactional(readOnly = true)
    public SearchPostResponse findPostById(Long postId) {
        Posts findPost = null;
        try {
            findPost = postRepository.findPostWithStackAndPoster(postId);
        } catch (EmptyResultDataAccessException e) {
            log.info("[PostService:findPostById] post with post id '{}' not exits", postId);
            throw new ResourceNotFoundException("Posts", "postId", postId);
        }
        log.info("[PostService:findPostById] found post with post id '{}'", postId);

        Member findPoster = findPost.getPoster();
        log.info("[PostService:findPostById] post with post id '{}' wrote by '{}' member", postId, findPoster.getNickname());


        List<Comment> commentList = commentRepository.findCommentByPostId(postId);
        log.info("[PostService:findPostById] post with post id '{}' has {} comments", findPost.getPostId(), commentList.size());
        List<CommentDto> commentDtoList = new ArrayList<>();

        for(Comment comment : commentList) {
            Member commentWriter = comment.getMember();
            ImageDto commenterProfile = ImageDto.toDto(commentWriter.getProfileImage());
            log.info("[PostService:findPostById] fetching comment wrote by {}", commentWriter.getNickname());
            commentDtoList.add(CommentDto.toDto(comment.getMember(), findPost, commenterProfile, comment));
        }

        return SearchPostResponse.toDto(findPost)
                .comments(commentDtoList);
    }

    @Transactional(readOnly = true)
    public List<PostCompactDto> findAll(int offset, int limit) {
        log.info("[PostService:findAll] fetching all registered post from {} to {}", offset, limit);
        List<Posts> findPosts = postRepository.findAll(offset, limit);
        log.info("[PostService:findAll] found {} ea posts from database", findPosts.size());
        return PostCompactDto.toCompactDtoList(findPosts);
    }

    @Transactional(readOnly = true)
    public List<PostCompactDto> findPostByMemberId(Long memberId) {
        Member findMember = memberRepository.findMemberWithStack(memberId);
        if(findMember == null) {
            log.info("[PostService:findPostByMemberId] member with member id {} not exists", memberId);
            throw new ResourceNotFoundException("member id", "memberId", memberId);
        }
        log.info("[PostService:findPostByMemberId] found member with member id {} ", memberId);

        List<Posts> postList = postRepository.findPostByMemberId(memberId);
        log.info("[PostService:findPostByMemberId] {} wrote {} posts", findMember.getNickname(), postList.size());
        List<PostCompactDto> postCompactDtos = PostCompactDto.toCompactDtoList(postList);

        return postCompactDtos;
    }


    /***********************
     * POST UPDATE METHODS *
     ***********************/
    public void updateDesignateStacks(Posts findPost, List<TechStack> techStacks) {
        if(findPost.exportTechStack() == null) {
            log.info("[PostService:updateDesignateStacks] post with id {} has no registered tech stack", findPost.getPostId());
        } else {
            log.info("[PostService:updateDesignateStacks] before update, post with id {} has {}ea stacks: {}", findPost.getPostId(), findPost.getDesignatedStack().size(), findPost.exportTechStack().toString());
        }
        findPost.getDesignatedStack().clear();

        for(TechStack stack : techStacks) {
            findPost.addDesignateStack(stack);
        }
        log.info("[PostService:updateDesignateStacks] after update, post with id {} has {}ea stacks: {}", findPost.getPostId(), findPost.getDesignatedStack().size(), findPost.exportTechStack().toString());
    }

}
