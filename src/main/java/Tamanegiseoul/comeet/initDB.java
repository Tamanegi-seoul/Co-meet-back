package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.comment.request.CreateCommentRequest;
import Tamanegiseoul.comeet.dto.comment.response.CreateCommentResponse;
import Tamanegiseoul.comeet.dto.member.request.JoinMemberRequest;
import Tamanegiseoul.comeet.dto.member.response.JoinMemberResponse;
import Tamanegiseoul.comeet.dto.post.request.CreatePostRequest;
import Tamanegiseoul.comeet.dto.post.response.CreatePostResponse;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.MemberService;
import Tamanegiseoul.comeet.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static Tamanegiseoul.comeet.domain.enums.GroupType.PROJECT;
import static Tamanegiseoul.comeet.domain.enums.GroupType.STUDY;

@Profile("dev")
@Component
public class initDB {

    @Profile("dev")
    @Bean
    CommandLineRunner generateMockData(MemberService memberService, PostService postService, CommentService commentService) {
        return args -> {

            /**
             * INIT ROLE
             */

            memberService.saveRole(Role.builder().roleName("ROLE_USER").build());
            memberService.saveRole(Role.builder().roleName("ROLE_MANAGER").build());
            memberService.saveRole(Role.builder().roleName("ROLE_ADMIN").build());

            /**
             * SET UP MOCK MEMBER
             */

            JoinMemberResponse memberA = memberService.registerMember(
                    JoinMemberRequest.builder()
                            .email("admin")
                            .nickname("관리자")
                            .password("password")
                            .preferStacks(new ArrayList<>(List.of(TechStack.JAVA_SCRIPT, TechStack.PYTHON)))
                            .build(), null
            );


            JoinMemberResponse memberB = memberService.registerMember(
                    JoinMemberRequest.builder()
                            .email("c.craig@comeet.com")
                            .nickname("Carl Craig")
                            .password("password")
                            .preferStacks(new ArrayList<>(List.of(TechStack.JAVA_SCRIPT)))
                            .build(), null
            );

            JoinMemberResponse memberC = memberService.registerMember(
                    JoinMemberRequest.builder()
                            .email("j.Doe@comeet.com")
                            .nickname("John Doe")
                            .password("password")
                            .preferStacks(new ArrayList<>(List.of(TechStack.JAVA_SCRIPT)))
                            .build(), null
            );

            JoinMemberResponse memberD = memberService.registerMember(
                    JoinMemberRequest.builder()
                            .email("eli@comeet.com")
                            .nickname("Elisia")
                            .password("password")
                            .preferStacks(new ArrayList<>(List.of(TechStack.JAVA_SCRIPT)))
                            .build(), null
            );

            JoinMemberResponse memberE = memberService.registerMember(
                    JoinMemberRequest.builder()
                            .email("cooper@comeet.com")
                            .nickname("Cooper")
                            .password("password")
                            .preferStacks(new ArrayList<>(List.of(TechStack.JAVA_SCRIPT)))
                            .build(), null
            );


            /**
             *  SET UT MOCK POST
             */
            CreatePostRequest requestPostA = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postA = postService.registerPost(requestPostA);

            CreatePostRequest requestPostB = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postB = postService.registerPost(requestPostB);

            CreatePostRequest requestPostC = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postC = postService.registerPost(requestPostC);

            CreatePostRequest requestPostD = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postD = postService.registerPost(requestPostD);

            CreatePostRequest requestPostE = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postE = postService.registerPost(requestPostE);

            CreatePostRequest requestPostF = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postF = postService.registerPost(requestPostF);


            CreatePostRequest requestG = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postG = postService.registerPost(requestG);


            CreatePostRequest requestH = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postH = postService.registerPost(requestH);

            CreatePostRequest requestI = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postI = postService.registerPost(requestI);

            CreatePostRequest requestJ = CreatePostRequest.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .posterId(memberA.getMemberId())
                    .contact("p.stone@kakao.com")
                    .groupType(STUDY)
                    .remote(false)
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .designatedStacks(
                            new ArrayList<>(List.of(TechStack.JAVA))
                    )
                    .build();
            CreatePostResponse postJ = postService.registerPost(requestJ);

            /**
             * SET UP MOCK COMMENTS
             */

            CreateCommentRequest requestComA = CreateCommentRequest.builder() // commentId = 9
                    .postId(postA.getPostId())
                    .memberId(memberA.getMemberId())
                    .content("welcome whoever")
                    .build();
            CreateCommentResponse comA = commentService.registerComment(requestComA);

            CreateCommentRequest requestComB = CreateCommentRequest.builder() // commentId = 9
                    .postId(postA.getPostId())
                    .memberId(memberB.getMemberId())
                    .content("welcome whoever")
                    .build();
            CreateCommentResponse comB = commentService.registerComment(requestComB);

            CreateCommentRequest requestComC = CreateCommentRequest.builder() // commentId = 9
                    .postId(postB.getPostId())
                    .memberId(memberA.getMemberId())
                    .content("welcome whoever")
                    .build();
            CreateCommentResponse comC = commentService.registerComment(requestComC);

            CreateCommentRequest requestComD = CreateCommentRequest.builder() // commentId = 9
                    .postId(postA.getPostId())
                    .memberId(memberB.getMemberId())
                    .content("new comment")
                    .build();
            CreateCommentResponse comD = commentService.registerComment(requestComD);

            CreateCommentRequest requestComE = CreateCommentRequest.builder() // commentId = 9
                    .postId(postA.getPostId())
                    .memberId(memberC.getMemberId())
                    .content("new comment")
                    .build();
            CreateCommentResponse comE = commentService.registerComment(requestComE);

            CreateCommentRequest requestComF = CreateCommentRequest.builder() // commentId = 9
                    .postId(postA.getPostId())
                    .memberId(memberD.getMemberId())
                    .content("new comment")
                    .build();
            CreateCommentResponse comF = commentService.registerComment(requestComF);

            CreateCommentRequest requestComG = CreateCommentRequest.builder() // commentId = 9
                    .postId(postA.getPostId())
                    .memberId(memberE.getMemberId())
                    .content("new comment")
                    .build();
            CreateCommentResponse comG = commentService.registerComment(requestComG);
        };
    }
}
