package Tamanegiseoul.comeet.domain;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import com.sun.istack.NotNull;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Table(name = "member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {

    @Id
    @GeneratedValue
    @Column(name = "member_id")
    private Long memberId;

    @NotNull @Column(unique = true)
    private String nickname;

    @NotNull @Column(unique = true)
    private String email;

    @NotNull
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "MEMBER_ROLES",
            joinColumns = @JoinColumn(name = "member_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Collection<Role> roles = new ArrayList<>();

    @Nullable
//    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
    //@OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "owner")
    @OneToOne(mappedBy = "owner", fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
    private ImageData profileImage;

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
    private List<StackRelation> preferStacks = new ArrayList<>();

    @OneToMany(mappedBy = "poster", fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
    private List<Posts> wrotePosts = new ArrayList<>();

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = ALL, orphanRemoval = true)
    private List<Comment> wroteComments = new ArrayList<>();

    @NotNull
    private LocalDateTime createdTime;

    @NotNull
    private LocalDateTime modifiedTime;

    @Builder
    public Member(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    // update status of Users
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changePassword(String newPassword) {
        this.password = newPassword;
    }

    public void addPreferStack(TechStack ts) {
        this.preferStacks.add(StackRelation.builder()
                .member(this)
                .techStack(ts).build());
    }

    public void addProfileImage(ImageData imageData) {
        this.profileImage = imageData;
    }

    public void addWrotePost(Posts post) { this.wrotePosts.add(post); }

    public void addRole(Role role) { this.roles.add(role); }

    public void addWroteComments(Comment comment) { this.wroteComments.add(comment); }
    public void clearPreferStack() {
        this.preferStacks.clear();
    }

    public List<TechStack> exportPreferStack() {
        if(this.preferStacks.size() == 0) {
            return null;
        } else {
            ArrayList<TechStack> list = new ArrayList();
            for(StackRelation ts : this.preferStacks) {
                list.add(ts.getTechStack());
            }
            return list;
        }
    }


    // set LocalTime for created, modified date
    public void updateCreatedDate() {
        this.createdTime = LocalDateTime.now();
    }

    public void updateModifiedDate() {
        this.modifiedTime = LocalDateTime.now();
    }

    @Transactional
    public void initPreferredTechStacks() {
        this.preferStacks.clear();
    }

}
