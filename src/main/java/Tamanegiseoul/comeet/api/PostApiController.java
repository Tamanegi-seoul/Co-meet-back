package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.comment.response.CommentDto;
import Tamanegiseoul.comeet.dto.member.request.JoinMemberRequest;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.request.RemovePostRequest;
import Tamanegiseoul.comeet.dto.post.request.SearchPostRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.*;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.StackRelationService;
import Tamanegiseoul.comeet.service.MemberService;
import io.swagger.annotations.ApiOperation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/post")
@Tag(name = "Post API", description = "게시글 관련 CRUD 기능 제공")
public class PostApiController {

    private final PostService postService;
    private final MemberService memberService;
    private final CommentService commentService;
    private final StackRelationService stackRelationService;

    @PostMapping("/register")
    @ApiOperation(value="포스트 작성", notes="새로운 포스트 작성")
    public ResponseEntity<ApiResponse> registerPost(@RequestBody @Valid CreatePostRequest request) {
        try {
            log.warn("[PostApi:registerPost] registerPost init");
            Member findMember = memberService.findMemberById(request.getPosterId());

            Posts newPost = Posts.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .groupType(request.getGroupType())
                    .contactType(request.getContactType())
                    .contact(request.getContact())
                    .poster(findMember)
                    .startDate(request.getStartDate())
                    .expectedTerm(request.getExpectedTerm())
                    .recruitCapacity(request.getRecruitCapacity())
                    .build();

            log.warn("[PostApi:registerPost] successfully build Post entity");
            postService.registerPost(newPost);
            log.warn("[PostApi:registerPost] successfully registered");

            newPost.updateDesignateStack(request.getDesignatedStacks());
            log.warn("[PostApi:registerPost] updated designate stacks");

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_POST,
                    CreatePostResponse.toDto(newPost)
                            .posterNickname(findMember.getNickname())
                            .designatedStacks(request.getDesignatedStacks()));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.FAIL_CREATE_POST, e.getMessage());
        }
    }

    @PatchMapping("/update")
    @ApiOperation(value="포스트 수정", notes="등록된 포스트 수정")
    public ResponseEntity<ApiResponse> updatePost(@RequestBody @Valid UpdatePostRequest request) {
        try {
            Posts findPost = postService.findPostById(request.getPostId());
            postService.updatePost(findPost, request);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_POST, UpdatePostResponse.toDto(findPost)
                    .designatedStacks(request.getDesignatedStacks()));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/search/all")
    @ApiOperation(value="포스트 전체 조회", notes="등록된 포스트 전체 조회")
    public ResponseEntity<ApiResponse> searchAllPost(@RequestParam(value="offset",defaultValue="0") @Valid int offset, @RequestParam(value="limit",defaultValue="20") @Valid int limit) {
        try {
            log.warn("[PostApiController:searchAllPost]method execute init");
            //List<Posts> allPosts = postService.findAll();
            List<Posts> findPosts = postService.findAll(offset, limit);
            log.warn("found "+findPosts.size()+"ea posts from DB");

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST, this.toCompactDtoList(findPosts));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @GetMapping("/search")
    @ApiOperation(value="포스트 상세 조회", notes="등록된 포스트 상세 조회")
    public ResponseEntity<ApiResponse> searchPost(@RequestParam("post_id") Long postId) {
        try {
            Posts findPost = postService.findPostById(postId);
            List<CommentDto> commentDtoList = toCommentDtoList(commentService.findCommentByPostId(postId));
            List<TechStack> techStacks = stackRelationService.findTechStackByPostId(postId);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST,
                    SearchPostResponse.toDto(findPost)
                            .designatedStacks(techStacks)
                            .comments(commentDtoList)
            );
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    @ApiOperation(value="포스트 삭제", notes="등록된 포스트 삭제")
    public ResponseEntity<ApiResponse> removePost(@RequestBody @Valid RemovePostRequest request) {
        try {
            postService.removePostByPostId(request.getPostId());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_POST, RemovePostResponse.builder()
                            .postId(request.getPostId())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }


    private List<PostCompactDto> toCompactDtoList(List<Posts> postList) {
        List<PostCompactDto> list = new ArrayList<>();
        for(Posts post : postList) {
            PostCompactDto dto = PostCompactDto.toDto(post);
            List<TechStack> techStacks = stackRelationService.findTechStackByPostId(post.getPostId());
            log.warn("[PostApiController:toDtoList]"+techStacks.toString());
            dto.designatedStacks(techStacks);
            list.add(dto);
        }
        return list;
    }

    private List<CommentDto> toCommentDtoList(List<Comment> comments) {
        List<CommentDto> dtos = new ArrayList<>();
        for(Comment c : comments) {
            dtos.add(CommentDto.toDto(c));
        }
        return dtos;
    }

}
