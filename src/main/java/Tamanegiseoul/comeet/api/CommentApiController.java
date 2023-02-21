package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.comment.request.CreateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.response.CreateCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.RemoveCommentResponse;
import Tamanegiseoul.comeet.dto.comment.response.UpdateCommentResponse;
import Tamanegiseoul.comeet.service.CommentService;
import io.swagger.annotations.ApiResponses;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/comment")
@Tag(name = "Comment API", description = "덧글 관련 CRUD 기능 제공")
public class CommentApiController {
    private final CommentService commentService;

    @PostMapping
    @Operation(summary = "덧글 등록", description = "포스트에 대한 덧글 작성", tags = "Comment API")
    public ResponseEntity<ApiResponse> registerComment(@RequestBody @Valid CreateCommentRequest request) {
        try {
            log.info("[CommentApiController:registerComment] controller execute");
            CreateCommentResponse responseDto = commentService.registerComment(request);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_COMMNET, responseDto);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @PatchMapping
    @Operation(summary = "덧글 수정", description = "포스트에 작성된 덧글 수정", tags = "Comment API")
    public ResponseEntity<ApiResponse> updateComment(@RequestBody @Valid UpdateCommentRequest request) {
        try {
            log.info("[CommentApiController:updateComment] controller execute");
            UpdateCommentResponse responseDto = commentService.updateComment(request);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_COMMNET, responseDto);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @DeleteMapping
    @Operation(summary = "덧글 삭제", description = "포스트에 작성된 덧글 삭제", tags = "Comment API")
    public ResponseEntity<ApiResponse> removeComment(@RequestParam(name = "commentId") Long commentId) {
        try {
            log.info("[CommentApiController:removeComment] controller execute");
            RemoveCommentResponse responseDto = commentService.removeComment(commentId);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_COMMENT, responseDto);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }


}
