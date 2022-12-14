package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.member.request.UpdateMemberRequest;
import Tamanegiseoul.comeet.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.*;

@Service @Slf4j
@RequiredArgsConstructor
public class MemberService implements UserDetailsService {

    private final MemberRepository memberRepository;
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
    public Long registerMember(Member member) {
        log.info("register new member {} to the database", member.getNickname());
        validateMemberEmail(member.getEmail());
        validateMemberNickname(member.getNickname());
        member.changePassword(passwordEncoder.encode(member.getPassword())); // encrypt password
        member.updateCreatedDate();
        member.updateModifiedDate();
        memberRepository.save(member);
        return member.getMemberId();
    }

    @Transactional
    public Role saveRole(Role role){
        log.info("register new role {} to the database", role.getRoleName());
        return roleRepository.save(role);
    }

    @Transactional
    public void addRoleToMember(String email, String roleName) {
        log.info("Adding role {} to member {}", roleName, email);

        Member member = memberRepository.findMemberByEmail(email);
        Role role = roleRepository.findByRoleName(roleName);
        if(role == null) {
            log.warn("no such role {}", roleName);
        } else {
            member.getRoles().add(role);
        }
    }

    /*****************************
     * VALIDATE METHODS FOR MEMBER *
     *****************************/

    @Transactional(readOnly = true)
    public void validateMemberEmail(String email) {
        Member findMember = memberRepository.findMemberByEmail(email);
        if(findMember != null) {
            throw new DuplicateResourceException("Members Email", email);
        }
    }

    @Transactional(readOnly = true)
    public void validateMemberNickname(String nickname) {
        Member findMember = memberRepository.findMemberByNickname(nickname);
        if(findMember != null) {
            throw new DuplicateResourceException("Members Nickname", nickname);
        }
    }

    /**********************
     * MEMBER UPDATE METHODS
     **********************/

    @Transactional
    public Member updateMember(UpdateMemberRequest request) {
        Member findMember = this.findMemberById(request.getMemberId());
        Long findmemberId = findMember.getMemberId();
        findMember.changeNickname(request.getNewNickname());
        findMember.changePassword(passwordEncoder.encode(request.getNewPassword()));
        //findMember.initPreferredTechStacks();
        this.updatePreferStack(findmemberId, request.getUpdatedStacks());
        findMember.updateModifiedDate();
        em.flush();
        em.clear();
        return findMember;
    }

    @Transactional
    public void updateMemberNickname(Long id, String newNickname) {
        validateMemberNickname(newNickname);
        Member findMember = memberRepository.findOne(id);
        findMember.changeNickname(newNickname);
        findMember.updateModifiedDate();
    }

    @Transactional
    public void updateMemberPassword(Long id, String password) {
        Member findMember = memberRepository.findOne(id);
        findMember.changePassword(password);
        findMember.updateModifiedDate();
    }

    @Transactional
    public void updatePreferStack(Long memberId, List<TechStack> techStacks) {
        log.warn("[MemberService:updatePreferStack] method init");
        Member findMember = this.findMemberById(memberId); // checked
        //findMember.initPreferredTechStacks();
        stackRelationRepository.removeRelatedStakcsByMember(memberId);
        for(TechStack ts : techStacks) {
            findMember.addPreferStack(ts);
        }
        findMember.updateModifiedDate();
        em.flush();
        em.clear();
        log.warn("[MemberService:updatePreferStack] updated " + memberId + "'s tech stack" + techStacks.toString());
    }

    @Transactional
    public int removeMember(Long memberId) throws ResourceNotFoundException {
        Member findMember = this.findMemberById(memberId);

        // first of all,
        // delete related child entities
        stackRelationRepository.removeRelatedStakcsByMember(memberId);
        commentRepository.removeCommentBymemberId(memberId);
        imageDataRepository.removeImageByMemberId(memberId);
        //postRepository.removePostByPosterId(memberId);
        postService.removePostByPosterId(memberId);

        // then, remove parent entity
        return memberRepository.removeByMemberId(memberId);
    }

    /**********************
     * Member SEARCH METHODS
     **********************/

    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        Member findMember = memberRepository.findOne(memberId);

        if(findMember == null) {
            log.info("[MemberService:findMemberById] THE RESULT OF FIND ONE is NULL ");
            throw new ResourceNotFoundException("member_id", "memberId ", memberId);
        } else {
            log.info("[MemberService:findMemberById] find user with provided member id : " + memberId);
            return findMember;
        }
    }

    @Transactional(readOnly = true)
    public List<Member> findAll() {
        log.info("Fetching all Members");
        return memberRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Member findMemberByNickname(String nickname) {
        log.info("Fetching member {}", nickname);
        return memberRepository.findMemberByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        log.info("Fetching member with eamil {}", email);
        return memberRepository.findMemberByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<TechStack> findPreferredStacks(Long memberId) {
        List<TechStack> findStacks = new ArrayList<>();
        List<StackRelation> findStackRelations = memberRepository.findPreferredStacks(memberId);
        for(StackRelation sr : findStackRelations) {
            findStacks.add(sr.getTechStack());
        }
        return findStacks;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(email);
        if(member == null) {
            log.error("Member having email {} not found in the database", email);
            throw new UsernameNotFoundException("Member not found in the database");
        } else {
            log.info("Member who has email {} found in the database", email);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        member.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        return new org.springframework.security.core.userdetails.User(member.getEmail(), member.getPassword(), authorities);
    }
}
