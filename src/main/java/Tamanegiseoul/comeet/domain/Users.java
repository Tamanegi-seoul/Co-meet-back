package Tamanegiseoul.comeet.domain;

import Tamanegiseoul.comeet.domain.enums.TechStack;
import Tamanegiseoul.comeet.dto.user.response.SearchUserResponse;
import com.sun.istack.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static javax.persistence.CascadeType.ALL;

@Entity
@Getter
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Users {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    private Long userId;

    @NotNull @Column(unique = true)
    private String nickname;

    @NotNull @Column(unique = true)
    private String email;

    @NotNull
    private String password;


    private String roles; // USER, ADMIN

    @Nullable
    @OneToOne @JoinColumn(name = "image_id")
    private ImageData profileImage;

    @OneToMany(mappedBy = "stackRelationId", cascade = ALL, orphanRemoval = true)
    private List<StackRelation> preferStacks = new ArrayList<>();

    @OneToMany(mappedBy = "postId", cascade = ALL, orphanRemoval = true)
    private List<Posts> wrotePosts = new ArrayList<>();

    @NotNull
    private LocalDateTime createdTime;

    @NotNull
    private LocalDateTime modifiedTime;

    @Builder
    public Users(String nickname, String email, String password) {
        this.nickname = nickname;
        this.email = email;
        this.password = password;
    }

    // update status of Users
    public void changeNickname(String newNickname) {
        this.nickname = newNickname;
    }

    public void changePassword(String newPassword) {
        this.password = password;
    }

    public void addPreferStack(TechStack ts) {
        //StackRelation newSt = StackRelation.createForUser(this, ts);
        this.preferStacks.add(StackRelation.builder()
                .user(this)
                .techStack(ts).build());
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
        //this.preferStacks = new ArrayList<>();
        this.preferStacks.clear();
    }

    public List<String> getRoleList() {
        if(this.roles.length() > 0) {
            return Arrays.asList(this.roles.split(","));
        }
        return new ArrayList<>();
    }
}
