package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.comment.request.CreateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.request.UpdateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.response.CreateCommentResponse;
import Tamanegiseoul.comeet.dto.member.request.JoinMemberRequest;
import Tamanegiseoul.comeet.dto.member.response.JoinMemberResponse;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.PostCompactDto;
import Tamanegiseoul.comeet.repository.PostRepository;
import Tamanegiseoul.comeet.repository.MemberRepository;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.PostService;
import Tamanegiseoul.comeet.service.MemberService;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Before;
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
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
@EnableWebMvc
@Transactional
@Slf4j
@ActiveProfiles("dev")
public class CommentServiceTest {

    @Autowired
    MemberService memberService;
    @Autowired
    MemberRepository memberRepository;
    @Autowired PostService postService;
    @Autowired PostRepository postRepository;
    @Autowired CommentService commentService;

    @Before
    public void initialize() throws IOException {
        log.info("test initializer exceuted");
        JoinMemberResponse newMemberRequest = memberService.registerMember(JoinMemberRequest.builder()
                .email("93jpark@gmail.com")
                .nickname("케네스")
                .password("password")
                .preferStacks(new ArrayList<>(List.of(TechStack.JAVA, TechStack.SPRING)))
                .build(), null);
        log.info("new Member is registered");

        CreatePostRequest newPostRequest = CreatePostRequest.builder()
                .posterId(newMemberRequest.getMemberId())
                .contact("some_open_chat_url_/kakao.xyz")
                .title("NEW POST!")
                .content("this is empty content..")
                .recruitCapacity(4L)
                .contactType(ContactType.KAKAO_OPEN_CHAT)
                .expectedTerm(28L)
                .startDate(LocalDate.of(2024, 10, 23))
                .build();
        postService.registerPost(newPostRequest);
        log.info("new post is registered");
    }

    @Test
    public void 덧글_작성() {
        // given
        Posts findPost = postRepository.findAll().get(0);
        Member findMember = memberService.findAll().get(0);

        // when
        CreateCommentRequest request = CreateCommentRequest.builder()
                .memberId(findMember.getMemberId())
                .postId(findPost.getPostId())
                .content("foo boo")
                .build();
        CreateCommentResponse responseDto = commentService.registerComment(request);

        // then
        Comment findComment = commentService.findCommentById(responseDto.getCommentId());

        Assert.assertEquals("foo boo", findComment.getContent());
    }

    @Test
    public void 덧글_수정() {
        // given
        Posts findPost = postRepository.findAll().get(0);
        Member findMember = memberService.findAll().get(0);

        CreateCommentRequest request = CreateCommentRequest.builder()
                .postId(findPost.getPostId())
                .memberId(findMember.getMemberId())
                .content("foo boo")
                .build();
        CreateCommentResponse responseDto = commentService.registerComment(request);

        // when
        Comment findComment = commentService.findCommentById(responseDto.getCommentId());
        UpdateCommentRequest ucr = UpdateCommentRequest.builder()
                .commentId(findComment.getCommentId())
                .content("foo boo foo")
                .build();

        commentService.updateComment(ucr);

        // then
        Assert.assertEquals("foo boo foo", findComment.getContent());
    }

    @Test
    public void 덧글_조회() {
        // given

        Member findMember = memberService.findMemberByNickname("케네스");
        PostCompactDto findPost = postService.findPostByMemberId(findMember.getMemberId()).get(0);

        CreateCommentRequest request = CreateCommentRequest.builder()
                .memberId(findMember.getMemberId())
                .postId(findPost.getPostId())
                .content("foo boo")
                .build();
        CreateCommentResponse responseDto = commentService.registerComment(request);

        // when
        Comment findCommentWithmemberId = commentService.findCommentByMemberId(findMember.getMemberId()).get(0);
        Comment findCommentWithPostId = commentService.findCommentByPostId(findPost.getPostId()).get(0);

        // then
        Assert.assertEquals("foo boo", findCommentWithPostId.getContent());
        Assert.assertEquals("foo boo", findCommentWithmemberId.getContent());

    }

    @Test(expected = ResourceNotFoundException.class)
    public void 덧글_삭제() {
        // given
        Posts findPost = postRepository.findAll().get(0);
        Member findMember = memberService.findAll().get(0);

        CreateCommentRequest request = CreateCommentRequest.builder()
                .postId(findPost.getPostId())
                .memberId(findMember.getMemberId())
                .content("foo boo")
                .build();
        CreateCommentResponse responseDto = commentService.registerComment(request);

        // when
        commentService.removeComment(responseDto.getCommentId());

        // then
        commentService.findCommentById(responseDto.getCommentId());
        Assert.fail("comment has not been removed");
    }



}
