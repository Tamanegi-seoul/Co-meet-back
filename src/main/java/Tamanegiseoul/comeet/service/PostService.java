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

        Member findMember = memberRepository.findOne(request.getPosterId());
        if(findMember == null) {
            throw new ResourceNotFoundException("member id", "memberId", request.getPosterId());
        }

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
                .build();

        newPost.updateModifiedDate();
        newPost.updateCreatedDate();

        postRepository.save(newPost);
        updateDesignateStacks(newPost, request.getDesignatedStacks());
        em.flush();
        log.warn("check");
        findMember.addWrotePost(newPost);
        return CreatePostResponse.toDto(newPost)
                .posterNickname(findMember.getNickname())
                .designatedStacks(request.getDesignatedStacks());
    }

    /***********************
     * UPDATE POST METHODS *
     ***********************/

    @Transactional
    public UpdatePostResponse updatePost(UpdatePostRequest updatedPost) {
        Posts findPost = postRepository.findOne(updatedPost.getPostId());
        if(findPost == null) {
            throw new ResourceNotFoundException("postId", "post id", updatedPost.getPostId());
        }
        findPost.updatePost(updatedPost);
        findPost.updateDesignateStack(updatedPost.getDesignatedStacks());
        findPost.updateModifiedDate();

        return UpdatePostResponse.toDto(findPost).designatedStacks(findPost.exportTechStack());
    }

    // 포스트 ID를 기반으로 단건 포스트 삭제
    @Transactional
    public RemovePostResponse removePostByPostId(Long postId) {
        Posts findPost = postRepository.findOne(postId);
        if(findPost == null) {
            throw new ResourceNotFoundException("post", "postId", postId);
        }
        em.remove(findPost);
        return RemovePostResponse.builder()
                .postId(findPost.getPostId())
                .build();
    }

    // 회원이 작성한 모든 포스트 삭제
    @Transactional
    public void removePostByPosterId(Long memberId) {
        List<Posts> findPosts = postRepository.findPostByMemberId(memberId);
        for(Posts p : findPosts) {
            em.remove(p);
        }
    }

    /***********************
     * SEARCH POST METHODS *
     ***********************/
    @Transactional(readOnly = true)
    public SearchPostResponse findPostById(Long postId) {
        Posts findPost = postRepository.findOne(postId);

        if(findPost == null) { throw new ResourceNotFoundException("Posts", "postId", postId); }

        Member findPoster = findPost.getPoster();

        ImageDto findProfileImage = ImageDto.toDto(findPoster.getProfileImage());

        List<Comment> commentList = commentRepository.findCommentByPostId(postId);
        List<CommentDto> commentDtoList = new ArrayList<>();

        for(Comment comment : commentList) {
            Member commentWriter = comment.getMember();
            ImageDto commenterProfile = ImageDto.toDto(comment.getMember().getProfileImage());

            commentDtoList.add(CommentDto.toDto(comment.getMember(), findPost, commenterProfile, comment));
        }


        return SearchPostResponse.toDto(findPost)
                .designatedStacks(findPost.exportTechStack())
                .comments(commentDtoList)
                .posterProfile(findProfileImage);
    }

    @Transactional(readOnly = true)
    public List<PostCompactDto> findAll(int offset, int limit) {
        List<Posts> findPosts = postRepository.findAll(offset, limit);
        return PostCompactDto.toCompactDtoList(findPosts);
    }

    @Transactional(readOnly = true)
    public List<PostCompactDto> findPostByMemberId(Long memberId) {
        Member findMember = memberRepository.findOne(memberId);
        if(findMember == null) {
            throw new ResourceNotFoundException("member_id", "memberId", memberId);
        }

        List<Posts> postList = postRepository.findPostByMemberId(memberId);
        List<PostCompactDto> postCompactDtos = PostCompactDto.toCompactDtoList(postList);

        return postCompactDtos;
    }


    /***********************
     * POST UPDATE METHODS *
     ***********************/
    @Transactional
    public void updateDesignateStacks(Posts findPost, List<TechStack> techStacks) {
        findPost.getDesignatedStack().clear();
        for(TechStack stack : techStacks) {
            findPost.addDesignateStack(stack);
        }
    }

    /***********************
     * DTO TRANSFER METHODS *
     ***********************/



}
