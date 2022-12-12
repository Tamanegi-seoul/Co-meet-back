package Tamanegiseoul.comeet.service;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.repository.StackRelationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;

@Service @Slf4j
@RequiredArgsConstructor
public class StackRelationService {

    private final StackRelationRepository stackRelationRepository;

    @PersistenceContext
    EntityManager em;

    public List<TechStack> findTechStackByPostId(Long postId) {
        List<StackRelation> findSR = stackRelationRepository.findByPostId(postId);
        log.warn("[StackRelationService:findTechStackByPostId] found "+ findSR.size() + "ea StackRelation from DB");
        List<TechStack> findTS = new ArrayList<>();

        for(StackRelation sr : findSR) {
            log.warn("[StackRelationService:findTechStackByPostId] iterating.. " + sr.getTechStack());
            findTS.add(sr.getTechStack());
        }

        //findSR.stream().map(e -> findTS.add(e.getTechStack())); // need to debug why it didn't work
        log.warn("[StackRelationService:findTechStackByPostId] found "+ findTS.size() + "ea TechStack with given postId");
        return findTS;
    }

    public List<TechStack> findTechStackBymemberId(Long memberId) {
        List<StackRelation> findSR = stackRelationRepository.findByMemberId(memberId);
        List<TechStack> findTS = new ArrayList<>();
        findSR.stream().map(e -> findTS.add(e.getTechStack()));
        return findTS;
    }

    public int removeAllTechStacksByPostId(Long postId) {
        return stackRelationRepository.removeRelatedStacksByPost(postId);
    }
}
