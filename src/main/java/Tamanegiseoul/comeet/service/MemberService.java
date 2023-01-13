package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.member.request.JoinMemberRequest;
import Tamanegiseoul.comeet.dto.member.request.UpdateMemberRequest;
import Tamanegiseoul.comeet.dto.member.response.ImageDto;
import Tamanegiseoul.comeet.dto.member.response.JoinMemberResponse;
import Tamanegiseoul.comeet.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.IOException;
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

    private final ImageDataService imageDataService;

    private final PasswordEncoder passwordEncoder;

    @PersistenceContext
    private final EntityManager em;

    @Transactional
    public JoinMemberResponse registerMember(JoinMemberRequest request, MultipartFile image) throws IOException {

        validateMemberEmail(request.getEmail());
        validateMemberNickname(request.getNickname());

        Member newMember = Member.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();


        newMember.updateCreatedDate();
        newMember.updateModifiedDate();
        memberRepository.save(newMember);

        this.addRoleToMember(newMember.getEmail(), "ROLE_USER");
        this.updatePreferStack(newMember.getMemberId(), request.getPreferStacks());

        if(image!=null) {
            ImageDto imageDto = imageDataService.uploadImage(newMember, image);

            return JoinMemberResponse.toDto(newMember).preferStacks(request.getPreferStacks()).profileImage(imageDto);
        }


        return JoinMemberResponse.toDto(newMember).preferStacks(request.getPreferStacks()).profileImage(null);
    }

    @Transactional
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    @Transactional
    public void addRoleToMember(String email, String roleName) {
        Member findMember = memberRepository.findMemberByEmail(email);
        Role roleToAdd = roleRepository.findByRoleName(roleName);
        if(roleToAdd == null) {
            log.warn("no such role {}", roleName);
        } else {
            findMember.addRole(roleToAdd);
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
        Long findMemberId = findMember.getMemberId();
        findMember.changeNickname(request.getNewNickname());
        this.updatePreferStack(findMemberId, request.getUpdatedStacks());
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
        findMember.clearPreferStack();
        for(TechStack ts : techStacks) {
            findMember.addPreferStack(ts);
        }
        findMember.updateModifiedDate();
        em.flush();
        em.clear();
        log.warn("[MemberService:updatePreferStack] updated " + memberId + "'s tech stack" + techStacks.toString());
    }

    @Transactional
    public Long removeMember(Long memberId) throws ResourceNotFoundException {
        Member findMember = this.findMemberById(memberId);

        em.remove(findMember);

        return findMember.getMemberId();
    }

    /**********************
     * Member SEARCH METHODS
     **********************/

    @Transactional(readOnly = true)
    public Member findMemberById(Long memberId) {
        Member findMember = memberRepository.findOne(memberId);

        if(findMember == null) {
            log.info("[MemberService:findMemberById] THE RESULT OF MEMBER ID is NULL ");
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
        log.info("[MemberService:loadUserByUsername]method executed");
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
        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}
