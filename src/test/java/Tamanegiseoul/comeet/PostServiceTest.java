package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.GroupType;
import Tamanegiseoul.comeet.domain.enums.RecruitStatus;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.member.request.JoinMemberRequest;
import Tamanegiseoul.comeet.dto.member.response.JoinMemberResponse;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.request.UpdatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.CreatePostResponse;
import Tamanegiseoul.comeet.repository.PostRepository;
import Tamanegiseoul.comeet.repository.MemberRepository;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@EnableWebMvc
@ActiveProfiles("dev")

@Slf4j
public class PostServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;

    @Test
    public void 포스트_생성() throws IOException {
        // given
        JoinMemberRequest newMember = JoinMemberRequest.builder()
                .nickname("케네스")
                .email("93jpark@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.JAVA, TechStack.SPRING)))
                .build();
        JoinMemberResponse response = memberService.registerMember(newMember, null);

        // when
        CreatePostRequest requset = CreatePostRequest.builder()
                .title("이것은 새로운 포스트입니다.")
                .content("빈 내용")
                .contactType(ContactType.POSTER_EMAIL)
                .contact("93jpark@gmail.com")
                .posterId(response.getMemberId())
                .recruitCapacity(4L)
                .remote(false)
                .startDate(LocalDate.of(2022, 10, 23))
                .expectedTerm(14L)
                .designatedStacks(new ArrayList<>(List.of(TechStack.JAVA, TechStack.SPRING)))
                .build();
        CreatePostResponse postResponse = postService.registerPost(requset);
        ArrayList<TechStack> stacks = new ArrayList<TechStack>(
                Arrays.asList(TechStack.JAVA, TechStack.SPRING)
        );
        postService.updateDesignateStacks(postResponse.getPostId(), stacks);

        // then
        List<Posts> findPosts = postService.findPostByMemberId(response.getMemberId());
        log.info(findPosts.get(0).printout());
        Assert.assertEquals(1, findPosts.size());
    }

    @Test
    public void 포스트_수정() throws IOException {
        // given
        JoinMemberRequest newMember = JoinMemberRequest.builder()
                .nickname("케네스")
                .email("93jpark@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.JAVA, TechStack.SPRING)))
                .build();
        JoinMemberResponse memberResponse = memberService.registerMember(newMember, null);
        CreatePostRequest request = CreatePostRequest.builder()
                .title("이것은 새로운 포스트입니다.")
                .content("빈 내용")
                .contactType(ContactType.POSTER_EMAIL)
                .contact("93jpark@gmail.com")
                .posterId(memberResponse.getMemberId())
                .remote(false)
                .recruitCapacity(4L)
                .startDate(LocalDate.of(2022, 10, 23))
                .expectedTerm(14L)
                .designatedStacks(new ArrayList<>(List.of(TechStack.JAVA, TechStack.SPRING)))
                .build();
        CreatePostResponse response = postService.registerPost(request);

        // when
        UpdatePostRequest updatedPost = new UpdatePostRequest(
                response.getPostId(),
                "이것은 수정된 포스트입니다.",
                GroupType.STUDY,
                "빈 내용",
                RecruitStatus.DONE,
                8L,
                ContactType.POSTER_EMAIL,
                "93jpark@gmail.com",
                false,
                LocalDate.of(2022, 11, 27),
                28L,
                new ArrayList(Arrays.asList(TechStack.R, TechStack.REACT))
        );

        log.warn(updatedPost.getTitle());

        postService.updatePost(updatedPost);

        // then
        Posts findPost = postService.findPostByMemberId(memberResponse.getMemberId()).get(0);
        log.info(findPost.printout());

        Assert.assertEquals("이것은 수정된 포스트입니다.", findPost.getTitle());

    }

}
