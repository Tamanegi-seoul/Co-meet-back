package Tamanegiseoul.comeet;

import Tamanegiseoul.comeet.domain.Users;
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


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
@Slf4j
public class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    UserRepository userRepository;

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
}
