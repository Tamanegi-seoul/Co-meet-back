package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.Posts;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.user.request.UpdateUserRequest;
import Tamanegiseoul.comeet.repository.CommentRepository;
import Tamanegiseoul.comeet.repository.PostRepository;
import Tamanegiseoul.comeet.repository.StackRelationRepository;
import Tamanegiseoul.comeet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;
import java.util.stream.Collectors;

@Service @Slf4j
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StackRelationRepository stackRelationRepository;
    private final CommentRepository commentRepository;

    private final PostService postService;

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

    /**********************
     * USER UPDATE METHODS
     **********************/

    @Transactional
    public Users updateUser(UpdateUserRequest request) {
        Users findUser = this.findUserById(request.getUserId());
        Long findUserId = findUser.getUserId();
        findUser.changeNickname(request.getNewNickname());
        findUser.changePassword(request.getNewPassword());
        findUser.initPreferredTechStacks();
        em.flush();
        this.updatePreferStack(findUserId, request.getUpdatedStack());
        findUser.updateModifiedDate();
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
        Users findUser = userRepository.findOne(userId);
        findUser.initPreferredTechStacks();
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
