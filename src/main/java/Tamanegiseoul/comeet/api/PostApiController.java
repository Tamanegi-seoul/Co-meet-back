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
import Tamanegiseoul.comeet.dto.member.response.ImageDto;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.request.RemovePostRequest;
import Tamanegiseoul.comeet.dto.post.request.SearchPostRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.*;
import Tamanegiseoul.comeet.service.*;
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
    private final ImageDataService imageDataService;

    @PostMapping
    @ApiOperation(value="포스트 작성", notes="새로운 포스트 작성")
    public ResponseEntity<ApiResponse> registerPost(@RequestBody @Valid CreatePostRequest request) {
        try {

            CreatePostResponse responseDto = postService.registerPost(request);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_POST, responseDto);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.FAIL_CREATE_POST, e.getMessage());
        }
    }

    @PatchMapping
    @ApiOperation(value="포스트 수정", notes="등록된 포스트 수정")
    public ResponseEntity<ApiResponse> updatePost(@RequestBody @Valid UpdatePostRequest request) {
        try {
            UpdatePostResponse responseDto = postService.updatePost(request);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_POST, responseDto.designatedStacks(request.getDesignatedStacks()));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/all")
    @ApiOperation(value="포스트 전체 조회", notes="등록된 포스트 전체 조회")
    public ResponseEntity<ApiResponse> searchAllPost(@RequestParam(value="offset",defaultValue="0") @Valid int offset, @RequestParam(value="limit",defaultValue="20") @Valid int limit) {
        try {
            List<Posts> findPosts = postService.findAll(offset, limit);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST, postService.toCompactDtoList(findPosts));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @GetMapping("/my")
    @ApiOperation(value="작성자 기반 포스트 조회", notes="특정 회원이 작성한 포스트 모두 조회")
    public ResponseEntity<ApiResponse> searchPostByPosterId(@RequestParam(value="memberId") @Valid Long memberId) {
        try {
            List<Posts> findPosts = postService.findPostByMemberId(memberId);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST, postService.toCompactDtoList(findPosts));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @GetMapping
    @ApiOperation(value="포스트 상세 조회", notes="등록된 포스트 상세 조회")
    public ResponseEntity<ApiResponse> searchPost(@RequestParam("postId") Long postId) {
        try {
            Posts findPost = postService.findPostById(postId);
            Member findPoster = memberService.findMemberById(findPost.getPoster().getMemberId());
            ImageDto findImage = imageDataService.findImageByMemberId(findPoster.getMemberId());
            List<Comment> commentList = commentService.findCommentByPostId(postId);
            List<CommentDto> commentDtoList = commentService.commentListToDto(commentList);
            List<TechStack> techStacks = stackRelationService.findTechStackByPostId(postId);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST,
                            SearchPostResponse.toDto(findPost)
                            .designatedStacks(techStacks)
                            .comments(commentDtoList)
                            .posterProfile(findImage)
            );
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @DeleteMapping
    @ApiOperation(value="포스트 삭제", notes="등록된 포스트 삭제")
    public ResponseEntity<ApiResponse> removePost(@RequestParam(name = "postId")  Long postId) {
        try {
            postService.removePostByPostId(postId);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_POST, RemovePostResponse.builder()
                            .postId(postId)
                            .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

}
