package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.User;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.request.RemovePostRequest;
import Tamanegiseoul.comeet.dto.post.request.SearchPostRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.CreatePostResponse;
import Tamanegiseoul.comeet.dto.post.response.RemovePostResponse;
import Tamanegiseoul.comeet.dto.post.response.SearchPostResponse;
import Tamanegiseoul.comeet.dto.post.response.UpdatePostResponse;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.StackRelationService;
import Tamanegiseoul.comeet.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

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
            User findUser = userService.findUserById(request.getPosterId());

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
            Posts findPost = postService.findPostById(request.getPostId());
            postService.updatePost(findPost, request);

            return ApiResponse.of(HttpStatus.OK, ResponseMessage.UPDATE_POST, UpdatePostResponse.toDto(findPost)
                    .designatedStacks(request.getDesignatedStacks()));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.RESOURCE_NOT_FOUND, e.getMessage());
        }
    }

    @GetMapping("/search/all")
    public ResponseEntity<ApiResponse> searchAllPost(@RequestBody @Valid SearchPostRequest request) {
        try {
            log.warn("[PostApiController:searchAllPost]method execute init");
            List<Posts> allPosts = postService.findAll();
            log.warn("found "+allPosts.size()+"ea posts from DB");


            return ApiResponse.of(HttpStatus.OK, ResponseMessage.FOUND_POST, this.toDtoList(allPosts));
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }

    @GetMapping("/search/{post_id}")
    public ResponseEntity<ApiResponse> searchPost(@PathVariable("post_id") Long postId) {
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
            return ApiResponse.of(HttpStatus.OK, ResponseMessage.DELETE_POST, RemovePostResponse.builder()
                            .postId(request.getPostId())
                            .build());
        } catch (ResourceNotFoundException e) {
            return ApiResponse.of(HttpStatus.NOT_FOUND, ResponseMessage.NOT_FOUND_POST, e.getMessage());
        }
    }


    private List<SearchPostResponse> toDtoList(List<Posts> postList) {
        List<SearchPostResponse> list = new ArrayList<>();
        for(Posts post : postList) {
            SearchPostResponse response = SearchPostResponse.toDto(post);
            List<TechStack> techStacks = stackRelationService.findTechStackByPostId(post.getPostId());
            log.warn("[PostApiController:toDtoList]"+techStacks.toString());
            response.designatedStacks(techStacks);
            list.add(response);
        }
        return list;
    }

}
