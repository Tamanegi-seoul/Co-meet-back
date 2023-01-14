package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.member.request.JoinMemberRequest;
import Tamanegiseoul.comeet.dto.member.request.UpdateMemberRequest;
import Tamanegiseoul.comeet.dto.member.response.JoinMemberResponse;
import Tamanegiseoul.comeet.dto.member.response.RemoveMemberResponse;
import Tamanegiseoul.comeet.dto.member.response.UpdateMemberResponse;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.CreatePostResponse;
import Tamanegiseoul.comeet.repository.MemberRepository;
import Tamanegiseoul.comeet.service.CommentService;
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

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@EnableWebMvc
@ActiveProfiles("dev")
@Transactional
@Slf4j
public class MemberServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired PostService postService;
    @Autowired CommentService commentService;

    @PersistenceContext
    EntityManager em;

    @Test
    @Transactional
    public void 단일_유저_생성() throws Exception {
        // given
        JoinMemberRequest request = JoinMemberRequest.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.JAVA)))
                .build();

        // when
        memberService.registerMember(request, null);

        // then
        Member findMember = memberService.findMemberByNickname("test_user");
        Assert.assertEquals(findMember.getEmail(), "testuser@gmail.com");
    }

    @Test(expected = DuplicateResourceException.class)
    @Transactional
    public void 유저_이메일_중복검사() throws IOException {
        // given
        JoinMemberResponse newMember = memberService.registerMember(JoinMemberRequest.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.PYTHON)))
                .build(), null);

        // when
        JoinMemberResponse otherMember = memberService.registerMember(JoinMemberRequest.builder()
                .nickname("other_user")
                .email("testuser@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.R)))
                .build(), null);

        // then
        Assert.fail("something goes wrong");
    }

    @Test(expected = DuplicateResourceException.class)
    @Transactional
    public void 유저_닉네임_중복검사() throws IOException {
        // given
        JoinMemberRequest newMember = JoinMemberRequest.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.JAVA)))
                .build();
        memberService.registerMember(newMember, null);

        // when
        JoinMemberRequest otherMember = JoinMemberRequest.builder()
                .nickname("test_user")
                .email("otherUser@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.R)))
                .build();
        memberService.registerMember(otherMember, null);

        // then
        Assert.fail("duplicated member resource has been registered");
    }

    @Test
    @Transactional
    public void 기술스택_세팅() throws IOException {
        // given
        JoinMemberRequest newMember = JoinMemberRequest.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.R)))
                .build();
        JoinMemberResponse response = memberService.registerMember(newMember, null);
        Member findMember = memberRepository.findOne(response.getMemberId());

        // when
        memberService.updatePreferStack(findMember, new ArrayList<>(List.of(TechStack.R, TechStack.JAVA_SCRIPT)));

        // then
        List<TechStack> findStacks = memberService.findPreferredStacks(response.getMemberId());
        for(TechStack ts : findStacks) {
            log.info("TechStack : " + ts.name());
        }
        Assert.assertEquals(2, findStacks.size());
    }

    @Test
    @Transactional
    //@Rollback(false)
    public void 기술스택_수정() throws IOException {
        // given
        JoinMemberRequest newMember = JoinMemberRequest.builder()
                .nickname("woogie")
                .email("woogie@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.R, TechStack.JAVA)))
                .build();
        JoinMemberResponse response = memberService.registerMember(newMember, null);

        // when
        UpdateMemberRequest request = UpdateMemberRequest.builder()
                .memberId(response.getMemberId())
                .newNickname(response.getNickname())
                .updatedStacks(new ArrayList<>(List.of(TechStack.R, TechStack.JAVA)))
                .build();
        UpdateMemberResponse updateMemberResponse = memberService.updateMember(request, null);

        // then
        List<TechStack> findStacks = memberService.findPreferredStacks(updateMemberResponse.getMemberId());
        for(TechStack ts : findStacks) {
            log.info("TechStack : " + ts.name()); // should be javascript & python
        }
        Assert.assertEquals("R", findStacks.get(0).name());
        Assert.assertEquals(2, findStacks.size());
    }

    @Test(expected = ResourceNotFoundException.class)
    public void 유저_삭제() throws Exception {
        // given
        JoinMemberRequest newMember = JoinMemberRequest.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.R, TechStack.JAVA)))
                .build();
        JoinMemberResponse response = memberService.registerMember(newMember, null);

        CreatePostRequest request = CreatePostRequest.builder()
                .title("이것은 새로운 포스트입니다.")
                .content("빈 내용")
                .contactType(ContactType.POSTER_EMAIL)
                .contact("93jpark@gmail.com")
                .posterId(response.getMemberId())
                .recruitCapacity(4L)
                .startDate(LocalDate.of(2022, 10, 23))
                .expectedTerm(14L)
                .build();
        CreatePostResponse postResponse = postService.registerPost(request);
        ArrayList<TechStack> stacks = new ArrayList<TechStack>(
                Arrays.asList(TechStack.JAVA, TechStack.SPRING)
        );
        postService.updateDesignateStacks(postResponse.getPostId(), stacks);

        // when
        RemoveMemberResponse removeResponse = memberService.removeMember(response.getMemberId());
        em.flush();
        em.clear();
        log.info("member id {} has been removed", removeResponse.getMemberId());

        // then
        Member findMember = memberService.findMemberById(response.getMemberId());

        log.info("find member:", findMember.getNickname());

        Assert.fail("member has not been removed");
    }
}
