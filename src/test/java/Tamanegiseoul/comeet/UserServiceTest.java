package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.repository.UserRepository;
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

import java.util.ArrayList;
import java.util.List;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class UserServiceTest {

    @Autowired UserService userService;
    @Autowired UserRepository userRepository;

    @Test
    public void 단일_유저_생성() throws Exception {
        // given
        Users newUser = Users.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();

        // when
        userService.registerUser(newUser);

        // then
        Users findUser = userService.findUserByNickname("test_user");
        Assert.assertEquals(findUser.getEmail(), "testuser@gmail.com");
    }

    @Test(expected = DuplicateResourceException.class)
    public void 유저_이메일_중복검사() {
        // given
        Users newUser = Users.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);

        // when
        Users otherUser = Users.builder()
                .nickname("other_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(otherUser);

        // then
        Assert.fail("something goes wrong");
    }

    @Test(expected = DuplicateResourceException.class)
    public void 유저_닉네임_중복검사() {
        // given
        Users newUser = Users.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);

        // when
        Users otherUser = Users.builder()
                .nickname("test_user")
                .email("otherUser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(otherUser);

        // then
        Assert.fail("something goes wrong");
    }

    @Test
    public void 기술스택_세팅() {
        // given
        Users newUser = Users.builder()
                .nickname("test_user")
                .email("testuser@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);

        // when
        userService.updatePreferStack(newUser.getId(), new ArrayList<>(List.of(TechStack.R, TechStack.JAVA_SCRIPT)));

        // then
        List<TechStack> findStacks = userService.findPreferredStacks(newUser.getId());
        for(TechStack ts : findStacks) {
            log.info("TechStack : " + ts.name());
        }
        Assert.assertEquals(2, findStacks.size());
    }

    @Test
    //@Rollback(false)
    public void 기술스택_수정() {
        // given
        Users newUser = Users.builder()
                .nickname("woogie")
                .email("woogie@gmail.com")
                .password("password")
                .build();
        userService.registerUser(newUser);
        userService.updatePreferStack(newUser.getId(), new ArrayList<>(List.of(TechStack.R, TechStack.JAVA)));

        // when
        userService.updatePreferStack(newUser.getId(), new ArrayList<>(List.of(TechStack.JAVA_SCRIPT, TechStack.PYTHON)));

        // then
        List<TechStack> findStacks = userService.findPreferredStacks(newUser.getId());
        for(TechStack ts : findStacks) {
            log.info("TechStack : " + ts.name());
        }
        Assert.assertEquals("PYTHON", findStacks.get(0).name());
        Assert.assertEquals(2, findStacks.size());
    }
}
