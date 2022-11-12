package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.repository.PostRepository;
import Tamanegiseoul.comeet.repository.UserRepository;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class PostServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;

    @Test
    public void 포스트_생성() {
        // given
        Users newUser = Users.builder()
                .nickname("케네스")
                .email("93jpark@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);

        // when
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
        postService.updateDesignateStacks(newPost.getId(), TechStack.JAVA, TechStack.SPRING);

        // then
        List<Posts> findPosts = postService.findPostByUserId(newUser.getId());
        log.info(findPosts.get(0).printout());
        Assert.assertEquals(1, findPosts.size());
    }

    @Test
    @Rollback(false)
    public void 포스트_수정() {
        // given
        Users newUser = Users.builder()
                .nickname("케네스")
                .email("93jpark@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);
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
        postService.updateDesignateStacks(newPost.getId(), TechStack.JAVA, TechStack.SPRING);

        // when
        UpdatePostRequest updatedPost = UpdatePostRequest.builder()
                .title("이것은 수정된 포스트입니다.")
                .content("빈 내용")
                .contactType(ContactType.KAKAO_OPEN_CHAT)
                .contact("93jpark@gmail.com")
                .recruitStatus(RecruitStatus.RECRUIT)
                .recruitCapacity(8L)
                .startDate(LocalDate.of(2022, 11, 27))
                .expectedTerm(28L)
                .stacks(new ArrayList(Arrays.asList(TechStack.R, TechStack.REACT)))
                .build();

        log.warn(updatedPost.getTitle());

        postService.updatePost(newPost.getId(), updatedPost);

        // then
        Posts findPost = postService.findPostByUserId(newUser.getId()).get(0);
        log.info(findPost.printout());

        Assert.assertEquals("이것은 수정된 포스트입니다.", findPost.getTitle());

    }

}
