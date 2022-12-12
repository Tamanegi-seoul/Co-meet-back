package Tamanegiseoul.comeet.repository;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.StackRelation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class StackRelationRepository {
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public Member findMemberPreferredStackByNickname(String nickname) {
        return em.createQuery("select m from Member m where m.nickname = :nickname", Member.class)
                .getSingleResult();
    }

    public List<StackRelation> findByPostId(Long postId) {
        return em.createQuery("select sr from StackRelation sr where sr.post.postId = :postId", StackRelation.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public List<StackRelation> findByMemberId(Long memberId) {
        return em.createQuery("select sr from StackRelation sr where sr.member.memberId = :memberId", StackRelation.class)
                .setParameter("memberId", memberId)
                .getResultList();
    }

    public int removeRelatedStacksByPost(Long postId) {
        return em.createQuery("delete from StackRelation sr where sr.post.postId = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
    }

    public int removeRelatedStakcsByMember(Long memberId) {
        log.warn("[StackRelationRepository:removeRelatedStacksByUser]method init");
        return em.createQuery("delete from StackRelation sr where sr.member.memberId = :memberId")
                .setParameter("memberId", memberId)
                .executeUpdate();
    }


}
