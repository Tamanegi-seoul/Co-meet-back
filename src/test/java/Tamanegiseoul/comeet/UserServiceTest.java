package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.User;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.repository.UserRepository;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PostService postService;
    @Autowired CommentService commentService;

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    public void 단일_유저_생성() throws Exception {
        // given
        User newUser = User.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();

        // when
        userService.registerUser(newUser);

        // then
        User findUser = userService.findUserByNickname("test_user");
        Assert.assertEquals(findUser.getEmail(), "testuser@gmail.com");
    }

    @Test(expected = DuplicateResourceException.class)
    @Transactional
    public void 유저_이메일_중복검사() {
        // given
        User newUser = User.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);

        // when
        User otherUser = User.builder()
                .nickname("other_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(otherUser);

        // then
        Assert.fail("something goes wrong");
    }

    @Test(expected = DuplicateResourceException.class)
    @Transactional
    public void 유저_닉네임_중복검사() {
        // given
        User newUser = User.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);

        // when
        User otherUser = User.builder()
                .nickname("test_user")
                .email("otherUser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(otherUser);

        // then
        Assert.fail("something goes wrong");
    }

    @Test
    @Transactional
    public void 기술스택_세팅() {
        // given
        User newUser = User.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);

        // when
        userService.updatePreferStack(newUser.getUserId(), new ArrayList<>(List.of(TechStack.R, TechStack.JAVA_SCRIPT)));

        // then
        List<TechStack> findStacks = userService.findPreferredStacks(newUser.getUserId());
        for(TechStack ts : findStacks) {
            log.info("TechStack : " + ts.name());
        }
        Assert.assertEquals(2, findStacks.size());
    }

    @Test
    @Transactional
    //@Rollback(false)
    public void 기술스택_수정() {
        // given
        User newUser = User.builder()
                .nickname("woogie")
                .email("woogie@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);
        userService.updatePreferStack(newUser.getUserId(), new ArrayList<>(List.of(TechStack.R, TechStack.JAVA)));


        // when
        userService.updatePreferStack(newUser.getUserId(), new ArrayList<>(List.of(TechStack.JAVA_SCRIPT, TechStack.PYTHON)));

        // then
        List<TechStack> findStacks = userService.findPreferredStacks(newUser.getUserId());
        for(TechStack ts : findStacks) {
            log.info("TechStack : " + ts.name()); // should be javascript & python
        }
        Assert.assertEquals("JAVA_SCRIPT", findStacks.get(0).name());
        Assert.assertEquals(2, findStacks.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void 유저_삭제() throws Exception {
        // given
        User newUser = User.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        Long newUserId = userService.registerUser(newUser);
        userService.updatePreferStack(newUser.getUserId(), new ArrayList<>(List.of(TechStack.R, TechStack.JAVA)));

        Posts newPost = Posts.builder()
                .title("이것은 새로운 포스트입니다.")
                .content("빈 내용")
                .contactType(ContactType.POSTER_EMAIL)
                .contact("93jpark@gmail.com")
                .poster(newUser)
                .recruitCapacity(4L)
                .startDate(LocalDate.of(2022, 10, 23))
                .expectedTerm(14L)
                .build();
        postService.registerPost(newPost);
        postService.updateDesignateStacks(newPost.getPostId(), TechStack.JAVA, TechStack.SPRING);

        // when
        int queryExecuteTimes = userService.removeUser(newUserId);
        em.clear();
        log.info("query executed for " + queryExecuteTimes+ " times");

        // then
        User findUser = userService.findUserById(newUserId);

        log.info("find user:", findUser.getNickname());

        Assert.fail("user has not been removed");
    }
}
