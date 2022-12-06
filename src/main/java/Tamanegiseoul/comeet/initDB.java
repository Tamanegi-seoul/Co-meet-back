package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Comment;
import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.ContactType;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
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
            Users userA = Users.builder() // userId = 1
                    .nickname("Pansy Stone")
                    .email("p.stone@comeet.com")
                    .password("password")
                    .build();
            userA.addPreferStack(TechStack.R);
            userA.addPreferStack(TechStack.JAVA);
            userA.updateCreatedDate();
            userA.updateModifiedDate();
            em.persist(userA);

            Users userB = Users.builder() // userId = 2
                    .nickname("Carl Craig")
                    .email("c.craig@comeet.com")
                    .password("password")
                    .build();
            userB.addPreferStack(TechStack.JAVA_SCRIPT);
            userB.addPreferStack(TechStack.PYTHON);
            userB.updateCreatedDate();
            userB.updateModifiedDate();
            em.persist(userB);

            /**
             * implement mock-up posts
             */

            Posts postA = Posts.builder() // postId = 5
                    .title("need JAVA study crew!")
                    .content("tba")
                    .poster(userA)
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
                    .poster(userB)
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
                    .user(userA)
                    .content("welcome whoever")
                    .build();
            comA.updateCreatedTime();
            comA.updateModifiedTime();
            em.persist(comA);

            Comment comB = Comment.builder() // commentId = 10
                    .post(postA)
                    .user(userB)
                    .content("cmf!")
                    .build();
            comB.updateCreatedTime();
            comB.updateModifiedTime();
            em.persist(comB);

            Comment comC = Comment.builder() // commentId = 11
                    .post(postB)
                    .user(userA)
                    .content("what about JAVA?")
                    .build();
            comC.updateCreatedTime();
            comC.updateModifiedTime();
            em.persist(comC);

        }


    }
}
