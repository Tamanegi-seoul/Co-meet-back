package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.comment.request.CreateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.RemoveCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.response.CreateCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.RemoveCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.SearchCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.UpdateCommentResponse;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/comment")
@Tag(name = "Comment API", description = "덧글 관련 CRUD 기능 제공")
public class CommentApiController {
    private final MemberService memberService;
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/register")
    @Operation(summary = "덧글 등록", description = "포스트에 대한 덧글 작성")
    public ResponseEntity<ApiResponse> registerComment(@RequestBody @Valid CreateCommentRequest request) {
        try {
            Posts findPost = postService.findPostById(request.getPostId());
            Member findMember = memberService.findMemberById(request.getMemberId());

            Comment newComment = Comment.builder()
                    .post(findPost)
                    .member(findMember)
                    .content(request.getContent())
                    .build();
            commentService.registerComment(newComment);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_COMMNET, CreateCommentResponse.toDto(newComment));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/update")
    @Operation(summary = "덧글 수정", description = "포스트에 작성된 덧글 수정")
    public ResponseEntity<ApiResponse> updateComment(@RequestBody @Valid UpdateCommentRequest request) {
        try {
            Comment updatedComment = commentService.updateComment(request);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_COMMNET, UpdateCommentResponse.toDto(updatedComment));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    @Operation(summary = "덧글 삭제", description = "포스트에 작성된 덧글 삭제")
    public ResponseEntity<ApiResponse> removeComment(@RequestBody @Valid RemoveCommentRequest request) {
        try {
            Comment findComment = commentService.findCommentById(request.getCommentId());

            commentService.removeComment(request.getCommentId());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_COMMENT, RemoveCommentResponse.toDto(findComment));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/search")
    @Operation(summary = "덧글 조회", description = "포스트에 작성된 덧글 조회")
    public ResponseEntity<ApiResponse> searchComment(@RequestParam("post_id") Long postId) {
        try {
            Posts findPost = postService.findPostById(postId);
            List<Comment> commentList = commentService.findCommentByPostId(findPost.getPostId());
            log.warn("[CommentApiController:searchComment] comment list size is " + commentList.size());
            SearchCommentResponse response = SearchCommentResponse.builder().commentList(commentService.commentListToDto(commentList)).build();
            log.warn("[CommentApiController:searchComment] response's commentList size is " + response.getCommentList().size());

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_COMMENT, SearchCommentResponse.builder()
                            .commentList(commentService.commentListToDto(commentList))
                            .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }


}
