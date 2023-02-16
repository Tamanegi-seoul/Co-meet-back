package Tamanegiseoul.comeet.service;

import Tamanegiseoul.comeet.domain.Member;
import Tamanegiseoul.comeet.domain.Role;
import Tamanegiseoul.comeet.domain.StackRelation;
import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.domain.exception.DuplicateResourceException;
import Tamanegiseoul.comeet.domain.exception.ResourceNotFoundException;
import Tamanegiseoul.comeet.dto.member.request.JoinMemberRequest;
import Tamanegiseoul.comeet.dto.member.request.UpdateMemberRequest;
import Tamanegiseoul.comeet.dto.member.response.*;
import Tamanegiseoul.comeet.repository.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.EmptyResultDataAccessException;
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

    private final RoleRepository roleRepository;

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

        log.info("[MemberService:registerMember] member with email '{}' nickname '{}' are registered.", request.getEmail(), request.getNickname());

        if(image!=null) {
            ImageDto imageDto = imageDataService.uploadImage(newMember, image);
            log.info("[MemberService:registerMember] '{}' member have profile image with file name '{}'", newMember.getNickname(), imageDto.getFileName());
            return JoinMemberResponse.toDto(newMember).preferStacks(request.getPreferStacks()).profileImage(imageDto);
        } else {
            log.info("[MemberService:registerMember] '{}' member doesn't have profile image to register", newMember.getNickname());
        }


        this.addRoleToMember(newMember, "ROLE_USER");
        this.updatePreferStack(newMember, request.getPreferStacks());

        log.info("[MemberService:registerMember] '{}' member prefer stacks '{}'", newMember.getNickname(), request.getPreferStacks().toString());

        return JoinMemberResponse.toDto(newMember).preferStacks(request.getPreferStacks()).profileImage(null);
    }

    @Transactional
    public Role saveRole(Role role){
        return roleRepository.save(role);
    }

    @Transactional
    public void addRoleToMember(Member targetMember, String roleName) {
        Role roleToAdd = roleRepository.findByRoleName(roleName);
        if(roleToAdd == null) {
            log.warn("no such role {}", roleName);
        } else {
            log.info("{} set role with {}", targetMember.getNickname(), roleToAdd.getRoleName());
            targetMember.addRole(roleToAdd);
        }
    }

    /*****************************
     * VALIDATE METHODS FOR MEMBER *
     *****************************/

    @Transactional(readOnly = true)
    public void validateMemberEmail(String email) {
        Member findMember = memberRepository.findMemberByEmail(email);
        if(findMember != null) {
            log.info("[MemberService:ValidateMemberEmail] {} is already in use", email);
            throw new DuplicateResourceException("Members Email", email);
        }
        log.info("[MemberService:ValidateMemberEmail] {} is available", email);
    }

    @Transactional(readOnly = true)
    public void validateMemberNickname(String nickname) {
        Member findMember = memberRepository.findMemberByNickname(nickname);
        if(findMember != null) {
            log.info("[MemberService:ValidateMemberEmail] {} is already in use", nickname);
            throw new DuplicateResourceException("Members Nickname", nickname);
        }
        log.info("[MemberService:ValidateMemberEmail] {} is available", nickname);
    }

    /**********************
     * MEMBER UPDATE METHODS
     **********************/

    @Transactional
    public UpdateMemberResponse updateMember(UpdateMemberRequest request, MultipartFile file) throws IOException {
        Member findMember = memberRepository.findOne(request.getMemberId());
        if(findMember == null) {
            log.info("[MemberService:updateMember] member with member id '{}' not exists", request.getMemberId());
            throw new ResourceNotFoundException("member id", "memberId", request.getMemberId());
        }
        log.info("[MemberService:updateMember] found {} member", findMember.getNickname());

        // validate nickname
        if(!request.getNewNickname().equals(request.getPrevNickname())) {
            // if new nickname is duplicated,
            // DuplicateResourceException will be thrown
            validateMemberNickname(request.getNewNickname());
            findMember.changeNickname(request.getNewNickname());
            log.info("[MemberService:updateMember] updated nickname from '{}' to '{}'", request.getPrevNickname(), request.getNewNickname());
        } else {
            log.info("[MemberService:updateMember] request doesn't need update nickname");
        }

        this.updatePreferStack(findMember, request.getUpdatedStacks());
        log.info("[MemberService:updateMember] updated stacks to '{}'", request.getUpdatedStacks().toString());
        findMember.updateModifiedDate();

        ImageDto imageDto = imageDataService.findImageByMemberId(findMember.getMemberId());

        if(file!=null) {
            log.info("[MemberService:updateMember] request have profile image to upload or update.");
            if(imageDto == null) {
                log.info("[MemberService:updateMember] no registered image for member '{}'", findMember.getNickname());
                imageDto = imageDataService.uploadImage(findMember, file);
            } else {
                log.info("[MemberService:updateMember] found registered profile image '{}'", imageDto.getFileName());
                imageDto = imageDataService.updateImage(findMember, file);
            }
        } else {
            log.info("[MemberService:updateMember] request does not have profile image to upload or update.");
            imageDataService.removeImage(findMember);
        }

        return UpdateMemberResponse.toDto(findMember, imageDto);
    }

    @Transactional
    public void updateMemberPassword(Long id, String password) {
        Member findMember = memberRepository.findOne(id);
        if(findMember == null) {
            log.info("[MemberService:updateMemberPassword] member with member id '{}' not exists", id);
            throw new ResourceNotFoundException("member id", "memberId", id);
        }
        findMember.changePassword(password);
        log.info("[MemberService:updateMemberPassword] '{}' member changed password", findMember.getNickname());
        findMember.updateModifiedDate();
    }


    public void updatePreferStack(Member member, List<TechStack> techStacks) {
        member.clearPreferStack();
        for(TechStack ts : techStacks) {
            member.addPreferStack(ts);
        }
        log.info("[MemberService:updatePreferStack] updated '{}' member's tech stack '{}'", member.getNickname(), member.exportPreferStack());
    }

    @Transactional
    public RemoveMemberResponse removeMember(Long memberId) throws ResourceNotFoundException {
        Member findMember = memberRepository.findOne(memberId);

        if(findMember == null) {
            log.info("[MemberService:removeMember] member with member id {} not exists", memberId);
            throw new ResourceNotFoundException("member id", "memberId", memberId);
        }

        em.remove(findMember);
        log.info("[MemberService:removeMember] '{}' member removed", findMember.getNickname());

        return RemoveMemberResponse.toDto(findMember);
    }

    /**********************
     * Member SEARCH METHODS
     **********************/

    @Transactional(readOnly = true)
    public SearchMemberResponse findMemberById(Long memberId) {
        Member findMember;
        try {
            findMember = memberRepository.findMemberWithStack(memberId);
        } catch (EmptyResultDataAccessException e) {
            log.info("[MemberService:findMemberById] member with member id '{}' not exists", memberId);
            throw new ResourceNotFoundException("member_id", "memberId ", memberId);
        }

        // can't fetch join profile image because it can be null when user doesn't provide it.
        ImageDto findImage = ImageDto.toDto(findMember.getProfileImage());

        log.info("[MemberService:findMemberById] found member with member id {}", memberId);
        return SearchMemberResponse.toDto(findMember, findImage, findMember.getPreferStacks());
    }

    // this method is only for test environment
    @Transactional(readOnly = true)
    public List<Member> findAll() {
        log.info("[MemberService:findAll] finding all member");
        return memberRepository.findAll();
    }

    // this method is only for test environment
    @Transactional(readOnly = true)

    public Member findMemberByNickname(String nickname) {
        log.info("[MemberService:findMemberByNickname] Fetching member with nickname {}", nickname);
        return memberRepository.findMemberByNickname(nickname);
    }

    @Transactional(readOnly = true)
    public Member findMemberByEmail(String email) {
        log.info("[MemberService:findMemberByEmail] Fetching member with email {}", email);
        return memberRepository.findMemberByEmail(email);
    }

    @Transactional(readOnly = true)
    public List<TechStack> findPreferredStacks(Long memberId) {
        List<TechStack> findStacks = new ArrayList<>();
        log.info("[MemberService:findPreferredStacks] Fetching all preferred stacks with member '{}'", memberId);
        List<StackRelation> findStackRelations = memberRepository.findPreferredStacks(memberId);
        for(StackRelation sr : findStackRelations) {
            findStacks.add(sr.getTechStack());
        }
        return findStacks;
    }

    // this method is for Spring Security's UserDetailService
    // in this method, username indicates member email
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member member = memberRepository.findMemberByEmail(email);
        if(member == null) {
            log.error("[MemberService:loadUserByNickname] Member having email {} not found in the database", email);
            throw new UsernameNotFoundException("Member not found in the database");
        } else {
            log.info("[MemberService:loadUserByNickname] Member who has email {} found in the database", email);
        }

        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        member.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getRoleName()));
        });
        return new User(member.getEmail(), member.getPassword(), authorities);
    }
}
