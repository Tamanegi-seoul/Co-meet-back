package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.user.request.UpdateUserRequest;
import Tamanegiseoul.comeet.dto.user.response.ImageUploadResponse;
import Tamanegiseoul.comeet.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service @Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StackRelationRepository stackRelationRepository;
    private final CommentRepository commentRepository;
    private final ImageDataRepository imageDataRepository;

    private final PostService postService;

    private PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public Long registerUser(Users user) {
        validateUserEmail(user.getEmail());
        validateUserNickname(user.getNickname());

        user.updateCreatedDate();
        user.updateModifiedDate();
        userRepository.save(user);
        return user.getUserId();
    }


    /*****************************
     * VALIDATE METHODS FOR USER *
     *****************************/

    @Transactional(readOnly = true)
    public void validateUserEmail(String email) {
        Users findUser = userRepository.findUserByEmail(email);
        if(findUser != null) {
            throw new DuplicateResourceException("Users Email", email);
        }
    }

    @Transactional(readOnly = true)
    public void validateUserNickname(String nickname) {
        Users findUser = userRepository.findUserByNickname(nickname);
        if(findUser != null) {
            throw new DuplicateResourceException("Users Nickname", nickname);
        }
    }

    public Users getByCredentials(final String email, final String password, final PasswordEncoder encoder) {
        final Users originalUser = userRepository.findUserByEmail(email);

        // matches 메소드를 이용해서 패스워드가 같은지 확인
        if(originalUser != null && encoder.matches(password, originalUser.getPassword())) {
            log.warn("[UserService:getByCredentials] success to validate user password");
            return originalUser;
        }
        log.warn("[UserService:getByCredentials] fail to validate user password");
        return null;
    }

    /**********************
     * USER UPDATE METHODS
     **********************/

    @Transactional
    public Users updateUser(UpdateUserRequest request) {
        Users findUser = this.findUserById(request.getUserId());
        Long findUserId = findUser.getUserId();
        findUser.changeNickname(request.getNewNickname());
        findUser.changePassword(passwordEncoder.encode(request.getNewPassword()));
        //findUser.initPreferredTechStacks();
        this.updatePreferStack(findUserId, request.getUpdatedStacks());
        findUser.updateModifiedDate();
        em.flush();
        em.clear();
        return findUser;
    }

    @Transactional
    public void updateUserNickname(Long id, String newNickname) {
        validateUserNickname(newNickname);
        Users findUser = userRepository.findOne(id);
        findUser.changeNickname(newNickname);
        findUser.updateModifiedDate();
    }

    @Transactional
    public void updateUserPassword(Long id, String password) {
        Users findUser = userRepository.findOne(id);
        findUser.changePassword(password);
        findUser.updateModifiedDate();
    }

    @Transactional
    public void updatePreferStack(Long userId, List<TechStack> techStacks) {
        log.warn("[UserService:updatePreferStack] method init");
        Users findUser = this.findUserById(userId); // checked
        //findUser.initPreferredTechStacks();
        stackRelationRepository.removeRelatedStakcsByUser(userId);
        for(TechStack ts : techStacks) {
            findUser.addPreferStack(ts);
        }
        findUser.updateModifiedDate();
        em.flush();
        em.clear();
        log.warn("[UserService:updatePreferStack] updated " + userId + "'s tech stack" + techStacks.toString());
    }

    @Transactional
    public int removeUser(Long userId) throws ResourceNotFoundException {
        Users findUser = this.findUserById(userId);

        // first of all,
        // delete related child entities
        stackRelationRepository.removeRelatedStakcsByUser(userId);
        commentRepository.removeCommentByUserId(userId);
        imageDataRepository.removeImageByUserId(userId);
        //postRepository.removePostByPosterId(userId);
        postService.removePostByPosterId(userId);

        // then, remove parent entity
        return userRepository.removeByUserId(userId);
    }

    /**********************
     * USER SEARCH METHODS
     **********************/

    @Transactional(readOnly = true)
    public Users findUserById(Long userId) {
        Users findUser = userRepository.findOne(userId);

        if(findUser == null) {
            log.info("[UserService:findUserById] THE RESULT OF FIND ONE is NULL ");
            throw new ResourceNotFoundException("user_id", "userId ", userId);
        } else {
            log.info("[UserService:findUserById] find user with provided user id : " + userId);
            return findUser;
        }
    }

    @Transactional(readOnly = true)
    public List<Users> findAll() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Users findUserByNickname(String nickname) {
        return userRepository.findUserByNickname(nickname);
    }

    public List<TechStack> findPreferredStacks(Long userId) {
        List<TechStack> findStacks = new ArrayList<>();
        List<StackRelation> findStackRelations = userRepository.findPreferredStacks(userId);
        for(StackRelation sr : findStackRelations) {
            findStacks.add(sr.getTechStack());
        }

        return findStacks;
    }

}
