package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.request.RemovePostRequest;
import Tamanegiseoul.comeet.dto.post.request.SearchPostRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.CreatePostResponse;
import Tamanegiseoul.comeet.dto.post.response.SearchPostResponse;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.StackRelationService;
import Tamanegiseoul.comeet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@Slf4j
@RequestMapping("/api/post")
public class PostApiController {

    private final PostService postService;
    private final UserService userService;
    private final CommentService commentService;
    private final StackRelationService stackRelationService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponse> registerPost(@RequestBody @Valid CreatePostRequest request) {
        try {
            log.warn("[PostApi:registerPost] registerPost init");
            Users findUser = userService.findUserById(request.getPosterId());

            Posts newPost = Posts.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .contactType(request.getContactType())
                    .contact(request.getContact())
                    .poster(findUser)
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
                            .posterNickname(findUser.getNickname())
                            .designatedStacks(request.getDesignatedStacks()));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_USER, e.getMessage());
        } catch (Exception e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.FAIL_CREATE_POST, e.getMessage());
        }
    }

    @PatchMapping("/update")
    public ResponseEntity<ApiResponse> updatePost(@RequestBody @Valid UpdatePostRequest request) {
        try {
            postService.updatePost(request.getId(), request);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_POST, request);
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.BAD_REQUEST, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @GetMapping("/search/all")
    public ResponseEntity<ApiResponse> searchAllPost(@RequestBody @Valid SearchPostRequest request) {
        try {
            List<Posts> allPosts = postService.findAll();

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST, this.toDtoList(allPosts));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @GetMapping("/search/{id}")
    public ResponseEntity<ApiResponse> searchPost(@PathVariable Long postId) {
        try {
            Posts findPost = postService.findPostById(postId);
            List<TechStack> techStacks = stackRelationService.findTechStackByPostId(postId);
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST,
                    SearchPostResponse.toDto(findPost)
                            .designatedStacks(techStacks)
            );
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponse> removePost(@RequestBody @Valid RemovePostRequest request) {
        try {
            postService.removePostByPostId(request.getPostId());
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_POST, request.getPostId());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }


    private List<SearchPostResponse> toDtoList(List<Posts> postList) {
        List<SearchPostResponse> list = new ArrayList<>();
        for(Posts post : postList) {
            SearchPostResponse response = SearchPostResponse.toDto(post);
            List<TechStack> techStacks = stackRelationService.findTechStackByPostId(post.getPostId());
            response.designatedStacks(techStacks);
            list.add(response);
        }
        return list;
    }

}
