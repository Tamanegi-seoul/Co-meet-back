package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.User;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.repository.PostRepository;
import Tamanegiseoul.comeet.repository.UserRepository;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class CommentServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;
    @Autowired CommentService commentService;

    @Before
    public void initialize() {
        log.info("test initializer exceuted");
        User newUser = User.builder()
                .email("93jpark@gmail.com")
                .nickname("케네스")
                .password("password")
                .build();
        userService.registerUser(newUser);
        log.info("new user is registered");

        Posts newPost = Posts.builder()
                .poster(newUser)
                .contact("some_open_chat_url_/kakao.xyz")
                .title("NEW POST!")
                .content("this is empty content..")
                .recruitCapacity(4L)
                .contactType(ContactType.KAKAO_OPEN_CHAT)
                .expectedTerm(28L)
                .startDate(LocalDate.of(2024, 10, 23))
                .build();
        postService.registerPost(newPost);
        log.info("new post is registered");
    }

    @Test
    public void 덧글_작성() {
        // given
        Posts findPost = postService.findAll().get(0);
        User findUser = userService.findAll().get(0);

        // when
        Comment newComment = Comment.builder()
                .post(findPost)
                .user(findUser)
                .content("foo boo")
                .build();
        Long commentId = commentService.registerComment(newComment);

        // then
        Comment findComment = commentService.findCommentById(commentId);

        Assert.assertEquals("foo boo", findComment.getContent());
    }

    @Test
    public void 덧글_수정() {
        // given
        Posts findPost = postService.findAll().get(0);
        User findUser = userService.findAll().get(0);

        Comment newComment = Comment.builder()
                .post(findPost)
                .user(findUser)
                .content("foo boo")
                .build();
        Long commentId = commentService.registerComment(newComment);

        // when
        Comment findComment = commentService.findCommentById(commentId);
        UpdateCommentRequest ucr = UpdateCommentRequest.builder()
                .commentId(commentId)
                .content("foo boo foo")
                .build();

        commentService.updateComment(ucr);

        // then
        Assert.assertEquals("foo boo foo", findComment.getContent());
    }

    @Test
    public void 덧글_조회() {
        // given

        User findUser = userService.findUserByNickname("케네스");
        Posts findPost = postService.findPostByUserId(findUser.getUserId()).get(0);

        Comment newComment = Comment.builder()
                .post(findPost)
                .user(findUser)
                .content("foo boo")
                .build();
        Long commentId = commentService.registerComment(newComment);

        // when
        Comment findCommentWithUserId = commentService.findCommentByUserId(findUser.getUserId()).get(0);
        Comment findCommentWithPostId = commentService.findCommentByPostId(findPost.getPostId()).get(0);

        // then
        Assert.assertEquals("foo boo", findCommentWithPostId.getContent());
        Assert.assertEquals("foo boo", findCommentWithUserId.getContent());

    }

    @Test
    public void 덧글_삭제() {
        // given
        Posts findPost = postService.findAll().get(0);
        User findUser = userService.findAll().get(0);

        Comment newComment = Comment.builder()
                .post(findPost)
                .user(findUser)
                .content("foo boo")
                .build();
        Long commentId = commentService.registerComment(newComment);

        // when
        commentService.removeComment(commentId);

        // then
        //Assert.assertEquals(3, commentService.findAll().size());
    }



}
