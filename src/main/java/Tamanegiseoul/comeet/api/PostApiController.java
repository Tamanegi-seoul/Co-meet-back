package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.*;
import Tamanegiseoul.comeet.service.*;
import io.swagger.annotations.ApiOperation;
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
@RequestMapping("/api/post")
@Tag(name = "Post API", description = "게시글 관련 CRUD 기능 제공")
public class PostApiController {

    private final PostService postService;

    @PostMapping
    @ApiOperation(value="포스트 작성", notes="새로운 포스트 작성", tags = "Post API")
    public ResponseEntity<ApiResponse> registerPost(@RequestBody @Valid CreatePostRequest request) {
        try {
            log.info("[PostApiController:registerPost] controller execute");
            CreatePostResponse responseDto = postService.registerPost(request);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.CREATED_POST, responseDto);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.FAIL_CREATE_POST, e.getMessage());
        }
    }

    @PatchMapping
    @ApiOperation(value="포스트 수정", notes="등록된 포스트 수정", tags = "Post API")
    public ResponseEntity<ApiResponse> updatePost(@RequestBody @Valid UpdatePostRequest request) {
        try {
            log.info("[PostApiController:updatePost] controller execute");
            UpdatePostResponse responseDto = postService.updatePost(request);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_POST, responseDto);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/all")
    @ApiOperation(value="포스트 전체 조회", notes="등록된 포스트 전체 조회", tags = "Post API")
    public ResponseEntity<ApiResponse> searchAllPost(@RequestParam(value="offset", defaultValue="0") @Valid int offset, @RequestParam(value="limit",defaultValue="20") @Valid int limit) {
        try {
            log.info("[PostApiController:searchAllPost] controller execute");
            List<PostCompactDto> findPosts = postService.findAll(offset, limit);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST, findPosts);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @GetMapping("/my")
    @ApiOperation(value="작성자 기반 포스트 조회", notes="특정 회원이 작성한 포스트 모두 조회", tags = "Post API")
    public ResponseEntity<ApiResponse> searchPostByPosterId(@RequestParam(value="memberId") @Valid Long memberId) {
        try {
            log.info("[PostApiController:searchPostByPosterId] controller execute");
            List<PostCompactDto> responseDtos = postService.findPostByMemberId(memberId);//
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST, responseDtos);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @GetMapping
    @ApiOperation(value="포스트 상세 조회", notes="등록된 포스트 상세 조회", tags = "Post API")
    public ResponseEntity<ApiResponse> searchPost(@RequestParam("postId") Long postId) {
        try {
            log.info("[PostApiController:searchPost] controller execute");
            SearchPostResponse response = postService.findPostById(postId);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST, response);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @DeleteMapping
    @ApiOperation(value="포스트 삭제", notes="등록된 포스트 삭제", tags = "Post API")
    public ResponseEntity<ApiResponse> removePost(@RequestParam(name = "postId")  Long postId) {
        try {
            log.info("[PostApiController:removePost] controller execute");
            RemovePostResponse removePostResponse = postService.removePostByPostId(postId);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_POST, removePostResponse);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

}
