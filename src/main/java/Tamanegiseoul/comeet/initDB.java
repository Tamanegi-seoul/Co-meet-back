package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import java.time.LocalDate;

@Component
//@Profile("dev")
@RequiredArgsConstructor
public class initDB {
    private final InitService initService;
    private final MemberService memberService;

    @PostConstruct
    public void init() {
        initService.dbInit();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        public void dbInit() {

            /**
             * implement mock-up users
             */
            Member memberA = Member.builder() // memberId = 1
                    .nickname("Pansy Stone")
                    .email("p.stone@comeet.com")
                    .password("password")
                    .build();
            memberA.addPreferStack(TechStack.R);
            memberA.addPreferStack(TechStack.JAVA);
            memberA.updateCreatedDate();
            memberA.updateModifiedDate();
            em.persist(memberA);

            Member memberB = Member.builder() // memberId = 2
                    .nickname("Carl Craig")
                    .email("c.craig@comeet.com")
                    .password("password")
                    .build();
            memberB.addPreferStack(TechStack.JAVA_SCRIPT);
            memberB.addPreferStack(TechStack.PYTHON);
            memberB.updateCreatedDate();
            memberB.updateModifiedDate();
            em.persist(memberB);

            /**
             * implement mock-up posts
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
            em.persist(postA);

            Posts postB = Posts.builder() // postId = 7
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
            em.persist(postB);

            /**
             * implement mock-up comments
             */
            Comment comA = Comment.builder() // commentId = 9
                    .post(postA)
                    .member(memberA)
                    .content("welcome whoever")
                    .build();
            comA.updateCreatedTime();
            comA.updateModifiedTime();
            em.persist(comA);

            Comment comB = Comment.builder() // commentId = 10
                    .post(postA)
                    .member(memberB)
                    .content("cmf!")
                    .build();
            comB.updateCreatedTime();
            comB.updateModifiedTime();
            em.persist(comB);

            Comment comC = Comment.builder() // commentId = 11
                    .post(postB)
                    .member(memberA)
                    .content("what about JAVA?")
                    .build();
            comC.updateCreatedTime();
            comC.updateModifiedTime();
            em.persist(comC);


        }


    }
}
