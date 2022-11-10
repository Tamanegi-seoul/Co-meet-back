package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.Users;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @PersistenceContext
    EntityManager em;

    @Transactional
    public Long registerUser(Users user) {
        user.updateCreatedDate();
        userRepository.save(user);
        return user.getId();
    }

    // update methods
    @Transactional
    public void updateUserNickname(Long id, String newNickname) {
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
    public void updatePreferStack(Long id, TechStack...techStacks) {
        Users findUser = userRepository.findOne(id);
        for(TechStack ts : techStacks) {
            findUser.addPreferStack(ts);
        }
    }

    // below search methods

    @Transactional(readOnly = true)
    public Users findUserById(Long id) {
        return userRepository.findOne(id);
    }

    @Transactional(readOnly = true)
    public Users findUserByNickname(String nickname) {
        return userRepository.findUserByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public List<Users> findAll() {
        return userRepository.findAll();
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
