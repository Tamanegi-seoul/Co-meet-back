package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.User;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.user.request.UpdateUserRequest;
import Tamanegiseoul.comeet.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service @Slf4j
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final StackRelationRepository stackRelationRepository;
    private final CommentRepository commentRepository;
    private final ImageDataRepository imageDataRepository;
    private final RoleRepository roleRepository;

    private final PostService postService;

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public Long registerUser(User user) {
        log.info("register new user {} to the database", user.getNickname());
        validateUserEmail(user.getEmail());
        validateUserNickname(user.getNickname());
        user.changePassword(passwordEncoder.encode(user.getPassword())); // encrypt password
        user.updateCreatedDate();
        user.updateModifiedDate();
        userRepository.save(user);
        return user.getUserId();
    }

    @Transactional
    public Role saveRole(Role role){
        log.info("register new role {} to the database", role.getRoleName());
        return roleRepository.save(role);
    }

    @Transactional
    public void addRoleToUser(String nickname, String roleName) {
        log.info("Adding role {} to user {}", roleName, nickname);

        User user = userRepository.findUserByNickname(nickname);
        Role role = roleRepository.findByRoleName(roleName);
        if(role == null) {
            log.warn("no such role {}", roleName);
        } else {
            user.getRoles().add(role);
        }
    }

    /*****************************
     * VALIDATE METHODS FOR USER *
     *****************************/

    @Transactional(readOnly = true)
    public void validateUserEmail(String email) {
        User findUser = userRepository.findUserByEmail(email);
        if(findUser != null) {
            throw new DuplicateResourceException("Users Email", email);
        }
    }

    @Transactional(readOnly = true)
    public void validateUserNickname(String nickname) {
        User findUser = userRepository.findUserByNickname(nickname);
        if(findUser != null) {
            throw new DuplicateResourceException("Users Nickname", nickname);
        }
    }

    /**********************
     * USER UPDATE METHODS
     **********************/

    @Transactional
    public User updateUser(UpdateUserRequest request) {
        User findUser = this.findUserById(request.getUserId());
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
        User findUser = userRepository.findOne(id);
        findUser.changeNickname(newNickname);
        findUser.updateModifiedDate();
    }

    @Transactional
    public void updateUserPassword(Long id, String password) {
        User findUser = userRepository.findOne(id);
        findUser.changePassword(password);
        findUser.updateModifiedDate();
    }

    @Transactional
    public void updatePreferStack(Long userId, List<TechStack> techStacks) {
        log.warn("[UserService:updatePreferStack] method init");
        User findUser = this.findUserById(userId); // checked
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
        User findUser = this.findUserById(userId);

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
    public User findUserById(Long userId) {
        User findUser = userRepository.findOne(userId);

        if(findUser == null) {
            log.info("[UserService:findUserById] THE RESULT OF FIND ONE is NULL ");
            throw new ResourceNotFoundException("user_id", "userId ", userId);
        } else {
            log.info("[UserService:findUserById] find user with provided user id : " + userId);
            return findUser;
        }
    }

    @Transactional(readOnly = true)
    public List<User> findAll() {
        log.info("Fetching all users");
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User findUserByNickname(String nickname) {
        log.info("Fetching user {}", nickname);
        return userRepository.findUserByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public User findUserByEmail(String email) {
        log.info("Fetching user with eamil {}", email);
        return userRepository.findUserByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<TechStack> findPreferredStacks(Long userId) {
        List<TechStack> findStacks = new ArrayList<>();
        List<StackRelation> findStackRelations = userRepository.findPreferredStacks(userId);
        for(StackRelation sr : findStackRelations) {
            findStacks.add(sr.getTechStack());
        }

        return findStacks;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findUserByEmail(email);
        if(user == null) {
            log.error("User having email {} not found in the database", email);
            throw new UsernameNotFoundException("User not found in the database");
        } else {
            log.info("User who has email {} found in the database", email);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(), authorities);
    }
}
