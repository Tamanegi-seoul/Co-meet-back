package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.comment.request.CreateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.RemoveCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.SearchCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.response.CreateCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.SearchCommentResponse;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/comment")
public class CommentApiController {
    private final UserService userService;
    private final PostService postService;
    private final CommentService commentService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerComment(@RequestBody @Valid CreateCommentRequest request) {
        try {
            Posts findPost = postService.findPostById(request.getPostId());
            Users findUser = userService.findUserById(request.getUserId());

            Comment newComment = Comment.builder()
                    .post(findPost)
                    .user(findUser)
                    .content(request.getContent())
                    .build();
            commentService.registerComment(newComment);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_COMMNET, CreateCommentResponse.toDto(newComment));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updateComment(@RequestBody @Valid UpdateCommentRequest request) {
        try {
            Comment updatedComment = commentService.updateComment(request);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_COMMNET, CreateCommentResponse.toDto(updatedComment));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removeComment(@RequestBody @Valid RemoveCommentRequest request) {
        try {
            commentService.removeComment(request.getCommentId());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_COMMENT, request.getCommentId());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/search")
    public ResponseEntity<ApiResponse> searchComment(@RequestBody @Valid SearchCommentRequest request) {
        try {
            List<Comment> commentList = commentService.findCommentByPostId(request.getPostId());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_COMMENT, SearchCommentResponse.builder()
                            .commentList(SearchCommentResponse.commentToDto(commentList))
                            .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }


}
