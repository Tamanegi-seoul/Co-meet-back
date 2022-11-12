package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.repository.StackRelationRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StackRelationService {

    private final StackRelationRepository stackRelationRepository;

    @PersistenceContext
    EntityManager em;

    public List<TechStack> findTechStackByPostId(Long postId) {
        List<StackRelation> findSR = stackRelationRepository.findByPostId(postId);
        List<TechStack> findTS = new ArrayList<>();
        findSR.stream().map(e -> findTS.add(e.getTechStack()));
        return findTS;
    }

    public List<TechStack> findTechStackByUserId(Long userId) {
        List<StackRelation> findSR = stackRelationRepository.findByUserId(userId);
        List<TechStack> findTS = new ArrayList<>();
        findSR.stream().map(e -> findTS.add(e.getTechStack()));
        return findTS;
    }

    public int removeAllTechStacksByPostId(Long postId) {
        return stackRelationRepository.removeRelatedStacksByPost(postId);
    }
}
