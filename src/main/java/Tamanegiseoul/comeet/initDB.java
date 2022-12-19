package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.service.CommentService;
import Tamanegiseoul.comeet.service.MemberService;
import Tamanegiseoul.comeet.service.PostService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class initDB {

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

            memberService.registerMember(
                    Member.builder()
                            .email("admin")
                            .nickname("관리자")
                            .password("password")
                            .build()
            );

            memberService.addRoleToMember("admin", "ROLE_ADMIN");

            Member memberA = Member.builder()
                    .nickname("Pansy Stone")
                    .email("p.stone@comeet.com")
                    .password("password")
                    .build();
            memberA.addPreferStack(TechStack.JAVA_SCRIPT);
            memberA.addPreferStack(TechStack.PYTHON);
            memberA.updateCreatedDate();
            memberA.updateModifiedDate();
            memberService.registerMember(memberA);
            memberService.addRoleToMember("p.stone@comeet.com", "ROLE_USER");


            Member memberB = Member.builder() // memberId = 2
                    .nickname("Carl Craig")
                    .email("c.craig@comeet.com")
                    .password("password")
                    .build();
            memberB.addPreferStack(TechStack.JAVA_SCRIPT);
            memberB.addPreferStack(TechStack.PYTHON);
            memberB.updateCreatedDate();
            memberB.updateModifiedDate();
            memberService.registerMember(memberB);
            memberService.addRoleToMember("c.craig@comeet.com", "ROLE_USER");


            /**
             *  SET UT MOCK POST
             */
            Posts postA = Posts.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .poster(memberA)
                    .contact("p.stone@kakao.com")
                    .contactType(ContactType.KAKAO_OPEN_CHAT)
                    .startDate(LocalDate.parse("2021-08-15"))
                    .expectedTerm(21L)
                    .recruitCapacity(4L)
                    .build();
            postA.addDesignateStack(TechStack.JAVA);
            postA.updateCreatedDate();
            postA.updateModifiedDate();
            postService.registerPost(postA);

            Posts postB = Posts.builder()
                    .title("come on, Python algorithm study crew!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-01-23"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postB.addDesignateStack(TechStack.PYTHON);
            postB.updateCreatedDate();
            postB.updateModifiedDate();
            postService.registerPost(postB);

            Posts postC = Posts.builder()
                    .title("POST C!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-01-24"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postC.addDesignateStack(TechStack.PYTHON);
            postC.updateCreatedDate();
            postC.updateModifiedDate();
            postService.registerPost(postC);

            Posts postD = Posts.builder()
                    .title("POST D!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-01-25"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postD.addDesignateStack(TechStack.PYTHON);
            postD.updateCreatedDate();
            postD.updateModifiedDate();
            postService.registerPost(postD);

            Posts postE = Posts.builder()
                    .title("POST E!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-01-26"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postE.addDesignateStack(TechStack.PYTHON);
            postE.updateCreatedDate();
            postE.updateModifiedDate();
            postService.registerPost(postE);

            Posts postF = Posts.builder()
                    .title("POST F!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-02-26"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postF.addDesignateStack(TechStack.PYTHON);
            postF.updateCreatedDate();
            postF.updateModifiedDate();
            postService.registerPost(postF);


            Posts postG = Posts.builder()
                    .title("POST F!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-02-26"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postG.addDesignateStack(TechStack.PYTHON);
            postG.updateCreatedDate();
            postG.updateModifiedDate();
            postService.registerPost(postG);


            Posts postH = Posts.builder()
                    .title("POST H!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-03-26"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postH.addDesignateStack(TechStack.PYTHON);
            postH.updateCreatedDate();
            postH.updateModifiedDate();
            postService.registerPost(postH);

            Posts postI = Posts.builder()
                    .title("POST I!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-03-26"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postI.addDesignateStack(TechStack.PYTHON);
            postI.updateCreatedDate();
            postI.updateModifiedDate();
            postService.registerPost(postI);

            Posts postJ = Posts.builder()
                    .title("POST J!")
                    .content("tba")
                    .poster(memberB)
                    .contact("c.carig@gmail.com")
                    .contactType(ContactType.POSTER_EMAIL)
                    .startDate(LocalDate.parse("2022-03-26"))
                    .expectedTerm(90L)
                    .recruitCapacity(4L)
                    .build();
            postJ.addDesignateStack(TechStack.PYTHON);
            postJ.updateCreatedDate();
            postJ.updateModifiedDate();
            postService.registerPost(postJ);

            /**
             * SET UP MOCK COMMENTS
             */

            Comment comA = Comment.builder() // commentId = 9
                    .post(postA)
                    .member(memberA)
                    .content("welcome whoever")
                    .build();
            comA.updateCreatedTime();
            comA.updateModifiedTime();
            commentService.registerComment(comA);

            Comment comB = Comment.builder() // commentId = 10
                    .post(postA)
                    .member(memberB)
                    .content("cmf!")
                    .build();
            comB.updateCreatedTime();
            comB.updateModifiedTime();
            commentService.registerComment(comB);

            Comment comC = Comment.builder() // commentId = 11
                    .post(postB)
                    .member(memberA)
                    .content("what about JAVA?")
                    .build();
            comC.updateCreatedTime();
            comC.updateModifiedTime();
            commentService.registerComment(comC);
        };
    }
}
