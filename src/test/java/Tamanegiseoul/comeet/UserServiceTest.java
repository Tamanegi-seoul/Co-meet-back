package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.repository.UserRepository;
import Tamanegiseoul.comeet.service.UserService;

import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

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

    @Test(expected = IllegalStateException.class)
    public void 유저_이메일_중복검사() {

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
        userService.updatePreferStack(newUser.getId(), TechStack.JAVA, TechStack.R);

        // then
        List<TechStack> findStacks = userService.findPreferredStacks(newUser.getId());
        for(TechStack ts : findStacks) {
            log.info("TechStack : " + ts.name());
        }
        Assert.assertEquals(2, findStacks.size());
    }
}
