package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.CreatePostResponse;
import Tamanegiseoul.comeet.dto.post.response.PostCompactDto;
import Tamanegiseoul.comeet.dto.post.response.UpdatePostResponse;
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

    private final StackRelationService stackRelationService;

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

        return UpdatePostResponse.toDto(findPost);
    }

    // 포스트 ID를 기반으로 단건 포스트 삭제
    @Transactional
    public void removePostByPostId(Long postId) {
        Posts findPost = postRepository.findOne(postId);
        if(findPost == null) {
            throw new ResourceNotFoundException("post", "postId", postId);
        }
        em.remove(findPost);
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
    public Posts findPostById(Long postId) {
        log.warn("[PostService:findPostById] find method init");
        Posts findPost = postRepository.findOne(postId);
        if(findPost == null) {
            log.warn("[PostService:findPostById] can't find post with given post id");
            throw new ResourceNotFoundException("Posts", "postId", postId);
        } else {
            log.warn("[PostService:findPostById] find post with given post id");
            return findPost;
        }
    }

    @Transactional(readOnly = true)
    public List<Posts> findAll() {
        return postRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<Posts> findAll(int offset, int limit) {
        return postRepository.findAll(offset, limit);
    }

    @Transactional(readOnly = true)
    public List<Posts> findPostByMemberId(Long memberId) {
        Member findMember = memberRepository.findOne(memberId);
        if(findMember == null) {
            throw new ResourceNotFoundException("member_id", "memberId", memberId);
        }
        return postRepository.findPostByMemberId(memberId);
    }


    /***********************
     * POST UPDATE METHODS *
     ***********************/
    @Transactional
    public void updateDesignateStacks(Long postId, List<TechStack> techStacks) {
        Posts findPost = postRepository.findOne(postId);
        //findPost.initDesignateStack();
        findPost.getDesignatedStack().clear();
        for(TechStack stack : techStacks) {
            findPost.addDesignateStack(stack);
        }
    }

    /***********************
     * DTO TRANSFER METHODS *
     ***********************/

    public List<PostCompactDto> toCompactDtoList(List<Posts> postList) {
        List<PostCompactDto> list = new ArrayList<>();
        for(Posts post : postList) {
            PostCompactDto dto = PostCompactDto.toDto(post);
            List<TechStack> techStacks = stackRelationService.findTechStackByPostId(post.getPostId());
            dto.designatedStacks(techStacks);
            list.add(dto);
        }
        return list;
    }

}
