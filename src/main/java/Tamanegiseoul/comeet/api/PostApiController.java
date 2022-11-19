package Tamanegiseoul.comeet.api;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.ApiResponse;
import Tamanegiseoul.comeet.dto.ResponseMessage;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.CreatePostResponse;
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
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    public ResponseEntity<ApiResponse> registerPost(@RequestBody @Valid CreatePostRequest request) {
        try {
            Users findUser = userService.findUserById(request.getPosterId());

            Posts newPost = Posts.builder()
                    .title(request.getTitle())
                    .content(request.getContent())
                    .contactType(request.getContactType())
                    .contact(request.getContact())
                    .poster(findUser)
                    .startDate(request.getStartDate())
                    .recruitCapacity(request.getRecruitCapacity())
                    .expectedTerm(request.getExpectedTerm())
                    .build();


            newPost.updateDesignateStack(request.getDesignatedStacks());
            postService.registerPost(newPost);


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
}
