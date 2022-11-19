package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.dto.user.request.UpdateUserRequest;
import Tamanegiseoul.comeet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Long registerUser(Users user) {
        validateUserEmail(user.getEmail());
        validateUserNickname(user.getNickname());
        user.updateCreatedDate();
        user.updateModifiedDate();
        userRepository.save(user);
        return user.getId();
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
        Long findUserId = findUser.getId();
        findUser.changeNickname(request.getNewNickname());
        findUser.changePassword(request.getNewPassword());
        findUser.initPreferredTechStacks();
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
    public void updatePreferStack(Long id, List<TechStack> techStacks) {
        Users findUser = userRepository.findOne(id);
        findUser.initPreferredTechStacks();
        for(TechStack ts : techStacks) {
            findUser.addPreferStack(ts);
        }
        findUser.updateModifiedDate();
    }

    @Transactional
    public void removeUser(Long id) {
        Users findUesr = userRepository.findOne(id);
        em.remove(id);
    }

    /**********************
     * USER SEARCH METHODS
     **********************/

    @Transactional(readOnly = true)
    public Users findUserById(Long id) {
        return userRepository.findOne(id);
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
